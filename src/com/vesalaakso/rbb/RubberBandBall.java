package com.vesalaakso.rbb;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.controller.CameraController;
import com.vesalaakso.rbb.controller.InputMaster;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.PainterContainer;
import com.vesalaakso.rbb.view.PlayerPainter;
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

	/** InputMaster to handle the coordination of various input controllers. */
	private InputMaster inputMaster;

	/** A camera which controls the area of the world that is drawn. */
	private Camera camera = new Camera(0, 0);

	/** What would a game be without a player? */
	private Player player = new Player(this);

	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new TileMapBackLayerPainter(map));
		painterContainer.addPainter(new PlayerPainter(player));
		painterContainer.addPainter(new TileMapOverLayerPainter(map));
	}

	/** A helper method which adds all the controllers to the game. */
	private void addControllers(Input input) {
		inputMaster = new InputMaster(input);
		inputMaster.addController(new CameraController(camera));
	}

	/**
	 * Gets the current map.
	 * 
	 * @return the current <code>TileMap</code>
	 */
	public TileMap getMap() {
		return map;
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Remove the default input handlers, as input handling is done via
		// InputMaster class and not this.
		container.getInput().removeAllListeners();

		// Try to load the map
		try {
			map = new TileMap(3);
			map.init();
		}
		catch (MapException e) {
			Log.error(e.getMessage(), e);
			container.exit();
			return;
		}

		// Reset player position
		player.resetPosition();

		// Add the painters next
		addPainters();

		// And then the controllers
		addControllers(container.getInput());
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// Update the controllers.
		inputMaster.updateControllers(delta);
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		painterContainer.paintAll(g, camera);
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
