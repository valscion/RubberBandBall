package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;

import com.vesalaakso.rbb.model.RubberBand;

/**
 * Controls the rubber band which will be pulled from player.
 * 
 * @author Vesa Laakso
 */
public class RubberBandController extends MouseAdapter {

	/** <code>RubberBand</code> to control. */
	private RubberBand rubberBand;

	/**
	 * Constructs the <code>RubberBandController</code> and associates it with
	 * the given <code>RubberBand</code> model.
	 * 
	 * @param rubberBand
	 *            the {@link RubberBand} to control
	 */
	public RubberBandController(RubberBand rubberBand) {
		this.rubberBand = rubberBand;
	}

	@Override
	public void mousePressed(int button, int x, int y) {
		if (button == Input.MOUSE_LEFT_BUTTON) {
			rubberBand.startPull(x, y);
		}
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
		if (rubberBand.isPulled() && button == Input.MOUSE_LEFT_BUTTON) {
			rubberBand.endPull(x, y);
		}
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		if (rubberBand.isPulled()) {
			rubberBand.pull(newx, newy);
		}
	}
}
