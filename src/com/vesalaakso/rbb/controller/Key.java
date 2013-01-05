package com.vesalaakso.rbb.controller;

import org.newdawn.slick.Input;

/**
 * This enumeration is used to map the int values of KeyListener methods to sane
 * constants.
 * 
 * @author Vesa Laakso
 * 
 */
public enum Key {
	/** Camera movement in negative x-direction, default key: A */
	CAMERA_MOVE_LEFT(Input.KEY_A),
	/** Camera movement in positive x-direction, default key: D */
	CAMERA_MOVE_RIGHT(Input.KEY_D),
	/** Camera movement in negative y-direction, default key: W */
	CAMERA_MOVE_UP(Input.KEY_W),
	/** Camera movement in positive y-direction, default key: S */
	CAMERA_MOVE_DOWN(Input.KEY_S);

	/** The Input key constant mapped to the enum */
	public final int id;

	/**
	 * Constructs an enum and associates it with the given ID.
	 */
	private Key(int keyID) {
		this.id = keyID;
	}

	/**
	 * Finds and returns the enum representing the given integer. Returns
	 * <code>null</code> if none was found.
	 * 
	 * @param keyID
	 *            the integer used to find the correct enum
	 * @return the <code>Key</code> with the given id or <code>null</code> if
	 *         none was found.
	 */
	public static Key findWithId(int keyID) {
		Key[] keys = Key.values();
		for (int i = 0; i < keys.length; i++) {
			Key key = keys[i];
			if (key.id == keyID) {
				return key;
			}
		}
		// If we got here, no key was found.
		return null;
	}
}
