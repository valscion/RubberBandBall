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
import com.vesalaakso.rbb.controller.RubberBandController;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.ParticleManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.RubberBand;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.PainterContainer;
import com.vesalaakso.rbb.view.ParticleSystemPainter;
import com.vesalaakso.rbb.view.PhysicsPainter;
import com.vesalaakso.rbb.view.PlayerPainter;
import com.vesalaakso.rbb.view.RubberBandPainter;
import com.vesalaakso.rbb.view.TileMapBackLayerPainter;
import com.vesalaakso.rbb.view.TileMapOverLayerPainter;

/**
 * The game. You lost it.
 */
public class RubberBandBall extends BasicGame {

	/** The current tile map */
	private TileMap map;

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
	private Physics physics = new Physics(particleManager);

	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new TileMapBackLayerPainter(map));
		painterContainer.addPainter(new PlayerPainter(player));
		painterContainer.addPainter(new TileMapOverLayerPainter(map));
		painterContainer.addPainter(new RubberBandPainter(rubberBand));
		painterContainer.addPainter(new ParticleSystemPainter(particleManager));
		painterContainer.addPainter(new PhysicsPainter(physics));
	}

	/** A helper method which adds all the controllers to the game. */
	private void addControllers(Input input) {
		inputMaster = new InputMaster(input);
		inputMaster.addKeyListener(new CameraController());
		inputMaster.addMouseListener(new RubberBandController(rubberBand));
	}

	/** A helper method which adds all updateables. */
	private void addUpdateables() {
		updateables.add(inputMaster);
		updateables.add(physics);
		updateables.add(particleManager);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Remove the default input handlers, as input handling is done via
		// InputMaster class and not this.
		container.getInput().removeAllListeners();

		// Try to load the map and everything related to it.
		try {
			map = new TileMap(3);
			map.init();
			physics.addCollidablesFromMap(map);
		}
		catch (MapException e) {
			Log.error(e.getMessage(), e);
			container.exit();
			return;
		}

		// Make the player
		player = new Player(map);
		player.reset();

		// Add player to physics
		physics.addPlayer(player);

		// Add the rubber band to the game
		rubberBand = new RubberBand(player, physics);

		// Add the painters next
		addPainters();

		// And then the controllers
		addControllers(container.getInput());

		// Finally add all objects which hook to the update()-method.
		addUpdateables();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// Update everything that wants to be updated.
		for (Updateable u : updateables) {
			u.update(delta);
		}
	}

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
