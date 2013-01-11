package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.Vector;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.util.Utils;

/**
 * This class handles the controlling of {@link Camera}.
 */
public class CameraController extends MouseAdapter implements Updateable {

	/** If camera enters this border area from the screen, it will move. */
	private static final int BORDER_MAX_DIST = 50;

	/** This is the closest to the border mouse can get to gain full speed */
	private static final int BORDER_MIN_DIST = 10;

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/** The Player the camera will follow. */
	private Player player;

	/**
	 * Constructs a new CameraController and associates it with the given
	 * <code>Player</code>.
	 * 
	 * @param player
	 *            the <code>Player</code> this controller will make the Camera
	 *            follow.
	 */
	public CameraController(Player player) {
		this.player = player;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		int scrW = RubberBandBall.SCREEN_WIDTH;
		int scrH = RubberBandBall.SCREEN_HEIGHT;

		int xDist = 0, yDist = 0;

		if (newx - BORDER_MAX_DIST < 0) {
			xDist = newx;
		}
		else if (newx + BORDER_MAX_DIST > scrW) {
			xDist = newx - scrW;
		}
		if (newy - BORDER_MAX_DIST < 0) {
			yDist = newy;
		}
		else if (newy + BORDER_MAX_DIST > scrH) {
			yDist = newy - scrH;
		}
		moveCamera(xDist, yDist);
	}

	/**
	 * Handles moving camera based on the given values
	 * 
	 * @param xDist
	 *            mouse x-distance from screen edge
	 * @param yDist
	 *            mouse y-distance from screen edge
	 */
	private void moveCamera(int xDist, int yDist) {
		if (xDist < 0) {
			xDist = Utils.clamp(-xDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveX = (1.0f / xDist) * 15.0f;
		}
		else if (xDist > 0) {
			xDist = Utils.clamp(xDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveX = -(1.0f / xDist) * 15.0f;
		}
		else {
			cameraMoveX = 0;
		}
		if (yDist < 0) {
			yDist = Utils.clamp(-yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = (1.0f / yDist) * 15.0f;
		}
		else if (yDist > 0) {
			yDist = Utils.clamp(yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = -(1.0f / yDist) * 15.0f;
		}
		else {
			cameraMoveY = 0;
		}
	}

	/**
	 * @see Controller#update(int)
	 */
	@Override
	public void update(int delta) {
		Camera cam = Camera.get();

		// If the player is in a state that he is ready to be launched or hasn't
		// been positioned for start yet, move the camera 500px / sec as keys
		// are pressed.
		if (player.isReadyForLaunch() || !player.isStartPositioned()) {
			cam.translate(cameraMoveX * delta * .5f, cameraMoveY * delta * .5f);
		}
		else {
			// Glue the camera to the player if one is not ready for launch but
			// has a start position set.
			Vector oldP = new Vector(cam.getX(), cam.getY());
			Vector targetP = new Vector(player.getX(), player.getY());
			Vector curved = Utils.curvePoints(oldP, targetP, 20);

			cam.setPosition(curved.x, curved.y);
		}
	}

}
