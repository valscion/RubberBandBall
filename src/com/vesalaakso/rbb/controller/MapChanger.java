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

	/** If map is changing, this is the old map to change from. */
	private TileMap oldMap;

	/** If map is changing, this is the new map to change to. */
	private TileMap newMap;

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
	 * The big red button which starts the map changing routine.
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
		this.oldMap = oldMap;
		this.newMap = newMap;
	}

	/**
	 * Once this method finishes, the game is ready to be entered to the new
	 * level.
	 * 
	 * @throws MapException if the change could not be made.
	 */
	public void runChange() throws MapException {
		if (!newMap.isInitialized()) {
			newMap.init();
		}

		for (MapChangeListener listener : listeners) {
			listener.onMapChange(oldMap, newMap);
		}
	}

}
