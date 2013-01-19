package com.vesalaakso.rbb.controller;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.ControlledInputReciever;
import org.newdawn.slick.Input;
import org.newdawn.slick.KeyListener;
import org.newdawn.slick.MouseListener;

/**
 * Handles the coordination of different input based controllers.
 * 
 * @author Vesa Laakso
 */
public class InputMaster implements Updateable, Resetable {

	/** The input that generates events */
	private Input input;

	/** List of controllers which would like to act upon every frame. */
	private List<Updateable> updateables = new LinkedList<Updateable>();

	/** List of controllers which would like to be reset when map changes. */
	private List<Resetable> resetables = new LinkedList<Resetable>();

	/** Added mouse listeners. */
	private List<MouseListener> mouseListeners =
			new LinkedList<MouseListener>();

	/** Added key listeners. */
	private List<KeyListener> keyListeners = new LinkedList<KeyListener>();

	/** Is the input master paused or not */
	private boolean paused;

	/**
	 * Constructs an InputMaster and associates it with the given
	 * <code>Input</code>.
	 * 
	 * @param input
	 *            The <code>Input</code> that will generate events to listen to.
	 */
	public InputMaster(Input input) {
		this.input = input;
	}

	/**
	 * Makes the input generate events for the given <code>KeyListener</code>.
	 * 
	 * @param controller
	 *            the controller implementing <code>KeyListener</code> interface
	 *            and acting upon events generated by key pressed.
	 */
	public void addKeyListener(KeyListener controller) {
		input.addKeyListener(controller);
		keyListeners.add(controller);
		afterControllerAdded(controller);
	}

	/**
	 * Makes the input generate events for the given <code>MouseListener</code>.
	 * 
	 * @param controller
	 *            the controller implementing <code>MouseListener</code>
	 *            interface and acting upon events generated by mouse.
	 */
	public void addMouseListener(MouseListener controller) {
		input.addMouseListener(controller);
		mouseListeners.add(controller);
		afterControllerAdded(controller);
	}
	
	/** A helper method to do stuff after a controller is added */
	private void afterControllerAdded(ControlledInputReciever controller) {
		if (controller instanceof Updateable) {
			updateables.add((Updateable) controller);
		}
		if (controller instanceof Resetable) {
			resetables.add((Resetable) controller);
		}
	}

	/** Pauses the added inputs from firing */
	public void pause() {
		if (paused) {
			return;
		}
		paused = true;
		for (MouseListener listener : mouseListeners) {
			input.removeMouseListener(listener);
		}
		for (KeyListener listener : keyListeners) {
			input.removeKeyListener(listener);
		}
	}

	/** Resumes the added inputs to fire again */
	public void unpause() {
		if (!paused) {
			return;
		}
		paused = false;
		for (MouseListener listener : mouseListeners) {
			input.addMouseListener(listener);
		}
		for (KeyListener listener : keyListeners) {
			input.addKeyListener(listener);
		}
	}

	/**
	 * Calls the update()-method on all attached <code>Controller</code>s.
	 * 
	 * @param delta
	 *            The amount of time thats passed since last update in
	 *            milliseconds
	 * 
	 * @see Updateable#update(int)
	 */
	@Override
	public void update(int delta) {
		for (Updateable c : updateables) {
			c.update(delta);
		}
	}

	/**
	 * Resets all controllers which would like to be reset
	 * 
	 * @see com.vesalaakso.rbb.controller.Resetable#reset()
	 */
	@Override
	public void reset() {
		for (Resetable r : resetables) {
			r.reset();
		}
	}

}
