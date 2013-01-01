package com.vesalaakso.rbb.model;

import com.vesalaakso.rbb.RubberBandBall;

/**
 * Represents the ball player moves.
 * 
 * @author Vesa Laakso
 */
public class Player {

	/** The game <code>Player</code> belongs to. */
	private RubberBandBall game;

	/** Center x-coordinate of the player. */
	private float xWorld;

	/** Center y-coordinate of the player. */
	private float yWorld;

	/** Radius of the ball representing the player. */
	private float radius = 16f;

	/**
	 * Constructs the <code>Player</code> and associates it with the given game.
	 * 
	 * @param game
	 *            the game to associate the new <code>Player</code> with
	 */
	public Player(RubberBandBall game) {
		this.game = game;
		resetPosition();
	}

	/** Helper method to put the player in the starting coordinates. */
	private void resetPosition() {
		TileMap map = game.getMap();

		// Set the player to the center of the spawn area
		TileMapArea spawn = map.getSpawnArea();
		xWorld = spawn.x + (spawn.width - this.radius) * .5f;
		yWorld = spawn.x + (spawn.width - this.radius) * .5f;
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

}
