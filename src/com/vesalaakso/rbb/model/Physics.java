package com.vesalaakso.rbb.model;

import java.util.LinkedList;
import java.util.List;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vesalaakso.rbb.controller.PlayerCollisionListener;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Class for handling the interaction with the physics engine, fizzy.
 * 
 * @author Vesa Laakso
 */
public class Physics implements Updateable {

	/** Default gravity */
	private static final float DEFAULT_GRAVITY = 9.81f;

	/**
	 * The ParticleManager which will create nice little effects as events in
	 * the physics world will happen.
	 */
	private ParticleManager particleManager;

	/** The <code>World</code> in which The Magic (tm) happens. */
	private World world;

	/** The bodies associated with the map, linked to the collidable tile. */
	private BiMap<Body<?>, CollidableTile> bodyTileMap = HashBiMap.create();

	/** The player whose body will be added to the physics engine. */
	private Player player;

	/** The body of the player. */
	private Body<Circle> playerBody = null;

	/**
	 * Constructs the physics engine and boots it up with default gravity.
	 * 
	 * @param particleManager
	 *            the <code>ParticleManager</code> responsible for all the nice
	 *            and juicy particles
	 */
	public Physics(ParticleManager particleManager) {
		this.particleManager = particleManager;
		this.world = new World(DEFAULT_GRAVITY);
	}

	/**
	 * Makes the physics engine tick. Call on every update.
	 */
	@Override
	public void update(int delta) {
		world.update(1 / 20f);
		if (player != null) {
			player.setPosition(playerBody.getX(), playerBody.getY());
		}
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

	/**
	 * Adds the player to the physics engine.
	 * 
	 * @param player
	 *            the <code>Player</code>
	 * @throws NullPointerException
	 *             if the given player is <code>nullq</code>.
	 */
	public void addPlayer(Player player) {
		if (player == null) {
			throw new NullPointerException("player was null");
		}
		this.player = player;

		Circle shape = new Circle(player.getRadius());
		playerBody = new DynamicBody<Circle>(shape, player.getX(),
				player.getY());
		world.add(playerBody);
		world.addBodyListener(playerBody,
				new PlayerCollisionListener(player, particleManager));
	}

	/**
	 * Gets all the bodies associated with the physics engine in a
	 * <code>List</code> backed by <code>LinkedList</code>.
	 * 
	 * @return list of all bodies in the physics world
	 */
	public List<Body<?>> getBodies() {
		List<Body<?>> ret = new LinkedList<Body<?>>();

		// Add all the static tile bodies
		ret.addAll(bodyTileMap.keySet());

		// If player has a body set, add it too.
		if (playerBody != null) {
			ret.add(playerBody);
		}

		return ret;
	}

	/**
	 * Gets only the body of the player.
	 * 
	 * @return physics body of the player.
	 */
	public Body<Circle> getPlayerBody() {
		return playerBody;
	}

	/**
	 * Stops the movement of the player.
	 */
	public void stopPlayer() {
		playerBody.setActive(false);
	}
}
