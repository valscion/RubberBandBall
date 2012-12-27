package com.vesalaakso.rbb.painters;

import org.newdawn.slick.Graphics;

/**
 * Subclasses of this class will handle drawing of every single thing in the
 * game.
 * 
 * @author Vesa Laakso
 */
public abstract class AbstractPainter {

	/**
	 * This method tells the {@link PainterContainer} class whether painting
	 * should go to world coordinates.
	 * 
	 * TODO: @see tag to world coordinate description
	 * 
	 * @return <code>true</code>, if painting should be done to world
	 *         coordinates, <code>false</code> otherwise.
	 */
	abstract public boolean isDrawnToWorldCoordinates();

	/**
	 * Painting happens inside this method.
	 * 
	 * @param g
	 *            <code>Graphics</code>-object which will be used to draw stuff
	 *            to screen.
	 */
	abstract public void paint(Graphics g);
}
