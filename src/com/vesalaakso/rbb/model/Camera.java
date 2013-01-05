package com.vesalaakso.rbb.model;

/**
 * A singleton class epresenting the camera. A camera is used to specify the
 * area of the world which currently will be drawn. Moving the camera will paint
 * a different area of the world.
 * 
 * @author Vesa Laakso
 * 
 */
public class Camera {

	/** The only instance of this class. */
	private static final Camera _instance = new Camera();

	/** X-coordinate of the camera, in world coordinates. */
	private float x;

	/** Y-coordiante of the camera, in world coordinates. */
	private float y;

	/**
	 * Private constructor for the camera, because this class is a singleton.
	 */
	private Camera() {
	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return the singleton instance
	 */
	public static Camera get() {
		return _instance;
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
	 * @param moveX
	 *            the amount to move the camera in x-axis
	 * @param moveY
	 *            the amount to move the camera in y-axis
	 */
	public void translate(float moveX, float moveY) {
		x += moveX;
		y += moveY;
	}

}
