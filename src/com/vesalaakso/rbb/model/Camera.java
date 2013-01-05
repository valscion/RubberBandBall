package com.vesalaakso.rbb.model;

import org.newdawn.slick.geom.Vector2f;

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
	 * Translates the given vector from screen coordinates to world coordinates.
	 * 
	 * @param vector
	 *            the vector to translate to screen coordinates
	 */
	public static void toWorldCoordinates(Vector2f vector) {
		Camera cam = get();
		vector.add(cam.getLocation());
	}

	/**
	 * Translates the given vector from world coordinates to screen coordinates.
	 * 
	 * @param vector
	 *            the vector to translate to world coordinates
	 */
	public static void toScreenCoordinates(Vector2f vector) {
		Camera cam = get();
		vector.sub(cam.getLocation());
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
	 * Gets the current camera location in a <code>Vector2f</code>.
	 * 
	 * @return current location in a <code>Vector2f</code>.
	 */
	public Vector2f getLocation() {
		return new Vector2f(x, y);
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

	/**
	 * Sets the coordinates for the camera.
	 * 
	 * @param x
	 *            x-coordinate in world space to set the camera to
	 * @param y
	 *            y-coordinate in world space to set the camera to
	 */
	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

}
