package com.vesalaakso.rbb.model;

import com.vesalaakso.rbb.controller.MapChangeListener;

/**
 * Simple class which has a method to query the current <code>TileMap</code>. It
 * also listens for map changes to update the current map it links to.
 * 
 * @author Vesa Laakso
 */
public class TileMapContainer implements MapChangeListener {

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

	@Override
	public void onMapChange(TileMap oldMap, TileMap newMap) {
		map = newMap;
	}
}
