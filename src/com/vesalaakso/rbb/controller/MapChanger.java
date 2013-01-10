package com.vesalaakso.rbb.controller;

import java.util.Map;

import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Handles everything that needs to be updated when the map changes.
 * 
 * @author Vesa Laakso
 */
public class MapChanger {

	/** If map is changing, this is the new map to change to. */
	private TileMap newMap;

	/** Already initialized maps mapped to their index */
	private Map<Integer, TileMap> initializedMapsMap = Maps.newHashMap();

	/** The map container to update */
	private TileMapContainer mapContainer;

	/**
	 * Initializes the MapChanger and associates it with the given
	 * TileMapContainer, which in turn gets reset when map is changed.
	 * 
	 * @param mapContainer
	 *            the <code>TileMapContainer</code> to change map from
	 */
	public MapChanger(TileMapContainer mapContainer) {
		this.mapContainer = mapContainer;
	}

	/**
	 * The big red button which starts the map changing routine.
	 * 
	 * @param newMap
	 *            the map level to change to, can be uninitialized.
	 */
	public void changeMap(int newMap) {
		Log.info("Changing map!");

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

		mapContainer.setMap(newMap);
	}

}
