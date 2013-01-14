package com.vesalaakso.rbb.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.tiled.GroupObject;
import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;
import com.vesalaakso.rbb.controller.PlayerCollisionListener;
import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Class for handling the interaction with the physics engine, fizzy.
 * 
 * @author Vesa Laakso
 */
public class Physics implements Updateable, Resetable {

	/** Default gravity */
	private static final float DEFAULT_GRAVITY = 20.0f;

	/**
	 * Angular velocity cap, if the rotational speed is smaller than this, the
	 * ball is stopped from spinning.
	 */
	private static final float MIN_ANGULAR_VELOCITY = 0.1f;

	/**
	 * The ParticleManager which will create nice little effects as events in
	 * the physics world will happen.
	 */
	private ParticleManager particleManager;

	/** The <code>World</code> in which The Magic (tm) happens. */
	private World world;

	/** Current x-gravity in the world */
	private float xGravity;

	/** Current y-gravity in the world */
	private float yGravity;

	/** The bodies associated with the map, linked to the collidable object. */
	private Map<Body<?>, TileMapObject> bodyTileMap = Maps.newHashMap();

	/** The player whose body will be added to the physics engine. */
	private Player player;

	/** The body of the player. */
	private Body<Circle> playerBody;

	/** The listener listening for collisions of playerBody */
	private PlayerCollisionListener playerCollisionListener;

	/**
	 * A body to simulate rolling friction against or null if no simulation
	 * should happen.
	 */
	private Body<?> frictionSimulationBody;

	/** <code>TileMapContainer</code> to query the map from */
	private TileMapContainer mapContainer;

	/**
	 * Constructs the physics engine and boots it up with default gravity.
	 * 
	 * @param player
	 *            the <code>Player</code> bouncing around in the world.
	 * @param particleManager
	 *            the <code>ParticleManager</code> responsible for all the nice
	 *            and juicy particles
	 * @param mapContainer
	 *            the <code>TileMapContainer</code> to query the current map
	 *            from
	 */
	public Physics(Player player, ParticleManager particleManager,
			TileMapContainer mapContainer) {
		this.player = player;
		this.particleManager = particleManager;
		this.world = new World(DEFAULT_GRAVITY);
		this.mapContainer = mapContainer;
	}

	/**
	 * Makes the physics engine tick. Call on every update.
	 */
	@Override
	public void update(int delta) {
		world.update(1 / 40f);
		if (player == null || !player.isStartPositioned()) {
			// We've got nothing to do here
			return;
		}
		if (player.isStartPositioned() && playerBody == null) {
			// Player has not yet been added.
			addPlayer();
		}
		player.setPosition(playerBody.getX(), playerBody.getY());
		player.setAngle(playerBody.getRotation());

		if (!player.isReadyForLaunch() && playerBody.isSleeping()) {
			// We've arrived somewhere. Control to the player, ahoy!
			player.onStop();
		}
		else if (frictionSimulationBody != null && !playerBody.isSleeping()) {
			// Simulate friction against the body.
			float angVel = playerBody.getAngularVelocity();
			int dir = 0;
			if (xGravity == 0 && yGravity != 0) {
				dir = playerBody.getXVelocity() > 0 ? -1 : 1;
			}
			else if (xGravity != 0 && yGravity == 0 ) {
				dir = playerBody.getYVelocity() > 0 ? -1 : 1;
			}

			if (Math.abs(angVel) < MIN_ANGULAR_VELOCITY) {
				// Ok, a velocity smaller than MIN_ANGULAR_VELOCITY will be
				// considered stopped.
				playerBody.setAngularVelocity(0);
			}
			else {
				playerBody.applyTorque(dir * 1000f);
			}
		}
		// If we're in a floaty world, force the player to stop at small
		// velocities
		if (xGravity == 0 && yGravity == 0 && !playerBody.isSleeping()) {
			float angVel = playerBody.getAngularVelocity();
			float vel = Math.abs(playerBody.getXVelocity())
					+ Math.abs(playerBody.getYVelocity());

			if (angVel > -.1f && angVel < .1f) {
				// An angular velocity smaller than 0.1f will be considered
				// stopped.
				playerBody.setAngularVelocity(0);
			}
			if (vel < .25f) {
				// A combined velocity smaller than 0.25f will force stop
				// player.
				playerBody.setVelocity(0, 0);
			}
		}
	}

	/**
	 * Initializes the physics world based on the given map.
	 * 
	 * @throws MapException
	 *             if the current map contained invalid collidable tiles
	 */
	public void initializeMap() throws MapException {
		TileMap map = mapContainer.getMap();

		// Add all collisions
		List<TileMapObject> colObjs = map.getCollisionObjects();

		// The last determinant sign of a polygon
		float lastDetSign = 0;

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
				body = new StaticBody<Rectangle>(rect, obj.x, obj.y);
			}
			else if (isPolygon) {
				Polygon polygon = createPolygon(obj);
				// Calculate the determinant
				float det = polygon.getPointX(0) * polygon.getPointY(1) - 
						polygon.getPointX(1) * polygon.getPointY(0);
				float detSign = Math.signum(det);
				if (lastDetSign == 0) {
					lastDetSign = detSign;
				}
				else if (detSign != lastDetSign) {
					Log.warn("Map has got mixed CW / CCW polygons!");
				}
				body = new StaticBody<Polygon>(polygon, obj.x, obj.y);
			}
			else {
				String f = "Invalid collidable tile at (%d, %d): %s";
				String errStr = String.format(f, obj.x, obj.y, obj.objectType);
				throw new MapException(errStr);
			}

			// Now we have our pretty body, let's use it as it should be used.
			body.setRestitution(0); // No bouncing
			body.setFriction(1); // Friction is set by the colliding object
			bodyTileMap.put(body, obj);
			world.add(body);
		}

		// Setup world gravity based on map properties values.
		String strGravX = map.getTiledMap().getMapProperty("gravity_x", "");
		String strGravY = map.getTiledMap().getMapProperty("gravity_y", "");

		xGravity = 0.0f;
		yGravity = DEFAULT_GRAVITY;

		if (!strGravX.isEmpty()) {
			try {
				xGravity = Float.parseFloat(strGravX);
			}
			catch (NumberFormatException e) {
				Log.warn("Failed to parse gravity_x from map properties");
			}
		}
		if (!strGravY.isEmpty()) {
			try {
				yGravity = Float.parseFloat(strGravY);
			}
			catch (NumberFormatException e) {
				Log.warn("Failed to parse gravity_y from map properties");
			}
		}

		world.setGravity(xGravity, yGravity);
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

			int pointIndex = (geomPoints.length - i - 2) / 2;

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

		// Player should stop eventually, even if there is no gravity. The
		// damping value specifies how much will the player lose speed even when
		// in mid-air. Defaults to 0
		float damping = 0.1f;

		// Since friction doesn't do anything when we're creating a circle, we
		// simulate it ourselves elsewhere. Angular damping specifies the amount
		// of spinning force the circle will lose over time at every update.
		float angularDamping = 0.1f;

		if (xGravity == 0 && yGravity == 0) {
			// Larger damping when there's no gravity
			damping *= 2;
			angularDamping *= 2;
		}

		Circle shape = new Circle(radius, density, restitution);
		playerBody =
				new DynamicBody<Circle>(shape, player.getX(), player.getY());

		playerBody.setLinearDamping(damping);
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
		if (getPlayerBody() != null) {
			ret.add(getPlayerBody());
		}

		return ret;
	}

	/**
	 * Gets only the body of the player. WARNING! Can be <code>null</code>!
	 * 
	 * @return physics body of the player.
	 */
	public Body<Circle> getPlayerBody() {
		if (playerBody != null && playerBody.isAttached()) {
			return playerBody;
		}
		return null;
	}

	/**
	 * Gets the <code>TileMapObject</code> mapped to a <code>Body</code>.
	 * 
	 * @param body
	 *            the <code>Body</code> to search for a
	 *            <code>TileMapObject</code> for.
	 * 
	 * @return a <code>TileMapObject</code> mapped to the given
	 *         <code>Body</code> or <code>null</code>, if none was found.
	 */
	public TileMapObject getTileMapObject(Body<?> body) {
		return bodyTileMap.get(body);
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
		frictionSimulationBody = null;
	}

	/**
	 * Starts simulating rolling friction for the player.
	 * 
	 * @param otherBody
	 *            the <code>Body</code> in to which the player collides
	 *            currently.
	 */
	public void startSimulatingFriction(Body<?> otherBody) {
		frictionSimulationBody = otherBody;
	}

	/**
	 * Stops the rolling friction simulation, called when player is no longer
	 * colliding to the previous simulated body.
	 */
	public void stopSimulatingFriction() {
		frictionSimulationBody = null;
	}

	/**
	 * Sets the rising of the player to 0, effectively forcing it to start
	 * dropping down, not letting it bounce.
	 */
	public void stopPlayerRising() {
		Log.info("Forcefully stopped player from bouncing.");
		if (yGravity != 0) {
			playerBody.setVelocity(playerBody.getXVelocity(), 0);
		}
		if (xGravity != 0) {
			playerBody.setVelocity(0, playerBody.getYVelocity());
		}
	}

	/**
	 * Gets the value of how fast the player is going down.
	 * 
	 * @return player velocity in y-direction
	 */
	public float getPlayerFallingVelocity() {
		if (xGravity == 0 && yGravity != 0) {
			if (yGravity < 0) {
				return -playerBody.getYVelocity();
			}
			else {
				return playerBody.getYVelocity();
			}
		}
		if (xGravity != 0 && yGravity == 0) {
			if (xGravity < 0) {
				return -playerBody.getXVelocity();
			}
			else {
				return playerBody.getXVelocity();
			}
		}
		if (xGravity != 0 && yGravity != 0) {
			Vector2f gravVec = new Vector2f(xGravity, yGravity);
			return gravVec.length();
		}
		// If we got so far, there is no gravity.
		return 0;
	}

	/**
	 * Sets the gravity in the world.
	 * 
	 * @param xGravity
	 *            gravity in x-direction
	 * @param yGravity
	 *            gravity in y-direction
	 */
	public void setGravity(float xGravity, float yGravity) {
		if (xGravity != this.xGravity || yGravity != this.yGravity) {
			this.xGravity = xGravity;
			this.yGravity = yGravity;
			world.setGravity(xGravity, yGravity);
		}
	}

	/**
	 * When a map changes, create a new world simulating the physics and reset
	 * the player.
	 */
	@Override
	public void reset() {
		// Remove old bodies
		for (Body<?> mapBody : bodyTileMap.keySet()) {
			world.remove(mapBody);
		}
		bodyTileMap.clear();
		if (playerBody != null) {
			playerBody.setActive(false);
			world.remove(playerBody);
			playerBody = null;
		}

		// Reset state
		frictionSimulationBody = null;

		// Re-initialize for the new map.
		try {
			initializeMap();
		}
		catch (MapException e) {
			Log.error("There was a problem with changing map in Physics", e);
		}

		// Re-initialize the player.
		if (playerCollisionListener != null) {
			world.removeListener(playerCollisionListener);
		}
	}

	/**
	 * Returns whether player is above the given body. Takes gravity direction
	 * into account.
	 * 
	 * @param body
	 *            the body to check against
	 * 
	 * @return <code>true</code> if player is above the given body
	 */
	public boolean playerIsAbove(Body<?> body) {
		float playerValue, bodyValue;
		if (yGravity != 0) {
			playerValue = playerBody.getY();
			if (yGravity < 0) {
				playerValue = -playerValue;
			}
			bodyValue = body.getY();
		}
		else if (xGravity != 0) {
			playerValue = playerBody.getX();
			if (xGravity < 0) {
				playerValue = -playerValue;
			}
			bodyValue = body.getY();
		}
		else {
			return false;
		}

		return playerValue < bodyValue;
	}
}
