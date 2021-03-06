package com.vesalaakso.rbb.model;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.tiled.GroupObject;
import org.newdawn.slick.tiled.ObjectGroup;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Represents a tile map. Drawing of the tile map is located in
 * {@link com.vesalaakso.rbb.view.TileMapPainter} class.
 */
public class TileMap {

	/** Size of a single tile, in pixels. */
	public static final int TILE_SIZE = 32;

	/** Is the map initialized yet */
	private boolean isInitialized = false;

	/** The {@link org.newdawn.slick.tiled.TiledMap} this instance represents */
	private TiledMapPlus map;

	/** The current level this map represents */
	private int level;

	/** Index of the back layer that is drawn first */
	private int backLayer;

	/** Index of the over layer that is drawn last */
	private int overLayer;

	/** Spawn area. */
	private TileMapObject spawnArea;

	/** Safe areas */
	private List<TileMapObject> safeAreas = new ArrayList<TileMapObject>();

	/** Trigger areas. */
	private List<TileMapObject> triggerAreas = new ArrayList<TileMapObject>();

	/** Finish area. */
	private TileMapObject finishArea;

	/** Gravity field areas */
	private List<GravityArea> gravityAreas = new ArrayList<GravityArea>();

	/** Collision objects in the map. */
	private List<TileMapObject> collisionObjects =
			new ArrayList<TileMapObject>();

	/** The resource manager to consult for tiled maps */
	private ResourceManager resourceManager;

	/**
	 * Constructs a new TileMap. You need to call {@link #init}-method before
	 * actually using the tile map however.
	 * 
	 * @param level
	 *            the level this map represents
	 * @param resourceManager
	 *            the resource manager to consult for the tile map resource
	 */
	public TileMap(int level, ResourceManager resourceManager) {
		this.level = level;
		this.resourceManager = resourceManager;
	}

	/**
	 * Initializes and loads the level.
	 * 
	 * @throws MapException
	 *             if the level contained malformed data
	 */
	public void init() throws MapException {
		// Store the tiled map for this level
		map = resourceManager.getMap(level);

		// Loop through every object and store them
		saveAllObjects();

		// The map is initialized.
		isInitialized = true;
	}

	/**
	 * Returns the flag indicating whether the map is initialized or not.
	 * 
	 * @return <code>true</code> if the {@link #init()}-method has been called
	 *         successfully.
	 */
	public boolean isInitialized() {
		return isInitialized;
	}

	/** Loops through every object in the map and stores them. */
	private void saveAllObjects() throws MapException {
		// Loop through object areas
		List<ObjectGroup> objectGroups = map.getObjectGroups();
		ObjectGroup areaGroup = null;
		for (ObjectGroup tmpGroup : objectGroups) {
			if (tmpGroup.name.equals("areas")) {
				Log.info("Found areas object group");
				areaGroup = tmpGroup;
			}
		}

		// Store start area
		for (GroupObject area : areaGroup.getObjectsOfType("spawn")) {
			if (spawnArea != null) {
				throw new MapException("More than one spawn area set in level "
						+ level);
			}
			spawnArea = new TileMapObject(area, this);
		}
		if (spawnArea == null) {
			throw new MapException("Level " + level + " had no spawn area!");
		}

		// Store finish area
		for (GroupObject area : areaGroup.getObjectsOfType("finish")) {
			if (finishArea != null) {
				throw new MapException(
						"More than one finish area set in level " + level);
			}
			finishArea = new TileMapObject(area, this);
		}
		if (finishArea == null) {
			throw new MapException("Level " + level + " had no finish area!");
		}

		// Store trigger areas
		for (GroupObject area : areaGroup.getObjectsOfType("trigger")) {
			triggerAreas.add(new TileMapObject(area, this));
		}

		// Store safe areas
		for (GroupObject area : areaGroup.getObjectsOfType("safe")) {
			safeAreas.add(new TileMapObject(area, this));
		}

		// Store gravity fields
		for (GroupObject area : areaGroup.getObjectsOfType("gravity")) {
			gravityAreas.add(new GravityArea(area, this));
		}

		// Store collision objects
		for (GroupObject area : map.getObjectGroup("collisions").getObjects()) {
			TileMapObject newObj = new TileMapObject(area, this);
			collisionObjects.add(newObj);

			newObj.findObjectAbove();
		}
	}

	/**
	 * Gets the map width in tiles.
	 * 
	 * @return map width in tiles
	 */
	public int getWidthInTiles() {
		return map.getWidth();
	}

	/**
	 * Gets the map height in tiles.
	 * 
	 * @return map height in tiles
	 */
	public int getHeightInTiles() {
		return map.getHeight();
	}

	/**
	 * Gets the reference to the current Tiled tile map. Do not modify the map
	 * outside of this class, please!
	 * 
	 * @return the current {@link org.newdawn.slick.tiled.TiledMap} as a
	 *         reference.
	 */
	public TiledMap getTiledMap() {
		return map;
	}

	/**
	 * Gets the index of the back layer.
	 * 
	 * @return index of back layer
	 */
	public int getIndexOfBackLayer() {
		return backLayer;
	}

	/**
	 * Gets the index of the over layer.
	 * 
	 * @return index of over layer
	 */
	public int getIndexOfOverLayer() {
		return overLayer;
	}

	/**
	 * Returns the spawn area.
	 * 
	 * @return spawn area
	 */
	public TileMapObject getSpawnArea() {
		return spawnArea;
	}

	/**
	 * Returns the safe areas.
	 * 
	 * @return safe areas
	 */
	public List<TileMapObject> getSafeAreas() {
		return safeAreas;
	}

	/**
	 * Returns the trigger areas.
	 * 
	 * @return trigger areas
	 */
	public List<TileMapObject> getTriggerAreas() {
		return triggerAreas;
	}

	/**
	 * Returns the finish area.
	 * 
	 * @return finish area
	 */
	public TileMapObject getFinishArea() {
		return finishArea;
	}

	/**
	 * Returns the gravity fields.
	 * 
	 * @return gravity fields
	 */
	public List<GravityArea> getGravityAreas() {
		return gravityAreas;
	}

	/**
	 * Returns the collidable objects in a list that should NOT be modified.
	 * 
	 * @return collidable objects in a list
	 */
	public List<TileMapObject> getCollisionObjects() {
		return collisionObjects;
	}

	/**
	 * Get the level this map is.
	 * 
	 * @return the level number of this map
	 */
	public int getLevel() {
		return level;
	}
}
