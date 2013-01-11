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
	 * Calculate the current screen x-coordinate for the given world
	 * x-coordinate.
	 * 
	 * @param worldX
	 *            the world x-coordinate to transform
	 * @return screen x-coordinate corresponding to the given world x-coordinate
	 */
	public static float worldToScreenX(float worldX) {
		return 0;
	}

	/**
	 * Calculate the current screen y-coordinate for the given world
	 * y-coordinate.
	 * 
	 * @param worldY
	 *            the world y-coordinate to transform
	 * @return screen y-coordinate corresponding to the given world y-coordinate
	 */
	public static float worldToScreenY(float worldY) {
		// Need to calc half of the height of the screen for this
		float screenHalfHeight = RubberBandBall.SCREEN_HEIGHT * 0.5f;

		// Camera.
		Camera cam = Camera.get();

		// Calculate the screen coordinate
		float y = worldY - (cam.getY() - screenHalfHeight) / cam.getScaling();

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

	/**
	 * Clamps the value to the given range. If the min and max values are given
	 * in an incorrect way this method is useless.
	 * 
	 * @param value
	 *            the value to clamp
	 * @param min
	 *            the minimum amount for the value
	 * @param max
	 *            the maximum amount for the value
	 * 
	 * @return the clamped value
	 */
	public static int clamp(int value, int min, int max) {
		if (value < min) {
			return min;
		}
		if (value > max) {
			return max;
		}
		return value;
	}

	/**
	 * Calculates is the given coordinates are inside the given rectangle.
	 * 
	 * @param px
	 *            point x-coordinate
	 * @param py
	 *            point y-coordinate
	 * @param rectX
	 *            rectangle top left x-coordinate
	 * @param rectY
	 *            rectangle top left y-coordinate
	 * @param rectWidth
	 *            rectangle width
	 * @param rectHeight
	 *            rectangle height
	 * 
	 * @return <code>true</code> if the point was inside the rectangle.
	 */
	public static boolean isPointInsideRect(float px, float py, float rectX,
		float rectY, float rectWidth, float rectHeight) {

		float left = rectX;
		float right = rectX + rectWidth;
		float top = rectY;
		float bottom = rectY + rectHeight;

		if (px < left)
			return false;
		if (px > right)
			return false;
		if (py < top)
			return false;
		if (py > bottom)
			return false;

		return true;
	}

	/**
	 * Calculates is the given circle inside the given rectangle.
	 * 
	 * @param cx
	 *            circle center x-coordinate
	 * @param cy
	 *            circle center y-coordinate
	 * @param r
	 *            circle radius
	 * @param rectX
	 *            rectangle top left x-coordinate
	 * @param rectY
	 *            rectangle top left y-coordinate
	 * @param rectWidth
	 *            rectangle width
	 * @param rectHeight
	 *            rectangle height
	 * 
	 * @return <code>true</code> if the circle was inside the rectangle.
	 */
	public static boolean isCircleInsideRect(float cx, float cy, float r,
		float rectX, float rectY, float rectWidth, float rectHeight) {

		float left = rectX + r;
		float right = rectX + rectWidth - r;
		float top = rectY + r;
		float bottom = rectY + rectHeight - r;

		if (cx < left)
			return false;
		if (cx > right)
			return false;
		if (cy < top)
			return false;
		if (cy > bottom)
			return false;
		return true;
	}

}
