package com.vesalaakso.rbb.controller;

import java.util.List;

import org.newdawn.slick.command.BasicCommand;

import com.google.common.collect.ImmutableList;
import com.vesalaakso.rbb.model.Camera;

/**
 * This class handles the controlling of {@link Camera}.
 */
public class CameraController implements Controller {

	/** All the commands this class would like to handle. */
	private static final List<Command> commands =
			ImmutableList.of(
					Command.CAMERA_MOVE_LEFT,
					Command.CAMERA_MOVE_RIGHT,
					Command.CAMERA_MOVE_UP,
					Command.CAMERA_MOVE_DOWN);

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/**
	 * Constructs a new CameraController.
	 */
	public CameraController() {
	}

	/**
	 * @see org.newdawn.slick.command.InputProviderListener#controlPressed(org.newdawn.slick.command.Command)
	 */
	@Override
	public void controlPressed(org.newdawn.slick.command.Command pCommand) {
		Command cmd = Command.valueOfCommand((BasicCommand) pCommand);

		switch (cmd) {
			case CAMERA_MOVE_LEFT: cameraMoveX = -1.0f; break;
			case CAMERA_MOVE_RIGHT: cameraMoveX = 1.0f; break;
			case CAMERA_MOVE_UP: cameraMoveY = -1.0f; break;
			case CAMERA_MOVE_DOWN: cameraMoveY = 1.0f; break;
		}
	}

	/**
	 * @see org.newdawn.slick.command.InputProviderListener#controlReleased(org.newdawn.slick.command.Command)
	 */
	@Override
	public void controlReleased(org.newdawn.slick.command.Command pCommand) {
		Command cmd = Command.valueOfCommand((BasicCommand) pCommand);

		switch (cmd) {
			case CAMERA_MOVE_LEFT: // Falls below
			case CAMERA_MOVE_RIGHT: cameraMoveX = 0; break;
			case CAMERA_MOVE_UP: // Falls below
			case CAMERA_MOVE_DOWN: cameraMoveY = 0; break;
		}
	}

	/**
	 * @see Controller#getCommands()
	 */
	@Override
	public List<Command> getCommands() {
		return commands;
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
