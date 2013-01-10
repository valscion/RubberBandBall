package com.vesalaakso.rbb.model;

/**
 * Simple class which has a method to query the current <code>TileMap</code>.
 * 
 * @author Vesa Laakso
 */
public class TileMapContainer {

	/** The current <code>TileMap</code>. */
	private TileMap map;

	/**
	 * Gets the current map.
	 * 
	 * @return current map
	 */
	public TileMap getMap() {
		return map;
	}

	/**
	 * Sets the current map to the map given. Should only be called when the map
	 * changes!
	 * 
	 * @param map the new map
	 */
	public void setMap(TileMap map) {
		this.map = map;
	}
}
