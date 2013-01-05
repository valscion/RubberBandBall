package com.vesalaakso.rbb.controller;

/**
 * Any class which would like to act with update()-method should implement this
 * interface.
 * 
 * @author Vesa Laakso
 */
public interface Updateable {

	/**
	 * Called upon every update()-call in the game loop.
	 * 
	 * @param delta
	 *            The amount of time thats passed since last update in
	 *            milliseconds
	 * 
	 * @see org.newdawn.slick.BasicGame.update
	 */
	void update(int delta);
}
