package com.vesalaakso.rbb.model;

import org.newdawn.fizzy.Vector;
import org.newdawn.slick.geom.Vector2f;

import com.vesalaakso.rbb.controller.MapChangeListener;
import com.vesalaakso.rbb.util.Utils;

/**
 * The model for the rubber band which is pulled from the player and defines the
 * starting direction and force the ball will be launched.
 * 
 * @author Vesa Laakso
 */
public class RubberBand implements MapChangeListener {

	/** The <code>Player</code> from which the rubber band will be pulled */
	private Player player;

	/** The <code>Physics</code> to interact with when the player is launched */
	private Physics physics;

	/** Are we pulling the rubber band currently? */
	private boolean isPulled;

	/** Start point in world coordinates. */
	private Vector startPoint;

	/** The current end point in world coordinates. */
	private Vector currentEndPoint;

	/**
	 * Constructs a new rubber band model and associates it with the given
	 * <code>Player</code> and <code>Physics</code>.
	 * 
	 * @param player
	 *            the <code>Player</code> from which the rubber band is pulled
	 * @param physics
	 *            the <code>Physics</code> engine behind all The Magic (tm).
	 */
	public RubberBand(Player player, Physics physics) {
		this.player = player;
		this.physics = physics;
	}

	/**
	 * Called when mouse is pushed down and the pulling might start.
	 * 
	 * @param x
	 *            the start x-coordinate for the pull in screen coordinates
	 * @param y
	 *            the start y-coordinate for the pull in screen coordinates
	 * 
	 * @return <code>true</code> if the pull actually started
	 */
	public boolean startPull(float x, float y) {
		if (!player.isReadyForLaunch()) {
			// Player isn't ready to be pulled, so don't.
			return false;
		}
		// Translate parameters to world coordinates
		x = Utils.screenToWorldX(x);
		y = Utils.screenToWorldY(y);

		// Create a start point vector
		Vector2f point = new Vector2f(x, y);

		// And the endpoint as a vector, too.
		Vector2f playerLocation = new Vector2f(player.getX(), player.getY());

		// If the distance between the two points is smaller than the radius
		// of the player, consider pulling started.
		if (point.distanceSquared(playerLocation) < player.getRadius()
				* player.getRadius()) {
			// Close enough.
			isPulled = true;
			startPoint = new Vector(player.getX(), player.getY());
			currentEndPoint = startPoint;
			return true;
		}
		return false;
	}

	/**
	 * Called when the pulling ends.
	 * 
	 * @param endX
	 *            the end x-coordinate for the pull in screen coordinates
	 * @param endY
	 *            the end y-coordinate for the pull in screen coordinates
	 */
	public void endPull(float endX, float endY) {
		// Calculate the real end point, with Camera coordinates added to the
		// vector representing the starting point
		endX = Utils.screenToWorldX(endX);
		endY = Utils.screenToWorldY(endY);
		currentEndPoint = new Vector(endX, endY);
		isPulled = false;

		// Ok, now launch the player to somewhere!

		// Calculate the vector between the start point and the end point.
		Vector2f diffVector = new Vector2f(startPoint.x - endX,
				startPoint.y - endY);

		// The calculated force to launch the player in both axis is the same
		// as the scalar projection of the x- and y-axis vectors and the
		// diffVector.
		double radAngleX = Math.toRadians(diffVector.getTheta());
		double radAngleY = Math.toRadians(diffVector.getTheta() - 90);

		float forceX = (float) (Math.cos(radAngleX) * diffVector.length()) / 2;
		float forceY = (float) (Math.cos(radAngleY) * diffVector.length()) / 2;

		System.out.println("Launching the player!");
		System.out.println("    diffVector length: " + diffVector.length());
		System.out.println("     diffVector angle: " + diffVector.getTheta());
		System.out.println("              x-force: " + forceX);
		System.out.println("              y-force: " + forceY);

		player.setLaunched();
		physics.launchPlayer(forceX, forceY);
	}

	/**
	 * Called when the mouse moves, this method will update the current endPoint
	 * of the rubber band.
	 * 
	 * @param x
	 *            the current endpoint x-coordinate in screen coordinates
	 * @param y
	 *            the current endpoint y-coordinate in screen coordinates
	 */
	public void pull(float x, float y) {
		// Calculate the end point in world coordinates
		x = Utils.screenToWorldX(x);
		y = Utils.screenToWorldY(y);
		currentEndPoint = new Vector(x, y);
	}

	/**
	 * Gets the info of whether the rubber band is being pulled or not.
	 * 
	 * @return <code>true</code>, if the rubber band is being pulled.
	 */
	public boolean isPulled() {
		return isPulled;
	}

	/**
	 * Returns the startpoint.
	 * 
	 * @return current startpoint of the rubber band.
	 */
	public Vector getStartPoint() {
		return startPoint;
	}

	/**
	 * Returns the current endpoint.
	 * 
	 * @return current endpoint of the rubber band.
	 */
	public Vector getEndPoint() {
		return currentEndPoint;
	}

	/**
	 * Resets the rubber band when map is changed.
	 * 
	 * @see MapChangeListener#onMapChange(TileMap, TileMap)
	 */
	@Override
	public void onMapChange(TileMap oldMap, TileMap newMap) {
		startPoint = null;
		currentEndPoint = null;
		isPulled = false;
	}
}
