package com.vesalaakso.rbb.model;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Represents a tile map. Drawing of the tile map is located in
 * {@link TileMapPainter} class.
 */
public class TileMap {

	/** Size of a single tile, in pixels. */
	public static final int TILE_SIZE = 32;

	/** The {@link org.newdawn.slick.tiled.TiledMap} this instance represents */
	private TiledMap map;

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
	private TileMapArea spawnArea;

	/** Safe areas */
	private List<TileMapArea> safeAreas = new ArrayList<TileMapArea>();

	/** Trigger areas. */
	private List<TileMapArea> triggerAreas = new ArrayList<TileMapArea>();

	/** Finish area. */
	private TileMapArea finishArea;

	/** Collidable tiles in this map */
	private List<CollidableTile> collidableTiles =
			new ArrayList<CollidableTile>();

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
			map = new TiledMap(path);
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

		// Store everything that is storable in the meta layer, i.e. save the
		// collidable tiles
		saveAllSpecialTiles();
	}

	/** Loops through every object in the map and stores them. */
	private void saveAllObjects() throws MapException {
		for (int i = 0, iMax = map.getObjectGroupCount(); i < iMax; i++) {
			for (int j = 0, jMax = map.getObjectCount(i); j < jMax; j++) {
				TileMapArea area = new TileMapArea(map, i, j);
				
				String typeStr = area.type.toUpperCase();
				try {
					TileMapAreaType areaType = TileMapAreaType.valueOf(typeStr);
					switch (areaType) {
						case SPAWN:
							if (spawnArea != null) {
								throw new MapException("Level " + level
										+ " has more than one spawn area");
							}
							spawnArea = area;
							break;
						case SAFE:
							safeAreas.add(area);
							break;
						case TRIGGER:
							triggerAreas.add(area);
							break;
						case FINISH:
							if (finishArea != null) {
								throw new MapException("Level " + level
										+ " has more than one finish area");
							}
							finishArea = area;
							break;
						case RESPONSIVE:
							// TODO: Add responsive areas
							break;
						case TRANSITION:
							// TODO: Add transition areas
							break;
						default:
							break;
					}
				}
				catch (IllegalArgumentException e) {
					throw new MapException(
							"Invalid object type in level " + level, e);
				}
			}
		}
	}

	/**
	 * Loops through every special tile in the meta layer and saves them to the
	 * appropriate attributes. Right now the only thing saved is the collision
	 * tiles.
	 */
	private void saveAllSpecialTiles() throws MapException {
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int tileID = map.getTileId(x, y, metaLayer);
				String value =
						map.getTileProperty(tileID, "collision", "false");
				if (! value.equals("false")) {
					try {
						CollidableTile tile = new CollidableTile(x, y, this);
						collidableTiles.add(tile);
					}
					catch (MapException e) {
						throw new MapException("Invalid special tile in level "
								+ level, e);
					}
				}
			}
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
	public TileMapArea getSpawnArea() {
		return spawnArea;
	}

	/**
	 * Returns the safe areas.
	 * 
	 * @return safe areas
	 */
	public List<TileMapArea> getSafeAreas() {
		return safeAreas;
	}

	/**
	 * Returns the trigger areas.
	 * 
	 * @return trigger areas
	 */

	public List<TileMapArea> getTriggerAreas() {
		return triggerAreas;
	}

	/**
	 * Returns the finish area.
	 * 
	 * @return finish area
	 */
	public TileMapArea getFinishArea() {
		return finishArea;
	}

	/**
	 * Returns the collidable tiles in a list that should NOT be modified.
	 * 
	 * @return collidable tiles in a list
	 */
	public List<CollidableTile> getCollidableTiles() {
		return collidableTiles;
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
