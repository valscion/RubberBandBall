package com.vesalaakso.rbb.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.ResourceManager;

/**
 * A class used to load the resources and display a nice string telling what is
 * happening.
 * 
 * @author Vesa Laakso
 */
public class LoadState extends BasicGameState {

	/** The resource manager to initialize */
	private ResourceManager resourceManager;

	/** True if the initial "please wait" string has been rendered. */
	private boolean statusRendered;

	/**
	 * Constructs the load state and associates it with the given resource
	 * manager.
	 * 
	 * @param resourceManager
	 *            the resource manager to load when first rendered.
	 */
	public LoadState(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// NO-OP
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		String loadStr = "Please wait, loading resources...";

		float textWidth = g.getFont().getWidth(loadStr);

		float centerX = (RubberBandBall.SCREEN_WIDTH - textWidth) / 2;

		g.drawString(loadStr, centerX, 200);
		statusRendered = true;
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (statusRendered) {
			// Initialize resource manager after the "Please wait" text has been
			// rendered.
			resourceManager.init();

			// Then change to the main menu state.
			game.enterState(State.MAIN_MENU.ordinal());
		}
	}

	@Override
	public int getID() {
		return State.LOAD.ordinal();
	}

}
