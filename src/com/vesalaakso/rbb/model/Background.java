package com.vesalaakso.rbb.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.SlickException;

import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.controller.Updateable;

/**
 * Represents the background image of the game.
 * 
 * @author Vesa Laakso
 */
public class Background implements Updateable, Resetable {

	/** The image to be drawn. */
	private Image bgImage;

	/** X-coordinate for the image. */
	private float xPos;

	/** Y-coordinate for the image. */
	private float yPos;

	/**
	 * Constructs this Background and loads the background image
	 */
	public Background() {
		try {
			bgImage = new Image("data/spacebg.png");
		}
		catch (SlickException e) {
			Log.warn("Couldn't load background image", e);
		}
	}

	/**
	 * Updates the background position nicely, on every update tick.
	 * 
	 * @see com.vesalaakso.rbb.controller.Updateable#update(int)
	 */
	@Override
	public void update(int delta) {
		if (bgImage == null) {
			// Loading of the image has probably failed.
			return;
		}

		Camera cam = Camera.get();
		xPos = cam.getX() * -0.1f;
		yPos = cam.getY() * -0.1f;
	}

	/**
	 * Returns the background image.
	 * 
	 * @return the image to be drawn
	 */
	public Image getImage() {
		return bgImage;
	}

	/**
	 * Returns the background x-position.
	 * 
	 * @return background x-position in screen coordinates
	 */
	public float getX() {
		return xPos;
	}

	/**
	 * Returns the background x-position.
	 * 
	 * @return background x-position in screen coordinates
	 */
	public float getY() {
		return yPos;
	}

	@Override
	public void reset() {
		update(0);
	}

}
