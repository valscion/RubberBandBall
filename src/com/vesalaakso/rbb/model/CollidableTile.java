package com.vesalaakso.rbb.model;

import org.newdawn.slick.tiled.TiledMap;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableMap;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Represents every tile that is collidable in a tile map.
 * 
 * @author Vesa Laakso
 */
public class CollidableTile {

	/** Tile x-coordinate */
	private int tileX;

	/** Tile y-coordinate */
	private int tileY;

	/** The shape of the tile */
	private TileShape shape;

	/**
	 * Strings that represent different shapes in tile attributes mapped to
	 * the corresponding <code>TileShape</code>
	 */
	private static final ImmutableMap<String, TileShape> shapesMap =
		ImmutableMap.<String, TileShape> of(
				"all", TileShape.RECTANGLE,
				"top-left", TileShape.TRIANGLE_TOP_LEFT,
				"bottom-left", TileShape.TRIANGLE_BOTTOM_LEFT,
				"bottom-right", TileShape.TRIANGLE_BOTTOM_RIGHT,
				"top-right", TileShape.TRIANGLE_TOP_RIGHT);

	/**
	 * Creates the new <code>TileShape</code> from the given location of the
	 * given map.
	 * 
	 * @param tileX
	 *            x-coordinate of the tile to create
	 * @param tileY
	 *            y-coordinate of the tile to create
	 * @param map
	 *            <code>TileMap</code> where to get the information from
	 * 
	 * @throws MapException
	 *             if the given tile does not represent a collision tile
	 */
	public CollidableTile(int tileX, int tileY, TileMap map) throws MapException {
		this.tileX = tileX;
		this.tileY = tileY;

		int metaLayer = map.getIndexOfMetaLayer();

		TiledMap tmap = map.getTiledMap();

		int tileID = tmap.getTileId(tileX, tileY, metaLayer);
		String value = tmap.getTileProperty(tileID, "collision", "false");

		if (shapesMap.containsKey(value)) {
			shape = shapesMap.get(value);
		}
		else {
			String errStr = String.format(
					"Tile (%d, %d) does not represent a collision tile.",
					tileX, tileY);
			throw new MapException(errStr);
		}
	}

	/**
	 * Gets the x-coordinate of this tile, in tile coordinates.
	 * 
	 * @return tile x-coordinate
	 */
	public int getTileX() {
		return tileX;
	}

	/**
	 * Gets the y-coordinate of this tile, in tile coordinates.
	 * 
	 * @return tile y-coordinate
	 */
	public int getTileY() {
		return tileY;
	}

	/**
	 * Gets the <code>TileShape</code> attached with this collidable tile.
	 * 
	 * @return the attached <code>TileShape</code>.
	 */
	public TileShape getTileShape() {
		return shape;
	}

	@Override
	public String toString() {
		return Objects.toStringHelper(this)
				.add("tileX", tileX)
				.add("tileY", tileY)
				.toString();
	}
}
