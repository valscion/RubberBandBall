package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 * An adapter class for empty KeyListener methods, in order not to clutter
 * all the key listening controllers.
 * 
 * @author Vesa Laakso
 */
public class KeyAdapter implements KeyListener {

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
	public void keyPressed(int key, char c) {
	}

	@Override
	public void keyReleased(int key, char c) {
	}

}
