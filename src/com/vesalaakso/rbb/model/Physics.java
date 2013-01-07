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
import org.newdawn.slick.tiled.GroupObject;
import org.newdawn.slick.util.Log;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.vesalaakso.rbb.controller.MapChangeListener;
import com.vesalaakso.rbb.controller.PlayerCollisionListener;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Class for handling the interaction with the physics engine, fizzy.
 * 
 * @author Vesa Laakso
 */
public class Physics implements Updateable, MapChangeListener {

	/** Default gravity */
	private static final float DEFAULT_GRAVITY = 9.81f;

	/**
	 * The ParticleManager which will create nice little effects as events in
	 * the physics world will happen.
	 */
	private ParticleManager particleManager;

	/** The <code>World</code> in which The Magic (tm) happens. */
	private World world;

	/** The bodies associated with the map, linked to the collidable object. */
	private BiMap<Body<?>, TileMapObject> bodyTileMap = HashBiMap.create();

	/** The player whose body will be added to the physics engine. */
	private Player player;

	/** The body of the player. */
	private Body<Circle> playerBody;

	/** The listener listening for collisions of playerBody */
	private PlayerCollisionListener playerCollisionListener;

	/** Should we simulate friction on every update call */
	private boolean isSimulatingFriction;

	/**
	 * Constructs the physics engine and boots it up with default gravity.
	 * 
	 * @param player
	 *            the <code>Player</code> bouncing around in the world.
	 * @param particleManager
	 *            the <code>ParticleManager</code> responsible for all the nice
	 *            and juicy particles
	 */
	public Physics(Player player, ParticleManager particleManager) {
		this.player = player;
		this.particleManager = particleManager;
		this.world = new World(DEFAULT_GRAVITY);
	}

	/**
	 * Makes the physics engine tick. Call on every update.
	 */
	@Override
	public void update(int delta) {
		world.update(1 / 60f);
		if (player != null) {
			player.setPosition(playerBody.getX(), playerBody.getY());

			if (!player.isReadyForLaunch() && playerBody.isSleeping()) {
				// We've arrived somewhere. Control to the player, ahoy!
				player.onStop();
			}
			else if (isSimulatingFriction && !playerBody.isSleeping()) {
				// Simulate friction against the body.
				float angVel = playerBody.getAngularVelocity();
				int dir = playerBody.getXVelocity() > 0 ? -1 : 1;

				if (angVel > -.1f && angVel < .1f) {
					// Ok, a velocity smaller than 0.1f will be considered
					// stopped.
					playerBody.setAngularVelocity(0);
				}
				else {
					playerBody.applyTorque(dir * 1000f);
				}
			}
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
		List<TileMapObject> colObjs = map.getCollisionObjects();

		for (TileMapObject obj : colObjs) {
			// The static body we will add later on to the world and
			// mapBodies list
			StaticBody<?> body = null;

			boolean isPolygon = false;
			boolean isRectangle = false;
			if (obj.objectType == GroupObject.ObjectType.POLYGON) {
				isPolygon = true;
			}
			else if (obj.objectType == GroupObject.ObjectType.RECTANGLE) {
				isRectangle = true;
			}

			if (isRectangle) {
				Rectangle rect = new Rectangle(obj.width, obj.height);
				body = new StaticBody<Rectangle>(rect, obj.x * obj.width, 
						obj.y * obj.height);
			}
			else if (isPolygon) {
				Polygon polygon = createPolygon(obj);
				body = new StaticBody<Polygon>(polygon, obj.x * obj.width,
						obj.y * obj.height);
			}
			else {
				String errStr = String.format(
						"Invalid collidable tile at (%d, %d): %s",
						obj.x, obj.y, obj.objectType);
				throw new MapException(errStr);
			}

			// Now we have our pretty body, let's use it as it should be used.
			body.setRestitution(0); // No bouncing
			body.setFriction(1); // Friction is set by the colliding object
			bodyTileMap.put(body, obj);
			world.add(body);
		}
	}

	/**
	 * Builds a new Polygon based on the given TileMapObject.
	 * 
	 * @param obj
	 *            a <code>TileMapObject</code> representing a polygon
	 * @return a new Polygon representing the shape.
	 * 
	 * @throws MapException
	 *             if the polygon could not be created
	 */
	private Polygon createPolygon(TileMapObject obj) throws MapException {
		// Get the underlying geometric polygon shape
		org.newdawn.slick.geom.Polygon polygon = obj.getPolygon();
		
		// Get the geometric points, using CW winding rule.
		float[] geomPoints = polygon.getPoints();
		
		// Make sure that we got some points and we got an x- and y-coordinate
		// for them all.
		if (geomPoints.length == 0 || geomPoints.length % 2 == 1) {
			throw new MapException("Weird geometric points for " + obj);
		}
		
		// Store the points in a CCW winding rule in this array of vectors
		Vec2[] points = new Vec2[geomPoints.length / 2];
		
		// Loop-de-la-loop backwards to store those.
		for (int i = geomPoints.length - 2; i >= 0; i -= 2) {
			float x = geomPoints[i];
			float y = geomPoints[i + 1];

			int pointIndex = (geomPoints.length - i) / 2;

			points[pointIndex] = new Vec2(x, y);
		}

		Polygon triangle = new Polygon();
		triangle.setPoints(points);
		return triangle;
	}

	/** A helper method to add the player to the world. */
	private void addPlayer() {

		// Values which control the physics object representing the player.
		// These are specified here in order to get a better picture of all the
		// different values.
		float radius = player.getRadius();

		// How dense the circle will be, 25.0f is the default value.
		float density = 25.0f;

		// Restitution specifies how much will the circle bounce off when it
		// hits a wall. 0.9f is the default.
		float restitution = 0.75f;

		// Since friction doesn't do anything when we're creating a circle, we
		// simulate it ourselves elsewhere. Angular damping specifies the amount
		// of spinning force the circle will lose over time at every update.
		float angularDamping = 0.1f;

		Circle shape = new Circle(radius, density, restitution);
		playerBody =
				new DynamicBody<Circle>(shape, player.getX(), player.getY());
		playerBody.setAngularDamping(angularDamping);

		// Add the player to the world
		world.add(playerBody);

		// Initialize the listener and add it to the world
		playerCollisionListener =
				new PlayerCollisionListener(this, player, particleManager);
		world.addBodyListener(playerBody, playerCollisionListener);

		// Also, make the player not yet take part in any collision.
		playerBody.setActive(false);
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
	 * Launches the player by applying a force to him.
	 * 
	 * @param forceX
	 *            the power of the force in x-axis
	 * @param forceY
	 *            the power of the force in y-axis
	 */
	public void launchPlayer(float forceX, float forceY) {
		playerBody.setActive(true);
		playerBody.setVelocity(forceX, forceY);
		isSimulatingFriction = false;
	}

	/**
	 * Tries to simulate a rolling friction for the player body.
	 * 
	 * @param otherBody
	 *            the <code>Body</code> in to which the player collides
	 *            currently.
	 */
	public void startSimulatingFriction(Body<?> otherBody) {
		TileMapObject tile = bodyTileMap.get(otherBody);

		if (tile.objectType == GroupObject.ObjectType.RECTANGLE) {
			// For now, only simulate friction for rectangles. And only if the
			// rectangle is below us.
			if (playerBody.getY() < otherBody.getY()) {
				System.out.println("Starting friction simulation");
				isSimulatingFriction = true;
			}
		}
	}

	/**
	 * Stops the rolling friction simulation, called when player is no longer
	 * colliding to the previous simulated body.
	 */
	public void stopSimulatingFriction() {
		if (isSimulatingFriction) {
			System.out.println("Stopped friction simulation");
			isSimulatingFriction = false;
		}
	}

	/**
	 * When a map changes, create a new world simulating the physics and reset
	 * the player.
	 * 
	 * @see MapChangeListener#onMapChange(TileMap, TileMap)
	 */
	@Override
	public void onMapChange(TileMap oldMap, TileMap newMap) {
		// Remove old bodies
		for (Body<?> mapBody : bodyTileMap.keySet()) {
			world.remove(mapBody);
		}
		bodyTileMap.clear();
		if (playerBody != null) {
			world.remove(playerBody);
		}

		// Reset state
		isSimulatingFriction = false;

		// Re-initialize for the new map.
		try {
			addCollidablesFromMap(newMap);
		}
		catch (MapException e) {
			Log.error("There was a problem with changing map in Physics", e);
		}

		// Re-initialize the player.
		if (playerCollisionListener != null) {
			world.removeListener(playerCollisionListener);
		}
		addPlayer();
	}
}
