package com.vesalaakso.rbb.util;

import org.lwjgl.Sys;
import org.newdawn.fizzy.Vector;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;

/**
 * Some static methods to aid in various little things.
 * 
 * @author Vesa Laakso
 */
public class Utils {

	/** A private constructor as this class has only static methods */
	private Utils() {
	}

	/**
	 * Calculates a point between two points in a curved way. This method can be
	 * used to apply tweening between movements.
	 * 
	 * @param old
	 *            the coordinates where we start moving
	 * @param target
	 *            the coordinates where we want to end up in the long run
	 * @param n
	 *            the slowdown factor, must be >= 1
	 * @return smoothed vector between the vectors <code>old</code> and
	 *         <code>target</code>.
	 */
	public static Vector curvePoints(Vector old, Vector target, int n) {
		if (n < 1) {
			n = 1;
		}

		// Calculate the new coordinations using weighted average function
		float x = ((old.x * (n - 1)) + target.x) / n;
		float y = ((old.y * (n - 1)) + target.y) / n;

		// Return the curved endpoint
		return new Vector(x, y);
	}

	/**
	 * Calculate the current world x-coordinate for the given screen
	 * x-coordinate.
	 * 
	 * @param screenX
	 *            the screen x-coordinate to transform
	 * @return world x-coordinate corresponding to the given screen x-coordinate
	 */
	public static float screenToWorldX(float screenX) {
		// Need to calc half of the width of the screen for this
		float screenHalfWidth = RubberBandBall.SCREEN_WIDTH * 0.5f;

		// Camera.
		Camera cam = Camera.get();

		// Calculate the screen coordinate
		float x = cam.getX() + (screenX - screenHalfWidth) / cam.getScaling();

		return x;
	}

	/**
	 * Calculate the current world y-coordinate for the given screen
	 * y-coordinate.
	 * 
	 * @param screenY
	 *            the screen y-coordinate to transform
	 * @return world y-coordinate corresponding to the given screen y-coordinate
	 */
	public static float screenToWorldY(float screenY) {
		// Need to calc half of the height of the screen for this
		float screenHalfHeight = RubberBandBall.SCREEN_HEIGHT * 0.5f;

		// Camera.
		Camera cam = Camera.get();

		// Calculate the screen coordinate
		float y = cam.getY() + (screenY - screenHalfHeight) / cam.getScaling();

		return y;
	}

	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

}
