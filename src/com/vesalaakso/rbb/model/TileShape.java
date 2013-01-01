package com.vesalaakso.rbb.model;

/**
 * Each tile has a basic shape and this enum is used to represent it.
 * 
 * @author Vesa Laakso
 */
public enum TileShape {

	/** A rectangular shape */
	RECTANGLE,
	/** A triangle with the long sides at top and left */
	TRIANGLE_TOP_LEFT,
	/** A triangle with the long sides at bottom and left */
	TRIANGLE_BOTTOM_LEFT,
	/** A triangle with the long sides at bottom and right */
	TRIANGLE_BOTTOM_RIGHT,
	/** A triangle with the long sides at top and right */
	TRIANGLE_TOP_RIGHT;

}
