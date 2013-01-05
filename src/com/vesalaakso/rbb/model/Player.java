package com.vesalaakso.rbb.model;


/**
 * Represents the ball player moves.
 * 
 * @author Vesa Laakso
 */
public class Player {

	/** The map <code>Player</code> belongs to. */
	private TileMap map;

	/** Center x-coordinate of the player. */
	private float xWorld;

	/** Center y-coordinate of the player. */
	private float yWorld;

	/** Radius of the ball representing the player. */
	private float radius = 16f;

	/**
	 * Constructs the <code>Player</code> and associates it with the given map.
	 * 
	 * @param map
	 *            the map to associate the new <code>Player</code> with
	 */
	public Player(TileMap map) {
		this.map = map;
	}

	/** Resets the position of the player. */
	public void resetPosition() {
		// Set the player to the center of the spawn area
		TileMapArea spawn = map.getSpawnArea();
		xWorld = spawn.x + spawn.width * .5f;
		yWorld = spawn.y + spawn.height * .5f;
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

}
