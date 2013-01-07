package com.vesalaakso.rbb.model;

import com.vesalaakso.rbb.controller.MapChangeListener;


/**
 * Represents the ball player moves.
 * 
 * @author Vesa Laakso
 */
public class Player implements MapChangeListener {

	/** The map <code>Player</code> belongs to. */
	private TileMap map;

	/** Center x-coordinate of the player. */
	private float xWorld;

	/** Center y-coordinate of the player. */
	private float yWorld;

	/** Radius of the ball representing the player. */
	private float radius = 16f;

	/** Is player ready to be launched or not */
	private boolean isReadyForLaunch;

	/**
	 * Constructs the <code>Player</code>. The new Player is pretty much useless
	 * and dangerous before it has a map set, so it should not be used before
	 * the {@link #onMapChange(TileMap, TileMap)} method is called.
	 */
	public Player() {
	}

	/** A helper method to reset the position and status of the player. */
	private void reset() {
		// Set the player to the center of the spawn area
		TileMapObject spawn = map.getSpawnArea();
		xWorld = spawn.x + spawn.width * .5f;
		yWorld = spawn.y + spawn.height * .5f;

		// Make player ready to be launched
		isReadyForLaunch = true;

		// Set the camera to the players coordinates, too.
		Camera.get().setPosition(xWorld, yWorld);
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
	 * Resets the state of the player when the map is changed.
	 * @see MapChangeListener#onMapChange(TileMap, TileMap)
	 */
	@Override
	public void onMapChange(TileMap oldMap, TileMap newMap) {
		map = newMap;
		reset();
	}

}
