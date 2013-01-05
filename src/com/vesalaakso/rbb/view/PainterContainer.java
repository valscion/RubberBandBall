package com.vesalaakso.rbb.view;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;

/**
 * Every instance of classes that implement {@link Painter} interface are stored
 * in an instance of this class. Later on, this class will handle the calls to
 * draw everything.
 * 
 * @author Vesa Laakso
 */
public class PainterContainer {

	/** Everything that is to be painted. */
	private List<Painter> painters = new LinkedList<Painter>();

	/**
	 * If the drawing has been translated to world coordinates, this will be
	 * <code>true</code>.
	 */
	private boolean isWorldTranslationOn = false;

	/**
	 * Adds a new <code>Painter</code> to the end of the painters list. The
	 * order of which painters are added will also be the order of which they
	 * will be drawn - the first <code>Painter</code> will be drawn under every
	 * subsequent <code>Painter</code>.
	 * 
	 * @param painter
	 *            the object to be added to the painters list
	 */
	public void addPainter(Painter painter) {
		painters.add(painter);
	}

	/**
	 * Draws every painter in the order they were added.
	 * 
	 * @param g
	 *            <code>Graphics</code>-object which will be used to draw stuff
	 *            to screen.
	 */
	public void paintAll(Graphics g) {
		for (Painter p : this.painters) {
			if (p.isDrawnToWorldCoordinates() != isWorldTranslationOn) {
				if (isWorldTranslationOn) {
					// We wanted to go back to regular drawing mode.
					g.resetTransform();
				}
				else {
					// We wanted translation.
					Camera cam = Camera.get();
					g.translate(-cam.getX(), -cam.getY());
				}

				// Flip the flag.
				isWorldTranslationOn = !isWorldTranslationOn;
			}

			// Store the color before drawing this Painter
			Color origColor = g.getColor();

			// Paint.
			p.paint(g);

			// Restore color, if needed.
			if (g.getColor().equals(origColor) == false) {
				g.setColor(origColor);
			}
		}
	}
}
