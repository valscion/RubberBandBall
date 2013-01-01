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

	/** The <code>Camera</code> we will control. */
	private Camera camera;

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/**
	 * Constructs a new CameraController and associates the given {@link Camera}
	 * with it.
	 * 
	 * @param camera
	 *            the {@link Camera} to be controlled
	 */
	public CameraController(Camera camera) {
		this.camera = camera;
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

		System.out.println(
				String.format("Pressed: %s, moving camera x: %.2f, y: %.2f",
						cmd, cameraMoveX, cameraMoveY));
	}

	/**
	 * @see org.newdawn.slick.command.InputProviderListener#controlReleased(org.newdawn.slick.command.Command)
	 */
	@Override
	public void controlReleased(org.newdawn.slick.command.Command pCommand) {
		Command cmd = Command.valueOfCommand((BasicCommand) pCommand);
		System.out.println("Released: " + cmd);
		
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
		// Move 100px / sec in both directions
		camera.translate(cameraMoveX * delta * .1f,cameraMoveY * delta * .1f);
	}

}
