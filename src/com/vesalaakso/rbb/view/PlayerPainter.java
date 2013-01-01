package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;

/**
 * Handles the drawing of the Player.
 * 
 * @author Vesa Laakso
 */
public class PlayerPainter implements Painter {

	/** The Player this instance will draw. */
	private Player player;

	/**
	 * Constructs a new <code>PlayerPainter</code> and associates it with the
	 * given {@link Player}.
	 * 
	 * @param player
	 *            the <code>Player</code> this instance will draw.
	 */
	public PlayerPainter(Player player) {
		this.player = player;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>true</code>, player is always drawn to world coordinates.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return true;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(org.newdawn.slick.Graphics,
	 * com.vesalaakso.rbb.model.Camera)
	 */
	@Override
	public void paint(Graphics g, Camera cam) {
		// Calculate top left coords based on the center coordinates of the
		// player.
		float x = player.getX() - player.getRadius();
		float y = player.getY() - player.getRadius();

		// And the width as well
		float width = player.getRadius() * 2;

		g.fillOval(x, y, width, width);
	}
}
