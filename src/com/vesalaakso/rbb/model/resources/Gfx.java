package com.vesalaakso.rbb.model.resources;

/**
 * All graphics files mapped to a sane enum
 * 
 * @author Vesa Laakso
 */
public enum Gfx {
	/** Menu background */
	BACKGROUND_MENU("main-menu_bg.png"),
	/** Game background */
	BACKGROUND_GAME("spacebg.png"),
	/** Game play information image */
	GAMEPLAY("gameplay.png"),
	/** A gravitation arrow for maps */
	MAP_GRAV_ARROW("grav-arrow.png");

	/** The string for the file name of graphics file */
	String fileName;

	/**
	 * Constructs the enum and associates it with the given file name.
	 * 
	 * @param fileName
	 *            the file name to associate with the enum
	 */
	Gfx(String fileName) {
		this.fileName = fileName;
	}
}
