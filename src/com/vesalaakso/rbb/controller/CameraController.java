package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.Camera;

/**
 * This class handles the controlling of {@link Camera}.
 */
public class CameraController extends KeyAdapter implements Updateable {

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/**
	 * Constructs a new CameraController.
	 */
	public CameraController() {
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
		// Move camera 100px / sec in both directions
		Camera.get().translate(cameraMoveX * delta * .1f,
				cameraMoveY * delta * .1f);
	}

}
