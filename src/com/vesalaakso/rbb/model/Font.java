package com.vesalaakso.rbb.model;

/**
 * An enumeration to map all fonts to a constant
 * 
 * @author Vesa Laakso
 */
public enum Font {
	/** Menu items */
	MENU_ITEM("Rokkitt.ttf"),
	/** Regular text */
	REGULAR("neuropol_x_free.ttf"),
	/** Scores in game */
	SCORE("LondonMM.ttf");

	/** The font file name behind the enum */
	public final String fileName;

	/**
	 * Constructs the enum and associates it with the given font file name.
	 * 
	 * @param fileName
	 *            the font file name to associate with the enum
	 */
	Font(String fileName) {
		this.fileName = fileName;
	}
}
