package com.vesalaakso.rbb.states;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.controller.CameraController;
import com.vesalaakso.rbb.controller.CameraLimiter;
import com.vesalaakso.rbb.controller.DebugKeyController;
import com.vesalaakso.rbb.controller.InputMaster;
import com.vesalaakso.rbb.controller.MapChanger;
import com.vesalaakso.rbb.controller.MenuKeyController;
import com.vesalaakso.rbb.controller.PlayerListener;
import com.vesalaakso.rbb.controller.PlayerPositioner;
import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.controller.RubberBandController;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.Background;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.GameStatus;
import com.vesalaakso.rbb.model.EffectManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.RubberBand;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;
import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.BackgroundPainter;
import com.vesalaakso.rbb.view.DebugPrintPainter;
import com.vesalaakso.rbb.view.GameStatusPainter;
import com.vesalaakso.rbb.view.PainterContainer;
import com.vesalaakso.rbb.view.ParticleSystemPainter;
import com.vesalaakso.rbb.view.PhysicsPainter;
import com.vesalaakso.rbb.view.PlayerPainter;
import com.vesalaakso.rbb.view.RubberBandPainter;
import com.vesalaakso.rbb.view.TileMapAreaPainter;
import com.vesalaakso.rbb.view.TileMapBackLayerPainter;
import com.vesalaakso.rbb.view.TileMapOverLayerPainter;

/**
 * The state which handles the game logic.
 * 
 * @author Vesa Laakso
 */
public class GameState extends BasicGameState {

	/**
	 * The current map is always stored in this <code>TileMapContainer</code>.
	 * The underlying <code>TileMap</code> object may change but this object
	 * stays.
	 */
	private final TileMapContainer mapContainer = new TileMapContainer();

	/** The current game status is stored in this attribute. */
	private final GameStatus gameStatus;

	/**
	 * The <code>MapChanger</code> responsible for changing the map and
	 * notifying everything that needs to be notified.
	 */
	private final MapChanger mapChanger;

	/**
	 * A <code>PainterContainer</code> which stores everything that can be
	 * painted.
	 */
	private final PainterContainer painterContainer;

	/**
	 * All the stuff which need to be updated in update()-method are stored
	 * here.
	 */
	private List<Updateable> updateables = new LinkedList<Updateable>();

	/** All the stuff which need to be reset when state is changed */
	private List<Resetable> resetables = new LinkedList<Resetable>();

	/** InputMaster to handle the coordination of various input controllers. */
	private InputMaster inputMaster;

	/** What would a game be without a player? */
	private Player player;

	/** The player is controlled by this rubber band! */
	private RubberBand rubberBand;

	/** Ooh, effects, yes please! */
	private final EffectManager effectManager;

	/** Of course we need physics, here it is! */
	private Physics physics;

	/** And some eye candy. */
	private Background background;

	/** A little something to stop the camera from going to distances. */
	private CameraLimiter cameraPositionLimiter =
		new CameraLimiter(mapContainer);

	/** If the game should stop at next update()-call, this flag knows. */
	private boolean stopAtNextUpdate;

	/** Is the game in debug mode? */
	private boolean debugMode = false;

	/** Change to this map on the next update() call. */
	private int changeToLevel = -1;

	/** The state which handles map changing routines. */
	private MapChangeState mapChangeState;

	/** We need something to do something when player does something. */
	private PlayerListener playerListener;

	/** Has this state been initialized in render call before enter already */
	private boolean renderInitializedBeforeEnter = false;

	/**
	 * Construct the GameState and tells it which state is responsible for map
	 * changing routines and also the GameStatus which handles the scores and
	 * all.
	 * 
	 * @param mapChangeState
	 *            the state which handles map changing routines.
	 * @param gameStatus
	 *            the game status where the game status is stored in ":D"
	 * @param resourceManager
	 *            the resource manager to associate with a new
	 *            {@link PainterContainer}
	 */
	public GameState(MapChangeState mapChangeState, GameStatus gameStatus,
			ResourceManager resourceManager) {
		this.mapChangeState = mapChangeState;
		this.gameStatus = gameStatus;
		this.mapChanger = new MapChanger(mapContainer, gameStatus);
		this.painterContainer = new PainterContainer(resourceManager);
		this.effectManager = new EffectManager(resourceManager);
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new BackgroundPainter(background));
		painterContainer.addPainter(new TileMapAreaPainter(mapContainer));
		painterContainer.addPainter(new TileMapBackLayerPainter(mapContainer));
		painterContainer.addPainter(new PlayerPainter(player));
		painterContainer.addPainter(new TileMapOverLayerPainter(mapContainer));
		painterContainer.addPainter(new RubberBandPainter(rubberBand));
		painterContainer.addPainter(new ParticleSystemPainter(effectManager));
		painterContainer.addPainter(new GameStatusPainter(gameStatus));
		painterContainer.addDebugPainter(new PhysicsPainter(physics));
		painterContainer
				.addDebugPainter(new DebugPrintPainter(physics, player));
	}

	/**
	 * A helper method which adds all the controllers and updateables to the
	 * game.
	 */
	private void addControllers(Input input) {
		inputMaster = new InputMaster(input);
		inputMaster.addMouseListener(new CameraController(player));
		inputMaster.addKeyListener(new MenuKeyController(this));
		inputMaster.addMouseListener(new RubberBandController(rubberBand));
		inputMaster.addKeyListener(new DebugKeyController(this, player));
		inputMaster
				.addMouseListener(new PlayerPositioner(player, mapContainer));
	}

	/**
	 * A helper method for listing up everything that needs to be reset before
	 * first render.
	 */
	private void addResetables() {
		resetables.add(inputMaster);
		resetables.add(player);
		resetables.add(cameraPositionLimiter);
		resetables.add(rubberBand);
		resetables.add(physics);
		resetables.add(effectManager);
		resetables.add(background);
	}

	/** A helper method which adds all updateables. */
	private void addUpdateables() {
		updateables.add(inputMaster);
		updateables.add(cameraPositionLimiter);
		updateables.add(physics);
		updateables.add(playerListener);
		updateables.add(background);
		updateables.add(effectManager);
	}

	/**
	 * Resets the state of the game, useful when transitions cause headaches.
	 * Mainly resets positions of various stuff.
	 */
	private void resetBeforeRender() {
		if (renderInitializedBeforeEnter) {
			// No need to do anything.
			return;
		}

		stopAtNextUpdate = false;
		changeToLevel = -1;
		inputMaster.unpause();

		// Reset camera at the middle of the spawn area
		TileMapObject spawn = mapContainer.getMap().getSpawnArea();
		Camera cam = Camera.get();
		cam.setPosition(spawn.x + spawn.width / 2, spawn.y + spawn.height / 2);

		for (Resetable r : resetables) {
			r.reset();
		}

		// This has been done now.
		renderInitializedBeforeEnter = true;
	}

	/**
	 * Changes the level on the next update() call to the one given.
	 * 
	 * @param level
	 *            the level index to change to.
	 */
	private void changeLevel(int level) {
		if (level < 1 || level > RubberBandBall.LEVEL_COUNT) {
			this.changeToLevel = -1;
			this.stop();
		}
		else {
			this.changeToLevel = level;
		}
	}

	/**
	 * Makes the game quit on the next update-loop.
	 */
	public void stop() {
		stopAtNextUpdate = true;
	}

	/** Changes to the next level */
	public void changeToNextLevel() {
		changeLevel(mapContainer.getMap().getLevel() + 1);
	}

	/** Resets the current level */
	public void resetLevel() {
		changeLevel(mapContainer.getMap().getLevel());
	}

	/** Changes to the previous level */
	public void changeToPreviousLevel() {
		changeLevel(mapContainer.getMap().getLevel() - 1);
	}

	/** Toggles debug mode */
	public void toggleDebug() {
		debugMode = !debugMode;
	}

	/**
	 * Gets the debug status of the game.
	 * 
	 * @return <code>true</code> if debug mode is on
	 */
	public boolean isDebugModeToggled() {
		return debugMode;
	}

	/**
	 * Called when a player fails the map.
	 * 
	 * @param reason
	 *            the reason why game ended
	 */
	public void failedMap(String reason) {
		// TODO: something better and moar graphical.
		System.out.println("GAME OVER -- " + reason);
		System.out.println(gameStatus);
		resetLevel();
		gameStatus.onMapFailed();
	}

	/**
	 * Called when a player successfully completes the map.
	 */
	public void completedMap() {
		// TODO: something better and moar graphical.
		System.out.println("Yay!");
		System.out.println(gameStatus);
		changeToNextLevel();
		gameStatus.onMapCompleted();
	}

	/**
	 * From slick: Initialise the state. It should load any resources it needs
	 * at this stage
	 * 
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// Remove this as the input handler, as input handling is done via
		// InputMaster class and not this.
		container.getInput().removeListener(this);

		// Load the background image
		background = new Background();

		// Construct the object representing the player
		player = new Player(mapContainer, gameStatus);

		// Physics world, too
		physics = new Physics(player, effectManager, mapContainer);

		// Add the rubber band to the game
		rubberBand = new RubberBand(player, physics);

		// The player listener. Oh yes.
		playerListener =
			new PlayerListener(mapContainer, player, physics, this);

		// Add the painters next
		addPainters();

		// And then the controllers
		addControllers(container.getInput());

		// Pause the controls at first.
		inputMaster.pause();

		// Some stuff need to be reset before first render.
		addResetables();

		// Finally add all objects which hook to the update()-method.
		addUpdateables();

		// Then reset the map.
		mapChanger.changeMap(1);
		try {
			mapChanger.runChange();
		}
		catch (MapException e) {
			// So we couldn't load even the first map... that's sad.
			Log.error("Failed to change to the first map.");
			throw new SlickException("Failed to change to the first map.", e);
		}
	}

	/**
	 * From slick: Render this state to the game's graphics context
	 * 
	 * @param g
	 *            The graphics context that can be used to render. However,
	 *            normal rendering routines can also be used.
	 * 
	 * @see org.newdawn.slick.Game#render(org.newdawn.slick.GameContainer,
	 *      org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		// Initialize some positions before rendering, because of transitions.
		resetBeforeRender();

		painterContainer.paintAll(g, this);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// The game was not meant to last forever, my friend.
		if (stopAtNextUpdate) {
			Transition leave = new FadeOutTransition();
			Transition enter = new FadeInTransition();
			// Pause controls when entering the menu
			inputMaster.pause();
			game.enterState(State.MAIN_MENU.ordinal(), leave, enter);
			return;
		}
		if (changeToLevel > 0) {
			Transition leave = new FadeOutTransition();
			mapChanger.changeMap(changeToLevel);
			mapChangeState.setupChange(mapChanger, this);
			game.enterState(mapChangeState.getID(), leave, null);
			return;
		}

		// Update everything that wants to be updated.
		for (Updateable u : updateables) {
			u.update(delta);
		}
	}

	/**
	 * @see org.newdawn.slick.state.GameState#leave(GameContainer,
	 *      StateBasedGame)
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		renderInitializedBeforeEnter = false;
	}

	@Override
	public int getID() {
		return State.GAME.ordinal();
	}

}
