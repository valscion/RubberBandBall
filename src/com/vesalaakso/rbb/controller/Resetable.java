package com.vesalaakso.rbb.controller;

/**
 * All classes which need to be reseted when map changes or state changes need
 * to implement this interface.
 * 
 * @author Vesa Laakso
 */
public interface Resetable {

	/**
	 * A general resetion method, calling this should reset the state of the
	 * object.
	 */
	public void reset();
}
