package com.vesalaakso.rbb.states;

/**
 * An enum for identifying different states and assigning them IDs.
 * 
 * @author Vesa Laakso
 */
public enum State {
	/** The state handling all the main game logic */
	GAME,
	/** The state for main menu */
	MAIN_MENU,
	/** The state for options menu */
	OPTIONS_MENU,
	/** A state handling map transition. */
	MAP_CHANGE,
	/** A state handling resource loading */
	LOAD;
}
