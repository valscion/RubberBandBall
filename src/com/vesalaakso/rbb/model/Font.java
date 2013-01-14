package com.vesalaakso.rbb.model;

/**
 * An enumeration to map all fonts to a constant
 * 
 * @author Vesa Laakso
 */
public enum Font {
	/** Menu items */
	MENU_ITEM("Rokkitt.ttf", 40),
	/** Menu items, hilighted */
	MENU_ITEM_HILIGHTED("Rokkitt.ttf", 40),
	/** Regular text */
	REGULAR("neuropol_x_free.ttf", 12),
	/** Scores in game */
	SCORE("LondonMM.ttf", 15);

	/** The font file name behind the enum */
	public final String fileName;

	/** The font size */
	public final int size;

	/**
	 * Constructs the enum and associates it with the given font file name and
	 * size.
	 * 
	 * @param fileName
	 *            the font file name to associate with the enum
	 * @param size
	 *            the size of the font
	 */
	Font(String fileName, int size) {
		this.fileName = fileName;
		this.size = size;
	}
}
