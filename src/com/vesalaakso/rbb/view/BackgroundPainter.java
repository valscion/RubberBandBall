package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Background;
import com.vesalaakso.rbb.model.Camera;

/**
 * Draws the bg space
 * 
 * @author Vesa Laakso
 */
public class BackgroundPainter implements Painter {

	/** The Background representing the bg (o'rly). */
	private Background bg;

	/**
	 * Constructs this <code>Painter</code> and associates it with the given
	 * <code>Background</code> model like a good MVC-specific class should do.
	 * 
	 * @param bg
	 *            the <code>Background</code> representing the thing one might
	 *            expect.
	 */
	public BackgroundPainter(Background bg) {
		this.bg = bg;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>false</code>, as the coordinations are calculated nicely by
	 *         hand.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return false;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(org.newdawn.slick.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		// Nope, we don't want scaling to affect this drawing.
		float scale = Camera.get().getScaling();
		g.scale(1 / scale, 1 / scale);

		g.drawImage(bg.getImage(), bg.getX(), bg.getY());

		// Reset scaling
		g.scale(scale, scale);
	}

}
