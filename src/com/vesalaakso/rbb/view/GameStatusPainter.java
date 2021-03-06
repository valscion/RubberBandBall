package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.GameStatus;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.resources.Font;

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
		g.setFont(resManager.getFont(Font.SCORE));

		float x = RubberBandBall.SCREEN_WIDTH - 200;
		float y1 = RubberBandBall.SCREEN_HEIGHT - 80;
		float y2 = y1 + 20;
		float y3 = y2 + 20;

		String shotsStr = "Shots: " + gameStatus.getCurrentShotCount();
		String triesStr = "Tries: " + gameStatus.getCurrentTryCount();
		String bestStr = "Best: " + gameStatus.getCurrentBest();
		g.drawString(shotsStr, x, y1);
		g.drawString(triesStr, x, y2);

		// Font is different for teh best one
		g.setFont(resManager.getFont(Font.SCORE_BEST));
		g.drawString(bestStr, x, y3);
	}
}
