package com.vesalaakso.rbb.model;

import org.newdawn.slick.geom.Vector2f;

/**
 * A singleton class epresenting the camera. A camera is used to specify the
 * area of the world which currently will be drawn. Moving the camera will paint
 * a different area of the world. <strong>The Camera must be initialized before
 * use!</strong> Use the {@link #init(int, int)} method for it.
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

	/** The scale factor used to scale the whole game. */
	private float scaling = 1.0f;

	/**
	 * A private constructor for this class is a singleton and has only one
	 * instance.
	 */
	private Camera() {
	}

	/**
	 * Returns the only instance of this class.
	 * 
	 * @return the one and only instance of <code>Camera</code>.
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

	/**
	 * Sets the scaling factor to scale the whole game. Minimum possible is 1.
	 * 
	 * @param scale
	 *            the new scaling factor of the whole game.
	 */
	public void setScaling(float scale) {
		if (scale < 1) {
			scale = 1;
		}
		this.scaling = scale;
	}

	/**
	 * Gets the scale factor to scale the whole game.
	 * 
	 * @return scaling factor
	 */
	public float getScaling() {
		return scaling;
	}

}
