package com.vesalaakso.rbb.model;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.GroupObject;
import org.newdawn.slick.tiled.ObjectGroup;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Represents a tile map. Drawing of the tile map is located in
 * {@link TileMapPainter} class.
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

	/**
	 * Index of the meta layer that contains information about special tiles.
	 * This layer is not drawn.
	 */
	private int metaLayer;

	/** Spawn area. */
	private TileMapObject spawnArea;

	/** Safe areas */
	private List<TileMapObject> safeAreas = new ArrayList<TileMapObject>();

	/** Trigger areas. */
	private List<TileMapObject> triggerAreas = new ArrayList<TileMapObject>();

	/** Finish area. */
	private TileMapObject finishArea;
	
	/** Collision objects in the map. */
	private List<TileMapObject> collisionObjects = new ArrayList<TileMapObject>();

	/**
	 * Constructs a new TileMap. You need to call {@link #init}-method before
	 * actually using the tile map however.
	 * 
	 * @param level
	 *            the level this map represents
	 */
	public TileMap(int level) {
		this.level = level;
	}

	/**
	 * Initializes and loads the level.
	 * 
	 * @throws MapException
	 *             if a mapfile was not found for this level
	 */
	public void init() throws MapException {
		String path = findMapPath(level);
		try {
			map = new TiledMapPlus(path);
		}
		catch (SlickException e) {
			throw new MapException("Failed to load map " + level, e);
		}
		catch (RuntimeException e) {
			throw new MapException("Failed to load map " + level, e);
		}

		// Find the layer indexes
		backLayer = map.getLayerIndex("back");
		overLayer = map.getLayerIndex("over");
		metaLayer = map.getLayerIndex("meta");

		// Validate that layer indexes were actually found.
		if (backLayer == -1 || overLayer == -1 || metaLayer == -1) {
			throw new MapException("Invalid map layers for level " + level);
		}

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
		
		// Print all of the objects in all groups.
		for (ObjectGroup tmpGroup : objectGroups) {
			Log.info("Map has area with name \"" + tmpGroup.name + "\"");
			for (GroupObject area : tmpGroup.getObjects()) {
				try {
					TileMapObject tmp = new TileMapObject(area);
					System.out.println(tmp);
				}
				catch (MapException e) {
					System.out.println(groupObjectToString(area));
				}
			}
		}

		// Store start area
		for (GroupObject area : areaGroup.getObjectsOfType("spawn")) {
			if (spawnArea != null) {
				throw new MapException("More than one spawn area set in level "
						+ level);
			}
			spawnArea = new TileMapObject(area);
		}
		if (spawnArea == null) {
			throw new MapException("Level " + level + " had no spawn area!");
		}
		
		// Store finish area
		for (GroupObject area : areaGroup.getObjectsOfType("finish")) {
			if (finishArea != null) {
				throw new MapException("More than one finish area set in level "
						+ level);
			}
			finishArea = new TileMapObject(area);
		}
		if (finishArea == null) {
			throw new MapException("Level " + level + " had no finish area!");
		}
		
		// Store trigger areas
		for (GroupObject area : areaGroup.getObjectsOfType("trigger")) {
			triggerAreas.add(new TileMapObject(area));
		}

		// Store collision objects
		for (GroupObject area : areaGroup.getObjectsOfType("trigger")) {
			triggerAreas.add(new TileMapObject(area));
		}
	}

	/**
	 * A helper method which returns a string representation of a GroupObject
	 * */
	private String groupObjectToString(GroupObject obj) {
		String str = "GroupObject={"
				+ "index:" + obj.index
				+ ",name:" + obj.name
				+ ",type:" + obj.type
				+ ",objectType:" + obj.getObjectType()
				+ ",x:" + obj.x
				+ ",y:" + obj.y
				+ ",width:" + obj.width
				+ ",height:" + obj.height
				+ ",props:" + obj.props
				+ "}";
		return str;
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
	 * Gets the index of the meta layer.
	 * 
	 * @return index of meta layer
	 */
	public int getIndexOfMetaLayer() {
		return metaLayer;
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
	 * Returns the collidable objects in a list that should NOT be modified.
	 * 
	 * @return collidable objects in a list
	 */
	public List<TileMapObject> getCollisionObjects() {
		return collisionObjects;
	}

	/**
	 * Finds the absolute file path to a level.
	 * 
	 * @param level
	 *            the level to search for
	 * @return an absolute file path to the given level
	 * @throws MapException
	 *             if the file was not found for the given level
	 */
	public static String findMapPath(int level) throws MapException {
		String filename = level + ".tmx";
		if (level >= 0 && level <= 9) {
			// Keep it zero padded if necessary
			filename = "0" + filename;
		}

		// The directory where the maps reside
		String dirname = "data/levels/";

		// aaand combine them.
		String path = dirname + filename;

		return path;
	}
}
