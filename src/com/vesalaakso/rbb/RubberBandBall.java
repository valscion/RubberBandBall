package com.vesalaakso.rbb;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.controller.CameraController;
import com.vesalaakso.rbb.controller.InputMaster;
import com.vesalaakso.rbb.controller.MapChanger;
import com.vesalaakso.rbb.controller.RubberBandController;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.ParticleManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.RubberBand;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.PainterContainer;
import com.vesalaakso.rbb.view.ParticleSystemPainter;
import com.vesalaakso.rbb.view.PhysicsPainter;
import com.vesalaakso.rbb.view.PlayerPainter;
import com.vesalaakso.rbb.view.RubberBandPainter;
import com.vesalaakso.rbb.view.TileMapAreaPainter;
import com.vesalaakso.rbb.view.TileMapBackLayerPainter;
import com.vesalaakso.rbb.view.TileMapOverLayerPainter;

/**
 * The game. You lost it.
 */
public class RubberBandBall extends BasicGame {

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

	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new TileMapAreaPainter(mapContainer));
		painterContainer.addPainter(new TileMapBackLayerPainter(mapContainer));
		painterContainer.addPainter(new PlayerPainter(player));
		painterContainer.addPainter(new TileMapOverLayerPainter(mapContainer));
		painterContainer.addPainter(new RubberBandPainter(rubberBand));
		painterContainer.addPainter(new ParticleSystemPainter(particleManager));
		painterContainer.addPainter(new PhysicsPainter(physics));
	}

	/** A helper method which adds all the controllers to the game. */
	private void addControllers(Input input) {
		inputMaster = new InputMaster(input);
		inputMaster.addKeyListener(new CameraController(player));
		inputMaster.addMouseListener(new RubberBandController(rubberBand));
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
		updateables.add(particleManager);
	}

	/**
	 * From slick: Initialise the game. This can be used to load static
	 * resources. It's called before the game loop starts
	 * 
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		// First things first. Initialize the camera properly.
		Camera.init(container.getWidth(), container.getHeight());

		// Remove the default input handlers, as input handling is done via
		// InputMaster class and not this.
		container.getInput().removeAllListeners();

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
		}
		catch (MapException e) {
			// So we couldn't load even the first map... that's sad.
			Log.error("Failed to change to the first map.");
			throw new SlickException("Failed to change to the first map.", e);
		}

		// Set the camera at first to players position
		Camera.get().setPosition(player.getX(), player.getY());

		// Finally add all objects which hook to the update()-method.
		addUpdateables();
	}

	/**
	 * From slick: Update the game logic here. No rendering should take place in
	 * this method though it won't do any harm.
	 * 
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer,
	 *      int)
	 */
	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// Update everything that wants to be updated.
		for (Updateable u : updateables) {
			u.update(delta);
		}
	}

	/**
	 * From slick: Render the game's screen here.
	 * 
	 * @param g
	 *            The graphics context that can be used to render. However,
	 *            normal rendering routines can also be used.
	 * 
	 * @see org.newdawn.slick.Game#render(org.newdawn.slick.GameContainer,
	 *      org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		painterContainer.paintAll(g);
	}

	/**
	 * Starting point for the game.
	 * 
	 * @param args
	 *            Command line parameters
	 */
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new RubberBandBall());

			app.setDisplayMode(800, 600, false);
			// app.setTargetFrameRate(60);
			app.setVSync(true);
			app.start();
		}
		catch (SlickException e) {
			// So we didn't even manage to start the freaking game now, did we.
			e.printStackTrace();
		}
	}

}
