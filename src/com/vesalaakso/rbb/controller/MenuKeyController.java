package com.vesalaakso.rbb.controller;

import org.newdawn.slick.KeyListener;

import com.vesalaakso.rbb.states.GameState;

/**
 * Responds to the keyboard events used to move around in the menus and stop the
 * game.
 * 
 * @author Vesa Laakso
 */
public class MenuKeyController extends KeyAdapter implements KeyListener {

	/** Game to quit when the game is exited or reseting the level. */
	private GameState game;
	
	/**
	 * Constructs a new controller for controlling the menus and exiting the
	 * game when necessary.
	 * 
	 * @param game the game to control
	 */
	public MenuKeyController(GameState game) {
		this.game = game;
	}

	@Override
	public void keyPressed(Key key, char c) {
		if (key == Key.GO_BACK) {
			game.stop();
		}
		else if (key == Key.RESET_LEVEL) {
			game.resetLevel();
		}
	}
}
