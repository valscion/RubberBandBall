package com.vesalaakso.rbb.model;

import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.util.Utils;

/**
 * Represents the ball player moves.
 * 
 * @author Vesa Laakso
 */
public class Player implements Resetable {

	/** The map container to query the map <code>Player</code> belongs to. */
	private TileMapContainer mapContainer;

	/** Center x-coordinate of the player. */
	private float xWorld;

	/** Center y-coordinate of the player. */
	private float yWorld;

	/** Radius of the ball representing the player. */
	private float radius = 16f;

	/** Angle of the player, in radians */
	private float angle;

	/** Is player ready to be launched or not */
	private boolean isReadyForLaunch;

	/** Has player been positioned for start */
	private boolean isStartPositioned;

	/** The happiness of the player, measured in range -1.0 ... 1.0 */
	private float happiness;

	/**
	 * Constructs the <code>Player</code> and sets it up with the given
	 * <b>TileMapContainer</b> to query the current map when needed.
	 * 
	 * @param mapContainer
	 *            the map container to query the current map from
	 */
	public Player(TileMapContainer mapContainer) {
		this.mapContainer = mapContainer;
	}

	/**
	 * Reset the position and status of the player.
	 */
	@Override
	public void reset() {
		// Set the player to the center of the spawn area
		TileMapObject spawn = mapContainer.getMap().getSpawnArea();
		xWorld = spawn.x + spawn.width * .5f;
		yWorld = spawn.y + spawn.height * .5f;

		// Reset happiness
		happiness = 0;

		// Reset launch and position flags
		isReadyForLaunch = false;
		isStartPositioned = false;

		// Reset angle
		angle = 0;
	}

	/**
	 * Gets the players x-coordinate in world coordinates
	 * 
	 * @return x-coordinate of the player in world coordinates
	 */
	public float getX() {
		return xWorld;
	}

	/**
	 * Gets the players y-coordinate in world coordinates
	 * 
	 * @return y-coordinate of the player in world coordinates
	 */
	public float getY() {
		return yWorld;
	}

	/**
	 * Gets the radius of the player
	 * 
	 * @return radius of the player
	 */
	public float getRadius() {
		return radius;
	}

	/**
	 * Gets the angle of the player, in radians
	 * 
	 * @return angle of the player, in radians
	 */
	public float getAngle() {
		return angle;
	}

	/**
	 * Sets the position for the player.
	 * 
	 * @param x
	 *            new x-position
	 * @param y
	 *            new y-position
	 */
	public void setPosition(float x, float y) {
		xWorld = x;
		yWorld = y;
	}

	/**
	 * Sets the angle of the player, in radians.
	 * 
	 * @param angle
	 *            the new angle
	 */
	public void setAngle(float angle) {
		this.angle = angle;
	}

	/**
	 * Returns whether player is ready for launch or not.
	 * 
	 * @return <code>true</code> if player to be launched
	 */
	public boolean isReadyForLaunch() {
		return isReadyForLaunch;
	}

	/**
	 * This method gets called once the player has ended up somewhere.
	 */
	public void onStop() {
		System.out.println("I stopped!");
		isReadyForLaunch = true;
	}

	/**
	 * Marks the player as not ready for launch.
	 */
	public void setLaunched() {
		isReadyForLaunch = false;
	}

	/**
	 * Gets the current happiness of the player.
	 * 
	 * @return happiness of the player
	 */
	public float getHappiness() {
		return happiness;
	}

	/**
	 * Changes the happiness of the player to the given value. Automatically
	 * maps the value to -1.0f ... 1.0f as those are the limits.
	 * 
	 * @param newHappiness
	 *            the happiness to set player to
	 */
	public void setHappiness(float newHappiness) {
		if (newHappiness < -1.0f) {
			newHappiness = -1.0f;
		}
		else if (newHappiness > 1.0f) {
			newHappiness = 1.0f;
		}
		happiness = newHappiness;
	}

	/**
	 * Helper to check if the player is inside the given rectangular tile map
	 * object / area.
	 * 
	 * @param area
	 *            the TileMapObject representing an area.
	 * @return <code>true</code> if the player is inside the specified area
	 */
	public boolean isInsideArea(TileMapObject area) {
		return Utils.isPointInsideRect(this.xWorld, this.yWorld, area.x,
				area.y, area.width, area.height);
	}

	/**
	 * Gets if the player has been positioned and is ready for action.
	 * 
	 * @return if player has a start position set
	 */
	public boolean isStartPositioned() {
		return isStartPositioned;
	}

	/**
	 * Sets the player as positioned for start stuff.
	 */
	public void setStartPositioned() {
		isStartPositioned = true;
		isReadyForLaunch = true;
	}

}
