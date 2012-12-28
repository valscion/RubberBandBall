package com.vesalaakso.rbb.game.exceptions;

/**
 * Exception that is thrown when a given map was not found.
 */
public class MapException extends Exception {

	/** @see Serializable */
	private static final long serialVersionUID = -567528689020942807L;

	/**
	 * Constructs the exception with the given string.
	 * 
	 * @param message exception message
	 */
	public MapException(String message) {
		super(message);
	}
	
	/**
	 * Constructs the exception with the given string and a cause.
	 * 
	 * @param message exception message
	 * @param cause the cause of this exception
	 */
	public MapException(String message, Throwable cause) {
		super(message, cause);
	}

}
