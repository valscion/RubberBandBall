package com.vesalaakso.rbb.controller;

import java.util.List;

import org.newdawn.slick.command.InputProviderListener;

/**
 * Classes that implement this can be attached to {@link InputMaster} for
 * receiving commands. XXX is this necessary?
 * 
 * @author Vesa Laakso
 */
public interface Controller extends InputProviderListener {

	/**
	 * This method is used to get a list of {@link CommandEnum} enums that this
	 * <code>Controller</code> will handle.
	 * 
	 * @return List of commands this class will handle.
	 */
	public List<CommandEnum> getCommands();

	/**
	 * Every controller acts upon every tick of the game loop through this
	 * method.
	 * 
	 * @param delta
	 *            The amount of time thats passed since last update in
	 *            milliseconds
	 */
	public void update(int delta);
}
