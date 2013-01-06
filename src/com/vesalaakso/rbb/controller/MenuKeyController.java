package com.vesalaakso.rbb.controller;

import org.newdawn.slick.KeyListener;

import com.vesalaakso.rbb.RubberBandBall;

/**
 * Responds to the keyboard events used to move around in the menus and stop the
 * game.
 * 
 * @author Vesa Laakso
 */
public class MenuKeyController extends KeyAdapter implements KeyListener {

	/** Game to quit when the game is exited. */
	private RubberBandBall game;
	
	/**
	 * Constructs a new controller for controlling the menus and exiting the
	 * game when necessary.
	 * 
	 * @param game the game to control
	 */
	public MenuKeyController(RubberBandBall game) {
		this.game = game;
	}

	@Override
	public void keyPressed(int keyId, char c) {
		Key key = Key.findWithId(keyId);
		if (key == null) {
			return;
		}
		
		if (key == Key.GO_BACK) {
			game.stop();
		}
	}
}
