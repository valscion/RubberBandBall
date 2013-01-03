package com.vesalaakso.rbb.model;

import java.util.List;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Class for handling the interaction with the physics engine, fizzy.
 * 
 * @author Vesa Laakso
 */
public class Physics {

	/** Default gravity */
	private static final float DEFAULT_GRAVITY = 9.81f;

	/** The <code>World</code> in which The Magic (tm) happens. */
	private World world;

	/** The bodies associated with the map, linked to the collidable tile. */
	private BiMap<Body<?>, CollidableTile> bodyTileMap = HashBiMap.create();

	/**
	 * Constructs the physics engine and boots it up.
	 */
	public Physics() {
		world = new World(DEFAULT_GRAVITY);
	}

	/** Makes the physics engine tick. Call on every update. */
	public void update() {
		world.update(1 / 20f);
	}

	/**
	 * Adds collision shapes from the given map to the world.
	 * 
	 * @param map
	 *            the <code>TileMap</code> to get collidable tiles from
	 * @throws MapException
	 *             if the given map contained invalid collidable tiles
	 */
	public void addCollidablesFromMap(TileMap map) throws MapException {
		List<CollidableTile> tiles = map.getCollidableTiles();

		for (CollidableTile tile : tiles) {
			// The static body we will add later on to the world and
			// mapBodies list
			StaticBody<?> body = null;

			TileShape shape = tile.getTileShape();

			if (shape == TileShape.RECTANGLE) {
				Rectangle rect =
						new Rectangle(TileMap.TILE_SIZE, TileMap.TILE_SIZE);
				body = new StaticBody<Rectangle>(rect,
						tile.getTileX() * TileMap.TILE_SIZE,
						tile.getTileY() * TileMap.TILE_SIZE);
			}
			else if (shape.isTriangular()) {
				Polygon triangle =
						createTrianglePolygon(shape, TileMap.TILE_SIZE);
				body = new StaticBody<Polygon>(triangle,
						tile.getTileX() * TileMap.TILE_SIZE,
						tile.getTileY() * TileMap.TILE_SIZE);
			}
			else {
				String errStr = String.format(
						"Invalid collidable tile at (%d, %d): %s",
						tile.getTileX(), tile.getTileY(), shape.toString());
				throw new MapException(errStr);
			}

			// Now we have our pretty body, let's use it as it should be used.
			bodyTileMap.put(body, tile);
			world.add(body);
		}
	}

	/**
	 * Builds a new triangle-shaped Polygon based on the given parameter.
	 * 
	 * @param shape
	 *            a <code>TileShape</code> representing a triangle
	 * @param width
	 *            width of a single side of the triangle
	 * @return a new Polygon representing a triangular shape.
	 */
	private Polygon createTrianglePolygon(TileShape shape, float width) {
		Vec2[] points = null;

		switch (shape) {
			case TRIANGLE_TOP_LEFT:
				points = new Vec2[] {
						new Vec2(0, 0), // Left, Top
						new Vec2(width, 0), // Right, Top
						new Vec2(0, width) // Left, Bottom
				};
				break;
			case TRIANGLE_BOTTOM_LEFT:
				points = new Vec2[] {
						new Vec2(0, 0), // Left, Top
						new Vec2(width, width), // Right, Bottom
						new Vec2(0, width) // Left, Bottom
				};
				break;
			case TRIANGLE_BOTTOM_RIGHT:
				points = new Vec2[] {
						new Vec2(0, width), // Left, Bottom
						new Vec2(width, 0), // Right, Top
						new Vec2(width, width) // Right, Bottom
				};
				break;
			case TRIANGLE_TOP_RIGHT:
				points = new Vec2[] {
						new Vec2(0, 0), // Left, Top
						new Vec2(width, 0), // Right, Top
						new Vec2(width, width) // Right, Bottom
				};
				break;
			default:
				throw new IllegalArgumentException(
						"The given shape is not triangular. Got: " + shape);
		}

		Polygon triangle = new Polygon();
		triangle.setPoints(points);
		return triangle;
	}
}
