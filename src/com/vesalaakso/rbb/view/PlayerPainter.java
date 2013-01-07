package com.vesalaakso.rbb.view;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Player;

/**
 * Handles the drawing of the Player.
 * 
 * @author Vesa Laakso
 */
public class PlayerPainter implements Painter {

	/** The Player this instance will draw. */
	private Player player;

	/** The Color used to draw player. */
	private final Color playerColor;

	/** The Color used to draw the outline of player */
	private final Color playerOutlineColor;

	/** The Color used to draw the outline of players eyes */
	private final Color eyeOutlineColor;

	/** The Color used to draw the insides of players eyes */
	private final Color eyeInsideColor;

	/** The Color used to draw the pupil of players eyes */
	private final Color eyePupilColor;

	/**
	 * Constructs a new <code>PlayerPainter</code> and associates it with the
	 * given {@link Player}.
	 * 
	 * @param player
	 *            the <code>Player</code> this instance will draw.
	 */
	public PlayerPainter(Player player) {
		this.player = player;

		// Colors built from this.
		java.awt.Color c;

		// Player inside color
		c = java.awt.Color.getHSBColor(0.75f, 0.5f, 0.5f);
		playerColor = new Color(c.getRed(), c.getGreen(), c.getBlue());

		// Player outline color
		c = java.awt.Color.getHSBColor(0.75f, 0.5f, 0.85f);
		playerOutlineColor = new Color(c.getRed(), c.getGreen(), c.getBlue());

		// Eye outline color
		c = java.awt.Color.getHSBColor(0f, 0.0f, 0.05f);
		eyeOutlineColor = new Color(c.getRed(), c.getGreen(), c.getBlue());

		// Eye inside color
		c = java.awt.Color.getHSBColor(0f, 0.0f, 0.85f);
		eyeInsideColor = new Color(c.getRed(), c.getGreen(), c.getBlue());

		// Eye pupil color
		c = java.awt.Color.getHSBColor(0f, 0.0f, 0.05f);
		eyePupilColor = new Color(c.getRed(), c.getGreen(), c.getBlue());
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
	 *      com.vesalaakso.rbb.model.Camera)
	 */
	@Override
	public void paint(Graphics g) {
		// Calculate top left coords based on the center coordinates of the
		// player.
		float x = player.getX() - player.getRadius();
		float y = player.getY() - player.getRadius();

		// And the width as well
		float width = player.getRadius() * 2;

		// Draw a circle representing the player
		g.setColor(playerColor);
		g.fillOval(x, y, width, width);

		// Outline of player
		g.setColor(playerOutlineColor);
		g.drawOval(x, y, width, width);

		// EYES!
		drawEyes(g);
	}

	/** A helper to draw eyes. */
	private void drawEyes(Graphics g) {
		// And before drawing, center the translation to player coordinates and
		// rotate the transformations
		float rotation = (float) Math.toDegrees(player.getAngle());
		g.translate(player.getX(), player.getY());
		g.rotate(0, 0, rotation);

		// EYES!
		float xOffset = player.getRadius() * .7f;
		float width = xOffset * .9f;
		float height = player.getRadius() * .85f;
		float y = -height * .85f;
		float pupilWidth = width * .5f;
		float pupilHeight = pupilWidth * 1.15f;
		float pupilXOffset = xOffset - (width + pupilWidth) * .25f;
		float pupilY = y + (height + pupilHeight) * .25f;

		// Inside first
		g.setColor(eyeInsideColor);
		g.fillOval(-xOffset, y, width, height);
		g.fillOval(xOffset - width, y, width, height);

		// Then pupils
		g.setColor(eyePupilColor);
		g.fillOval(-pupilXOffset, pupilY, pupilWidth, pupilHeight);
		g.fillOval(pupilXOffset - pupilWidth, pupilY, pupilWidth, pupilHeight);

		// Then outline
		g.setColor(eyeOutlineColor);
		g.drawOval(-xOffset, y, width, height);
		g.drawOval(xOffset - width, y, width, height);

		// Reset transformations
		g.rotate(0, 0, -rotation);
		g.translate(-player.getX(), -player.getY());
	}
}
