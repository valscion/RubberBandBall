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

	/**
	 * A helper method which tells if the current shape is triangular.
	 * 
	 * @return <code>true</code>, if the current shape was triangular,
	 *         <code>false</code> otherwise
	 */
	public boolean isTriangular() {
		if (this != RECTANGLE) {
			return true;
		}
		return false;
	}

}
