package com.vesalaakso.rbb.controller;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Handles everything that needs to be updated when the map changes.
 * 
 * @author Vesa Laakso
 */
public class MapChanger {

	/** Everything that wants to be notified of map changes. */
	private List<MapChangeListener> listeners =
		new LinkedList<MapChangeListener>();

	/**
	 * Initializes the MapChanger. Remember to add listeners with the
	 * {@link #addListener} method or this instance is pretty much useless.
	 * 
	 */
	public MapChanger() {
	}

	/**
	 * Adds a listener which listens for map changes.
	 * 
	 * @param listener
	 *            a <code>MapChangeListener</code> to be notified when the map
	 *            is changed.
	 */
	public void addListener(MapChangeListener listener) {
		listeners.add(listener);
	}

	/**
	 * The big red button which changes the level.
	 * 
	 * @param oldMap
	 *            the map to change from
	 * @param newMap
	 *            the map to change to, can be uninitialized.
	 * @throws MapException
	 *             if the new map could not be initialized
	 */
	public void changeMap(TileMap oldMap, TileMap newMap) throws MapException {
		Log.info("Changing map!");
		if (!newMap.isInitialized()) {
			newMap.init();
		}

		for (MapChangeListener listener : listeners) {
			listener.onMapChange(oldMap, newMap);
		}
	}

}
