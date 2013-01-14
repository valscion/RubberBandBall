package com.vesalaakso.rbb.view;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.ResourceManager;

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
	 * @see com.vesalaakso.rbb.view.Painter#paint(Graphics, ResourceManager)
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		// Before drawing, center the drawing to player coordinates and apply
		// player rotation, too
		float rotation = (float) Math.toDegrees(player.getAngle());
		g.translate(player.getX(), player.getY());
		g.rotate(0, 0, rotation);

		// Get the radius of the player to this variable r for easy reading.
		float r = player.getRadius();

		// Draw a circle representing the player
		g.setColor(playerColor);
		g.fillOval(-r, -r, r * 2, r * 2);

		// Outline of player
		g.setColor(playerOutlineColor);
		g.drawOval(-r, -r, r * 2, r * 2);

		// EYES!
		drawEyes(g);

		// And mouth!
		drawMouth(g);

		// Reset transformations
		g.rotate(0, 0, -rotation);
		g.translate(-player.getX(), -player.getY());
	}

	/** A helper to draw eyes. */
	private void drawEyes(Graphics g) {

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
	}

	/** A helper to draw mouth */
	private void drawMouth(Graphics g) {
		// Easy reading.
		float r = player.getRadius();

		// Happiness, a value from -1 to 1.
		float happiness = player.getHappiness();

		// Calculate the width and height of the arc based on the happiness
		float width = r;
		float height = r * 0.75f * happiness;

		// Height must be at least 1.5 to draw something.
		if (height > -1.5f && height < 1.5f) {
			if (height < 0) {
				height = -1.5f;
			}
			else {
				height = 1.5f;
			}
		}

		// Center of a full oval
		float cx = width / 2;
		float cy = (height - width) / 2;

		// Don't let it snap when close to 0
		if (height < 0) {
			cy -= 1.0f;
		}

		// When the mouth is smiley enough, its borders move up / down, too.
		if (height > r * .125f) {
			cy += (height / (r * .75f)) * r * .25f;
		}
		else if (height < -r * .125f) {
			cy += (height / (r * .75f)) * r * .125f;
		}

		g.translate(-cx, -cy);
		g.fillArc(0, 0, width, height, 0, 180);
		g.translate(cx, cy);
	}
}
