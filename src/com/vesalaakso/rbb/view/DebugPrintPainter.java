package com.vesalaakso.rbb.view;

import org.lwjgl.input.Mouse;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.resources.Font;
import com.vesalaakso.rbb.util.Utils;

/**
 * A Painter to draw all sorts of useful debug prints.
 * 
 * @author Vesa Laakso
 */
public class DebugPrintPainter implements Painter {

	/** We want to draw some coordinates from physics world. */
	private Physics physics;

	/** Also the player would be nice to print coordinates and angle from */
	private Player player;

	/**
	 * Constructs a new painter for drawing debug info on the screen.
	 * 
	 * @param physics
	 *            the Physics world to draw information from
	 * @param player
	 *            the Player to visualize with text
	 */
	public DebugPrintPainter(Physics physics, Player player) {
		this.physics = physics;
		this.player = player;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>false</code>, as debug text is drawn fixed to the screen.
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
		// Player body
		Body<Circle> body = physics.getPlayerBody();
		// Camera
		Camera cam = Camera.get();

		// Mouse y flipped because of LWJGL and slick difference.
		int mouseY = RubberBandBall.SCREEN_HEIGHT - Mouse.getY();
		int mouseX = Mouse.getX();
		
		// Mouse in world coords
		float mouseWX = Utils.screenToWorldX(mouseX);
		float mouseWY = Utils.screenToWorldY(mouseY);

		// String array to loop through and draw
		String[] rows = {
				String.format("Player position: (%.1f; %.1f)",
						player.getX(), player.getY()),
				String.format("Player angle: %.1f",
						Math.toDegrees(player.getAngle())),
				String.format("Player velocity: (%.1f; %.1f)",
						(body != null ? body.getXVelocity() : 0),
						(body != null ? body.getYVelocity() : 0)),
				String.format("Player sleeping: %b", 
						(body != null ? body.isSleeping() : true)),
				String.format("Player happiness: %.2f", player.getHappiness()),
				"------",
				String.format("Camera position: (%.1f; %.1f)", cam.getX(),
						cam.getY()),
				String.format("Camera scale: %.3f", cam.getScaling()),
				String.format("Mouse position (screen): (%d, %d)",
						mouseX, mouseY),
				String.format("Mouse position (world): (%.1f; %.1f)",
						mouseWX, mouseWY)
		};

		// Set the font
		g.setFont(resManager.getFont(Font.REGULAR));

		// Loop through all strings.
		for (int i = 0; i < rows.length; i++) {
			g.drawString(rows[i], 10, 60 + i * 15);
		}
	}

}
