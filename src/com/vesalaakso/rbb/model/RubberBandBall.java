package com.vesalaakso.rbb.model;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.exceptions.MapException;
import com.vesalaakso.rbb.view.PainterContainer;
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

	/** A camera. TODO: What is a camera? */
	private Camera camera = new Camera(0, 0);

	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}

	/** A helper method which adds all the painters in the correct order. */
	private void addPainters() {
		painterContainer.addPainter(new TileMapBackLayerPainter(map));
		painterContainer.addPainter(new TileMapOverLayerPainter(map));
	}

	@Override
	public void init(GameContainer container) throws SlickException {
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

		// Add the painters next
		addPainters();
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO Auto-generated method stub

	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		painterContainer.paintAll(g, camera);
	}

	/**
	 * Starting point for the game.
	 * 
	 * @param args Command line parameters
	 * */
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new RubberBandBall());

			app.setDisplayMode(800, 600, false);
			//app.setTargetFrameRate(60);
			app.setVSync(true);
			app.start();
		}
		catch (SlickException e) {
			// So we didn't even manage to start the freaking game now, did we.
			e.printStackTrace();
		}
	}

}
