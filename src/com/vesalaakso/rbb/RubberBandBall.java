package com.vesalaakso.rbb;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.vesalaakso.rbb.states.GameState;
import com.vesalaakso.rbb.states.MainMenuState;

/**
 * The game. You lost it.
 */
public class RubberBandBall extends StateBasedGame {

	/** The width of the display to create */
	public static final int SCREEN_WIDTH = 800;

	/** The height of the display to create */
	public static final int SCREEN_HEIGHT = 600;

	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}

	/**
	 * Starting point for the game.
	 * 
	 * @param args
	 *            Command line parameters
	 */
	public static void main(String[] args) {
		try {
			AppGameContainer app = new AppGameContainer(new RubberBandBall());

			app.setDisplayMode(SCREEN_WIDTH, SCREEN_HEIGHT, false);
			// app.setTargetFrameRate(60);
			app.setVSync(true);
			app.start();
		}
		catch (SlickException e) {
			// So we didn't even manage to start the freaking game now, did we.
			e.printStackTrace();
		}
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(new MainMenuState());
		addState(new GameState());
	}

}
