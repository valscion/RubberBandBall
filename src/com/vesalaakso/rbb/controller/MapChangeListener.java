package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.TileMap;

/**
 * Any class which wants to react to map changing should implement this
 * interface.
 * 
 * @author Vesa Laakso
 */
public interface MapChangeListener {

	/**
	 * Called when a map changes.
	 * 
	 * @param oldMap
	 *            the map that just finished. Will be <code>null</code> if there
	 *            was no map being played before.
	 * @param newMap
	 *            the new map which will follow the old map
	 */
	public void onMapChange(TileMap oldMap, TileMap newMap);

}
