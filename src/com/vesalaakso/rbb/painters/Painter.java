package com.vesalaakso.rbb.painters;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.game.Camera;

/**
 * Classes that implement this interface will handle drawing of every single
 * thing in the game.
 * 
 * @author Vesa Laakso
 */
interface Painter {

	/**
	 * This method tells the {@link PainterContainer} class whether painting
	 * should go to world coordinates.
	 * 
	 * TODO: @see tag to world coordinate description
	 * 
	 * @return <code>true</code>, if painting should be done to world
	 *         coordinates, <code>false</code> otherwise.
	 */
	public boolean isDrawnToWorldCoordinates();

	/**
	 * Painting happens inside this method.
	 * 
	 * @param g
	 *            <code>Graphics</code>-object which will be used to draw stuff
	 *            to screen.
	 * @param cam
	 *            a <code>Camera</code> that can be consulted to get information
	 *            about the location one will draw stuff to
	 */
	public void paint(Graphics g, Camera cam);
}
