package com.vesalaakso.rbb.view;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.states.GameState;
import com.vesalaakso.rbb.util.Utils;

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

	/** Everything that is to be painted in debug mode */
	private List<Painter> debugPainters = new LinkedList<Painter>();

	/** The resource manager to use when needing to query for resources. */
	private ResourceManager resManager;

	/**
	 * Constructs the painter container and associates it with the given
	 * resource manager. The resource manager will be passed on to all painters
	 * later on, when <code>paint()</code> method is called for all
	 * {@link Painter}s.
	 * 
	 * @param resManager
	 *            the resource manager to use when painters need to get a
	 *            resource, such as a loaded font
	 */
	public PainterContainer(ResourceManager resManager) {
		this.resManager = resManager;
	}

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
	 * Adds a painter to be drawn only when the current game is in debug mode.
	 * All debug painters are drawn after the real ones.
	 * 
	 * @param painter
	 *            the painter to draw in debug mode
	 */
	public void addDebugPainter(Painter painter) {
		this.debugPainters.add(painter);
	}

	/**
	 * Draws every painter in the order they were added.
	 * 
	 * @param g
	 *            <code>Graphics</code>-object which will be used to draw stuff
	 *            to screen.
	 * @param game
	 *            the game to draw
	 */
	public void paintAll(Graphics g, GameState game) {
		// Anti-aliasing for all!
		g.setAntiAlias(true);

		// Camera, to a prettier variable.
		Camera cam = Camera.get();

		// Game scale is set by camera.
		float scaling = cam.getScaling();

		// All of the painters to be drawn. If we're not in debug mode,
		// allPainters is the same as regular painters. If we are in debug mode,
		// then allPainters is the combination of this.painters and
		// this.debugPainters.
		List<Painter> allPainters;
		if (game.isDebugModeToggled()) {
			allPainters = new LinkedList<Painter>();
			allPainters.addAll(painters);
			allPainters.addAll(debugPainters);
		}
		else {
			allPainters = painters;
		}

		// Flag which controls whether world translation has been enabled or
		// not.
		boolean isWorldTranslationOn = false;

		for (Painter p : allPainters) {
			if (p.isDrawnToWorldCoordinates() != isWorldTranslationOn) {
				// We wanted translation to world coordinates or back to screen
				// coordinates. That translation is always based on the fact
				// that camera is always at the center of the screen. We can
				// translate by looking for the screen (0, 0) coordinates in
				// world space and then translating to or out of that coordinate
				// space.

				float transX = Utils.screenToWorldX(0);
				float transY = Utils.screenToWorldY(0);

				// Flip the flag.
				isWorldTranslationOn = !isWorldTranslationOn;

				if (!isWorldTranslationOn) {
					// We wanted to go back to regular drawing mode.
					g.popTransform();
					g.setLineWidth(1);
				}
				else {
					// Translate to world coordinates
					g.pushTransform();
					g.scale(scaling, scaling);
					g.translate(-transX, -transY);
					g.setLineWidth(scaling);
				}
			}

			// Store the color before drawing this Painter
			Color origColor = g.getColor();

			// Paint.
			p.paint(g, resManager);

			// Restore color, if needed.
			if (g.getColor().equals(origColor) == false) {
				g.setColor(origColor);
			}
		}

		// Clear transformation stack, if needed
		if (isWorldTranslationOn) {
			g.popTransform();
		}

		// Reset all transformations to normal
		g.resetTransform();
	}

}
