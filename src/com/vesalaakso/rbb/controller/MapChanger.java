package com.vesalaakso.rbb.controller;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;
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

	/** Already initialized maps mapped to their index */
	private Map<Integer, TileMap> initializedMapsMap = Maps.newHashMap();

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
	 * @param newMap
	 *            the map level to change to, can be uninitialized.
	 */
	public void changeMap(int newMap) {
		Log.info("Changing map!");
		
		// Old map is the last new map
		this.oldMap = this.newMap;
		
		// Get the new map
		if (initializedMapsMap.containsKey(newMap)) {
			this.newMap = initializedMapsMap.get(newMap);
		}
		else {
			this.newMap = new TileMap(newMap);
		}
	}

	/**
	 * Once this method finishes, the game is ready to be entered to the new
	 * level.
	 * 
	 * @throws MapException
	 *             if the change could not be made.
	 */
	public void runChange() throws MapException {
		if (!newMap.isInitialized()) {
			newMap.init();
			initializedMapsMap.put(newMap.getLevel(), newMap);
		}

		for (MapChangeListener listener : listeners) {
			listener.onMapChange(oldMap, newMap);
		}
	}

}
