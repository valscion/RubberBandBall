package com.vesalaakso.rbb.model.resources;

import java.io.InputStream;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

import com.google.common.collect.Maps;

/**
 * Handles graphics loading and saving.
 * 
 * @author Vesa Laakso
 */
public class GfxContainer {

	/** All graphics files are stored in this map */
	private Map<Gfx, Image> images = Maps.newHashMap();

	/**
	 * Initializes all the graphics files.
	 * 
	 * @throws SlickException
	 *             if something went wrong with image loading
	 */
	public void init() throws SlickException {
		// Gotta load 'em all
		images.put(Gfx.BACKGROUND_GAME, loadImage(Gfx.BACKGROUND_GAME));
		images.put(Gfx.MAP_GRAV_ARROW, loadImage(Gfx.MAP_GRAV_ARROW));
	}

	/** A helper to load an image file */
	private Image loadImage(Gfx gfx) throws SlickException {
		Image img;
		String resourcePath = "/com/vesalaakso/rbb/data/" + gfx.fileName;
		InputStream in = this.getClass().getResourceAsStream(resourcePath);

		if (in == null) {
			String err =
				String.format("Could not find image %s with file name %s", gfx,
						gfx.fileName);
			throw new SlickException(err);
		}

		img = new Image(in, gfx.fileName, false);
		return img;
	}
	
	/**
	 * Getter for an image
	 * 
	 * @param gfx
	 *            the graphics to get
	 * @return the graphics one wanted
	 */
	public Image getImage(Gfx gfx) {
		Image img = images.get(gfx);
		return img;
	}
}
