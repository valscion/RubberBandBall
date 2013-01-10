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

import com.vesalaakso.rbb.controller.CameraController;
import com.vesalaakso.rbb.controller.DebugKeyController;
import com.vesalaakso.rbb.controller.InputMaster;
import com.vesalaakso.rbb.controller.MapChanger;
import com.vesalaakso.rbb.controller.MenuKeyController;
import com.vesalaakso.rbb.controller.RubberBandController;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.Background;
import com.vesalaakso.rbb.model.ParticleManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.RubberBand;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.BackgroundPainter;
import com.vesalaakso.rbb.view.DebugPrintPainter;
import com.vesalaakso.rbb.view.PainterContainer;
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

	/**
	 * The <code>MapChanger</code> responsible for changing the map and
	 * notifying everything that needs to be notified.
	 */
	private MapChanger mapChanger = new MapChanger();

	/**
	 * A <code>PainterContainer</code> which stores everything that can be
	 * painted.
	 */
	private PainterContainer painterContainer = new PainterContainer();

	/**
	 * All the stuff which need to be updated in update()-method are stored
	 * here.
	 */
	private List<Updateable> updateables = new LinkedList<Updateable>();

	/** InputMaster to handle the coordination of various input controllers. */
	private InputMaster inputMaster;

	/** What would a game be without a player? */
	private Player player;

	/** The player is controlled by this rubber band! */
	private RubberBand rubberBand;

	/** Ooh, particles, yes please! */
	private ParticleManager particleManager = new ParticleManager();

	/** Of course we need physics, here it is! */
	private Physics physics;

	/** And some eye candy. */
	private Background background;

	/** If the game should stop at next update()-call, this flag knows. */
	private boolean stopAtNextUpdate;

	/** Is the game in debug mode? */
	private boolean debugMode = true;

	/** Change to this map on the next update() call. */
	private int changeToLevel = -1;

	/** The state which handles map changing routines. */
	private MapChangeState mapChangeState;

	/**
	 * Construct the GameState and tells it which state is responsible for map
	 * changing routines.
	 * 
	 * @param mapChangeState
	 *            the state which handles map changing routines.
	 */
	public GameState(MapChangeState mapChangeState) {
		this.mapChangeState = mapChangeState;
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new BackgroundPainter(background));
		painterContainer.addPainter(new TileMapAreaPainter(mapContainer));
		painterContainer.addPainter(new TileMapBackLayerPainter(mapContainer));
		painterContainer.addPainter(new PlayerPainter(player));
		painterContainer.addPainter(new TileMapOverLayerPainter(mapContainer));
		painterContainer.addPainter(new RubberBandPainter(rubberBand));
		painterContainer.addDebugPainter(new PhysicsPainter(physics));
		painterContainer
				.addDebugPainter(new DebugPrintPainter(physics, player));
	}

	/** A helper method which adds all the controllers to the game. */
	private void addControllers(Input input) {
		inputMaster = new InputMaster(input);
		inputMaster.addKeyListener(new CameraController(player));
		inputMaster.addKeyListener(new MenuKeyController(this));
		inputMaster.addMouseListener(new RubberBandController(rubberBand));
		inputMaster.addKeyListener(new DebugKeyController(this, player));
	}

	/**
	 * A helper method for hooking everything that should be hooked to map
	 * changes.
	 */
	private void addMapChangerHooks() {
		// The first hook must be for the mapContainer!
		mapChanger.addListener(mapContainer);

		mapChanger.addListener(player);
		mapChanger.addListener(rubberBand);
		mapChanger.addListener(physics);
		mapChanger.addListener(particleManager);
	}

	/** A helper method which adds all updateables. */
	private void addUpdateables() {
		updateables.add(inputMaster);
		updateables.add(physics);
		updateables.add(background);
		updateables.add(particleManager);
	}

	/**
	 * Makes the game quit on the next update-loop.
	 */
	public void stop() {
		stopAtNextUpdate = true;
	}

	/**
	 * Changes the level on the next update() call to the one given.
	 * 
	 * @param level
	 *            the level index to change to.
	 */
	public void changeLevel(int level) {
		this.changeToLevel = level;
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
		player = new Player();

		// Physics world, too
		physics = new Physics(player, particleManager);

		// Add the rubber band to the game
		rubberBand = new RubberBand(player, physics);

		// Add the painters next
		addPainters();

		// And then the controllers
		addControllers(container.getInput());

		// When map changes, something needs to be modified. Set it so.
		addMapChangerHooks();

		// Now create the first map and use the hooks we just initialized to
		// initialize the game.
		try {
			mapChanger.changeMap(null, new TileMap(3));
			mapChanger.runChange();
		}
		catch (MapException e) {
			// So we couldn't load even the first map... that's sad.
			Log.error("Failed to change to the first map.");
			throw new SlickException("Failed to change to the first map.", e);
		}

		// Finally add all objects which hook to the update()-method.
		addUpdateables();
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
		painterContainer.paintAll(g, this);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		// The game was not meant to last forever, my friend.
		if (stopAtNextUpdate) {
			Transition leave = new FadeOutTransition();
			Transition enter = new FadeInTransition();
			stopAtNextUpdate = false;
			game.enterState(State.MAIN_MENU.ordinal(), leave, enter);
		}
		if (changeToLevel > 0) {
			Transition leave = new FadeOutTransition();
			try {
				TileMap newLevel = new TileMap(changeToLevel);
				mapChanger.changeMap(mapContainer.getMap(), newLevel);
				mapChangeState.setupChange(mapChanger, this);
				game.enterState(mapChangeState.getID(), leave, null);
			}
			catch (MapException e) {
				Log.error("Failed to change the map.");
				throw new SlickException("Failed to change the map.", e);
			}
			changeToLevel = -1;
		}

		// Update everything that wants to be updated.
		for (Updateable u : updateables) {
			u.update(delta);
		}
	}

	/**
	 * @see org.newdawn.slick.state.GameState#enter(GameContainer,
	 *      StateBasedGame)
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("entered, time " + System.currentTimeMillis());
	}

	/**
	 * @see org.newdawn.slick.state.GameState#leave(GameContainer,
	 *      StateBasedGame)
	 */
	@Override
	public void leave(GameContainer container, StateBasedGame game)
			throws SlickException {
		System.out.println("left, time " + System.currentTimeMillis());
	}

	@Override
	public int getID() {
		return State.GAME.ordinal();
	}

}
