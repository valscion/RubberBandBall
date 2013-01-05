package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;
import org.newdawn.slick.MouseListener;


/**
 * An adapter class for empty MouseListener methods, in order not to clutter
 * all the mouse listening controllers.
 * 
 * @author Vesa Laakso
 */
public class MouseAdapter implements MouseListener {

	@Override
	public void setInput(Input input) {
	}

	@Override
	public boolean isAcceptingInput() {
		return true;
	}

	@Override
	public void inputEnded() {
	}

	@Override
	public void inputStarted() {
	}

	@Override
	public void mouseWheelMoved(int change) {
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
	}

	@Override
	public void mousePressed(int button, int x, int y) {
	}

	@Override
	public void mouseReleased(int button, int x, int y) {
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
	}

}
