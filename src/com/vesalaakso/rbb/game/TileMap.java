package com.vesalaakso.rbb.game;

import org.newdawn.slick.tiled.TiledMap;

/**
 * Represents a tile map. Drawing of the tile map is located in
 * {@link TileMapPainter} class.
 */
public class TileMap {
	
	/** Size of a single tile, in pixels. */
	public static final int TILE_SIZE = 32;

	/** The tile map this instance represents */
	private TiledMap map;

	/** Index of the back layer that is drawn first */
	private int backLayer;

	/** Index of the over layer that is drawn last */
	private int overLayer;

	/**
	 * Index of the meta layer that contains information about special tiles.
	 * This layer is not drawn.
	 */
	private int metaLayer;

	/** The width of the map, in tiles. */
	private int widthInTiles;

	/** The height of the map, in tiles. */
	private int heightInTiles;
}
