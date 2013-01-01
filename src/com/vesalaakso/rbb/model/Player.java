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
	private float radius;

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
		
		// Find the center of the start point area
		//map.getTiledMap().
	}

}
