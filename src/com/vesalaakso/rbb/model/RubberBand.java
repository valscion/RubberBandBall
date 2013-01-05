package com.vesalaakso.rbb.model;

import org.newdawn.slick.geom.Vector2f;

/**
 * The model for the rubber band which is pulled from the player and defines the
 * starting direction and force the ball will be launched.
 * 
 * @author Vesa Laakso
 */
public class RubberBand {

	/** The <code>Player</code> from which the rubber band will be pulled */
	private Player player;

	/** Are we pulling the rubber band currently? */
	private boolean isPulled;

	/** Start point in world coordinates. */
	private Vector2f startPoint;

	/** The current end point in world coordinates. */
	private Vector2f currentEndPoint;

	/**
	 * Constructs a new rubber band model and associates it with the given
	 * <code>Player</code>.
	 * 
	 * @param player
	 */
	public RubberBand(Player player) {
		this.player = player;
	}

	/**
	 * Called when mouse is pushed down and the pulling might start.
	 * 
	 * @param x
	 *            the start x-coordinate for the pull in screen coordinates
	 * @param y
	 *            the start y-coordinate for the pull in screen coordinates
	 * 
	 * @return true if the pull actually started, i.e. coordinates were close
	 *         enough to the player.
	 */
	public boolean startPull(float x, float y) {
		// Calculate the real start point, with Camera coordinates added to the
		// vector representing the starting point
		Vector2f point = new Vector2f(x, y);
		Camera.toWorldCoordinates(point);

		// And the endpoint as a vector, too.
		Vector2f playerLocation = new Vector2f(player.getX(), player.getY());

		// If the distance between the two points is smaller than the radius
		// of the player, consider pulling started.
		if (point.distanceSquared(playerLocation) < player.getRadius()
				* player.getRadius()) {
			// Close enough.
			isPulled = true;
			startPoint = playerLocation;
			currentEndPoint = new Vector2f(startPoint);
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
		Vector2f point = new Vector2f(endX, endY);
		Camera.toWorldCoordinates(point);
		currentEndPoint = point;
		isPulled = false;
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
		// Calculate the real end point, with Camera coordinates added to the
		// vector representing the starting point
		Vector2f point = new Vector2f(x, y);
		Camera.toWorldCoordinates(point);
		currentEndPoint = point;
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
	public Vector2f getStartPoint() {
		return startPoint;
	}

	/**
	 * Returns the current endpoint.
	 * 
	 * @return current endpoint of the rubber band.
	 */
	public Vector2f getEndPoint() {
		return currentEndPoint;
	}
}
