package com.vesalaakso.rbb.game;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMap;

import com.vesalaakso.rbb.game.exceptions.MapException;

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
			this.map = new TiledMap(path);
		}
		catch (SlickException e) {
			throw new MapException("Failed to load map " + level, e);
		}

		// Find the layer indexes
		backLayer = this.map.getLayerIndex("back");
		overLayer = this.map.getLayerIndex("over");
		metaLayer = this.map.getLayerIndex("meta");

		// Validate that layer indexes were actually found.
		if (backLayer == -1 || overLayer == -1 || metaLayer == -1) {
			throw new MapException("Invalid map layers for level " + level);
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
