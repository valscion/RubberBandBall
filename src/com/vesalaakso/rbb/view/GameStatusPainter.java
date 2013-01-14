package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Font;
import com.vesalaakso.rbb.model.GameStatus;
import com.vesalaakso.rbb.model.ResourceManager;

/**
 * A painter responsible for drawing the game status.
 * 
 * @author Vesa Laakso
 */
public class GameStatusPainter implements Painter {

	/** The GameStatus to paint */
	private GameStatus gameStatus;

	/**
	 * Constructs a new painter for drawing the game status, which is given as a
	 * parameter.
	 * 
	 * @param gameStatus
	 *            the game status to draw
	 */
	public GameStatusPainter(GameStatus gameStatus) {
		this.gameStatus = gameStatus;
	}

	/**
	 * @return <code>false</code>, as game state is drawn at a static position
	 *         in screen
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return false;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(Graphics, ResourceManager)
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		// Reset transforms for a while.
		g.pushTransform();
		g.resetTransform();

		g.setFont(resManager.getFont(Font.SCORE));

		float x = RubberBandBall.SCREEN_WIDTH - 200;
		float y1 = RubberBandBall.SCREEN_HEIGHT - 50;
		float y2 = y1 + 20;

		String shotsStr = "Shots: " + gameStatus.getCurrentShotCount();
		String triesStr = "Tries: " + gameStatus.getCurrentTryCount();
		g.drawString(shotsStr, x, y1);
		g.drawString(triesStr, x, y2);

		// Reset the old transforms
		g.popTransform();
	}
}
