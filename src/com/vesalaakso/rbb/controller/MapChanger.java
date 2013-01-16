package com.vesalaakso.rbb.controller;

import java.util.Map;

import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;
import com.vesalaakso.rbb.model.GameStatus;
import com.vesalaakso.rbb.model.ResourceManager;
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

	/** A resource manager to consult for maps */
	private ResourceManager resourceManager;

	/** The map container to update */
	private final TileMapContainer mapContainer;

	/** The game status to update when map is changed */
	private final GameStatus gameStatus;

	/**
	 * Initializes the MapChanger and associates it with the given
	 * TileMapContainer, which in turn gets reset when map is changed. Also the
	 * game status needs to be updated when map is changed so associate this
	 * class with the current <code>GameStatus</code>, too.
	 * 
	 * @param mapContainer
	 *            the <code>TileMapContainer</code> to change map from
	 * @param gameStatus
	 *            the <code>GameStatus</code> to update when map is changed
	 * @param resourceManager
	 *            the <code>ResourceManager</code> that has all the current
	 *            levels loaded
	 */
	public MapChanger(TileMapContainer mapContainer, GameStatus gameStatus,
			ResourceManager resourceManager) {
		this.mapContainer = mapContainer;
		this.gameStatus = gameStatus;
		this.resourceManager = resourceManager;
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
			this.newMap = new TileMap(newMap, resourceManager);
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

		// Tell the game status that we're changing the map.
		gameStatus.onMapChange(mapContainer.getMap(), newMap);

		// Updating map container map updates the map for all.
		mapContainer.setMap(newMap);
	}

}
