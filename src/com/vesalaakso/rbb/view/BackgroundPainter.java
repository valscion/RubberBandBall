package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.resources.Gfx;

/**
 * Draws the bg space
 * 
 * @author Vesa Laakso
 */
public class BackgroundPainter implements Painter {

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>false</code>, as the coordinations are calculated nicely by
	 *         hand.
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
		// Get the bg image
		Image bgImg = resManager.getImage(Gfx.BACKGROUND_GAME);

		// Calculate coordinates based on camera coordinates
		float x = Camera.get().getX() * -.1f;
		float y = Camera.get().getY() * -.1f;

		g.drawImage(bgImg, x, y);
	}

}
