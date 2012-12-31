package com.vesalaakso.rbb.model;

/**
 * Represents the camera. A camera is used to specify the area of the world
 * which currently will be drawn. Moving the camera will paint a different area
 * of the world.
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

	/**
	 * Translates the camera according to the given coordinates.
	 * 
	 * @param moveX the amount to move the camera in x-axis
	 * @param moveY the amount to move the camera in y-axis
	 */
	public void translate(float moveX, float moveY) {
		x += moveX;
		y += moveY;
	}

}
