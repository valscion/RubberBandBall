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

	/**
	 * The speed how fast the camera will be moved when mouse enters the edges
	 * of screen.
	 */
	private static final float CAMERA_MOVE_SPEED = 15.0f;

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/** The scale to try to move the camera to */
	private float cameraTargetScale = 1.0f;

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

	@Override
	public void mouseWheelMoved(int change) {
		if (change < 0) {
			cameraTargetScale *= .75f;
			if (cameraTargetScale < Camera.MIN_SCALING) {
				cameraTargetScale = Camera.MIN_SCALING;
			}
		}
		else {
			cameraTargetScale *= 1.25f;
		}
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
			cameraMoveX = (1.0f / xDist) * CAMERA_MOVE_SPEED;
		}
		else if (xDist > 0) {
			xDist = Utils.clamp(xDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveX = -(1.0f / xDist) * CAMERA_MOVE_SPEED;
		}
		else {
			cameraMoveX = 0;
		}
		if (yDist < 0) {
			yDist = Utils.clamp(-yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = (1.0f / yDist) * CAMERA_MOVE_SPEED;
		}
		else if (yDist > 0) {
			yDist = Utils.clamp(yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = -(1.0f / yDist) * CAMERA_MOVE_SPEED;
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

		// If scaling target differs enough from the current scale, scale.
		float camScale = cam.getScaling();
		if (Math.abs(camScale - cameraTargetScale) > 1e-5f) {
			float newScale = Utils.curveValue(camScale, cameraTargetScale, 20);
			cam.setScaling(newScale);
		}

		// If the player is in a state that he is ready to be launched or hasn't
		// been positioned for start yet, move the camera by the speed specified
		// with mouse position
		if (player.isReadyForLaunch() || !player.isStartPositioned()) {
			cam.translate(cameraMoveX * delta, cameraMoveY * delta);
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
