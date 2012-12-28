package com.vesalaakso.rbb.game;

/**
 * Represents the camera. TODO: Explain what a Camera actually *is*!
 * 
 * @author Vesa
 * 
 */
public class Camera {

	/** X-coordinate of the camera, in world coordinates. */
	private float x;

	/** Y-coordiante of the camera, in world coordinates. */
	private float y;

	/**
	 * Constructor for the Camera, sets the starting coordinates.
	 * 
	 * @param x
	 *            x-coordinate in world coordinates
	 * @param y
	 *            y-coordinate in world coordinates
	 */
	public Camera(float x, float y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the current x-coordinate of the camera, in world coordinates.
	 * 
	 * @return current x-coordinate in world coordinates
	 */
	public float getX() {
		return x;
	}

	/**
	 * Gets the current y-coordinate of the camera, in world coordinates.
	 * 
	 * @return current y-coordinate in world coordinates
	 */
	public float getY() {
		return y;
	}

}
