package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;

/**
 * An adapter class for easier and prettier <code>KeyListener</code>
 * implementation.
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

	/**
	 * Notification that a key was pressed. If this method is not overridden, it
	 * will default to call {@link #keyPressed(Key, char)} method.
	 * 
	 * @see org.newdawn.slick.KeyListener#keyPressed(int, char)
	 */
	@Override
	public void keyPressed(int key, char c) {
		// Useful to quickly turn the key into a proper enum by default
		Key keyEnum = Key.findWithId(key);
		if (keyEnum == null) {
			return;
		}
		keyPressed(keyEnum, c);
	}

	/**
	 * Notification that a special, enumerated key was pressed.
	 * 
	 * @param key
	 *            the <code>Key</code> the pressed key maps to
	 * @param c
	 *            the character of the key that was pressed, if the key has a
	 *            visible character.
	 */
	public void keyPressed(Key key, char c) {
	}

	/**
	 * Notification that a key was released. If this method is not overridden,
	 * it will default to call {@link #keyReleased(Key, char)} method.
	 * 
	 * @see org.newdawn.slick.KeyListener#keyReleased(int, char)
	 */
	@Override
	public void keyReleased(int key, char c) {
		// Useful to quickly turn the key into a proper enum by default
		Key keyEnum = Key.findWithId(key);
		if (keyEnum == null) {
			return;
		}
		keyReleased(keyEnum, c);
	}

	/**
	 * Notification that a special, enumerated key was released.
	 * 
	 * @param key
	 *            the <code>Key</code> the released key maps to
	 * @param c
	 *            the character of the key that was pressed, if the key has a
	 *            visible character.
	 */
	public void keyReleased(Key key, char c) {
	}

}
