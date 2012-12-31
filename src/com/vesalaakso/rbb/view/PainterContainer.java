package com.vesalaakso.rbb.view;

import java.util.LinkedList;
import java.util.List;

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
	List<Painter> painters = new LinkedList<Painter>();

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
	 * @param cam
	 *            a <code>Camera</code> that can be consulted to get information
	 *            about the location one will draw stuff to
	 */
	public void paintAll(Graphics g, Camera cam) {
		for (Painter p : this.painters) {
			p.paint(g, cam);
		}
	}
}
