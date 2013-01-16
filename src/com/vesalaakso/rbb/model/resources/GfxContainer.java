package com.vesalaakso.rbb.model.resources;

import java.util.EnumMap;

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
	private EnumMap<Gfx, Image> images = Maps.newEnumMap(Gfx.class);

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
		img = new Image(gfx.fileName);
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
