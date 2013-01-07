package com.vesalaakso.rbb.util;

import org.newdawn.fizzy.Vector;

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
	 * @return smoothed vector between the vectors <code>old</code> and
	 *         <code>target</code>.
	 */
	public static Vector curvePoints(Vector old, Vector target) {
		// The slowdown factor
		final int N = 20;

		// Calculate the new coordinations using weighted average function
		float x = ((old.x * (N - 1)) + target.x) / N;
		float y = ((old.y * (N - 1)) + target.y) / N;

		// Return the curved endpoint
		return new Vector(x, y);
	}

}
