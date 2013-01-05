package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;

/**
 * This class handles the controlling of {@link Camera}.
 */
public class CameraController extends KeyAdapter implements Updateable {

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
	public void keyPressed(int keyId, char c) {
		Key key = Key.findWithId(keyId);
		if (key == null) {
			return;
		}

		switch (key) {
			case CAMERA_MOVE_LEFT: cameraMoveX = -1.0f; break;
			case CAMERA_MOVE_RIGHT: cameraMoveX = 1.0f; break;
			case CAMERA_MOVE_UP: cameraMoveY = -1.0f; break;
			case CAMERA_MOVE_DOWN: cameraMoveY = 1.0f; break;
		}
	}

	@Override
	public void keyReleased(int keyId, char c) {
		Key key = Key.findWithId(keyId);
		if (key == null) {
			return;
		}

		switch (key) {
			case CAMERA_MOVE_LEFT: // Falls below
			case CAMERA_MOVE_RIGHT: cameraMoveX = 0; break;
			case CAMERA_MOVE_UP: // Falls below
			case CAMERA_MOVE_DOWN: cameraMoveY = 0; break;
		}
	}

	/**
	 * @see Controller#update(int)
	 */
	@Override
	public void update(int delta) {
		Camera cam = Camera.get();

		// If the player is in a state that he is ready to be launched, allow
		// moving the camera 500px / sec as keys are pressed.
		if (player.isReadyForLaunch()) {
			cam.translate(cameraMoveX * delta * .5f, cameraMoveY * delta * .5f);
		}
		else {
			// Glue the camera to the player if player is not ready for launch.
			cam.setPosition(player.getX(), player.getY());
		}
	}

}
