package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;

/**
 * Classes that implement this interface will handle drawing of every single
 * thing in the game.
 * 
 * @author Vesa Laakso
 */
interface Painter {

	/**
	 * This method tells the {@link PainterContainer} class whether painting
	 * should go to world coordinates. Screen coordinates are precise and to
	 * accommodate {@link Camera} movements, the location of the
	 * <code>Camera</code> must be consulted. Drawing in world coordinates can
	 * always happen to the same place, the painting coordinates are
	 * automatically calculated by the software.
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
