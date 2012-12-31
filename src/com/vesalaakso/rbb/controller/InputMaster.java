package com.vesalaakso.rbb.controller;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Input;
import org.newdawn.slick.command.InputProvider;

/**
 * Handles the coordination of different input based controllers.
 * 
 * @author Vesa Laakso
 */
public class InputMaster {

	/** The input provider abstracting input */
	private InputProvider provider;

	/** The {@link Controller}s linked to this */
	private List<Controller> controllers = new LinkedList<Controller>();

	/**
	 * Constructs an InputMaster and all other controllers, too.
	 * 
	 * @param input
	 *            The <code>Input</code> an <code>InputProvider</code> will be
	 *            attached to.
	 */
	public InputMaster(Input input) {
		provider = new InputProvider(input);
		resetBindings();
	}

	/** Resets the default keys to all commands. */
	public void resetBindings() {
		for (CommandEnum cmdEnum : CommandEnum.values()) {
			provider.clearCommand(cmdEnum.basicCommand);
			provider.bindCommand(cmdEnum.defaultControl, cmdEnum.basicCommand);
		}
	}

	/**
	 * Adds the given {@link Controller} as a command listener.
	 * 
	 * @param controller
	 *            the <code>Controller</code> to add as a listener
	 */
	public void addController(Controller controller) {
		provider.addListener(controller);
		controllers.add(controller);
	}

	/**
	 * Calls the update()-method on all attached <code>Controller</code>s.
	 * 
	 * @param delta
	 *            The amount of time thats passed since last update in
	 *            milliseconds
	 * 
	 * @see Controller#update(int)
	 */
	public void updateControllers(int delta) {
		for (Controller c : controllers) {
			c.update(delta);
		}
	}

}
