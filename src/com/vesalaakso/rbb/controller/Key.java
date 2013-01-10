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
	/** Camera movement in negative x-direction, default key: LEFT */
	CAMERA_MOVE_LEFT(Input.KEY_LEFT),
	/** Camera movement in positive x-direction, default key: RIGHT */
	CAMERA_MOVE_RIGHT(Input.KEY_RIGHT),
	/** Camera movement in negative y-direction, default key: UP */
	CAMERA_MOVE_UP(Input.KEY_UP),
	/** Camera movement in positive y-direction, default key: DOWN */
	CAMERA_MOVE_DOWN(Input.KEY_DOWN),
	/** Goes back in a menu or opens pause menu, default key: ESCAPE */
	GO_BACK(Input.KEY_ESCAPE),
	/** Toggles debug mode, default key: D */
	TOGGLE_DEBUG(Input.KEY_D),
	/** Debug, adds player happiness. */
	DBG_PLAYER_HAPPINESS_ADD(Input.KEY_Z),
	/** Debug, substracts player happiness. */
	DBG_PLAYER_HAPPINESS_SUB(Input.KEY_X),
	/** Debug, scales the camera up. */
	DBG_SCALE_CAMERA_UP(Input.KEY_ADD),
	/** Debug, scales the camera down. */
	DBG_SCALE_CAMERA_DOWN(Input.KEY_SUBTRACT),
	/** Debug, changes the level. */
	DBG_CHANGE_LEVEL(Input.KEY_M);

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
