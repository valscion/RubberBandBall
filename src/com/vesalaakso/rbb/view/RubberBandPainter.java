package com.vesalaakso.rbb.view;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.geom.Vector2f;

import com.vesalaakso.rbb.model.RubberBand;

/**
 * Class responsible for drawing the <code>RubberBand</code>.
 * 
 * @author Vesa Laakso
 */
public class RubberBandPainter implements Painter {

	/** The RubberBand to draw. */
	private RubberBand rubberBand;

	/**
	 * Constructs a new RubberBandPainter and associates it with the given
	 * <code>RubberBand</code>.
	 * 
	 * @param rubberBand
	 *            the {@link RubberBand} to associate this Painter with.
	 */
	public RubberBandPainter(RubberBand rubberBand) {
		this.rubberBand = rubberBand;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>true</code>, since rubber band is drawn relative to the
	 *         players coordinates and player is drawn to world coordinates.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return true;
	}

	@Override
	public void paint(Graphics g) {
		if (!rubberBand.isPulled()) {
			// Don't paint the rubber band unless it's being pulled.
			return;
		}

		Vector2f p1 = rubberBand.getStartPoint();
		Vector2f p2 = rubberBand.getEndPoint();

		g.setColor(Color.pink);

		float oldLineWidth = g.getLineWidth();
		g.setLineWidth(4.5f);

		g.drawLine(p1.x, p1.y, p2.x, p2.y);

		g.setLineWidth(oldLineWidth);
	}

}
