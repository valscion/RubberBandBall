package com.vesalaakso.rbb.states;

import java.util.List;

import org.newdawn.slick.Music;

import com.vesalaakso.rbb.model.Audio;
import com.vesalaakso.rbb.model.ResourceManager;

/**
 * A game state responsible for main menu logic and drawing. From here we can
 * get to the real game.
 * 
 * @author Vesa Laakso
 */
public class MainMenuState extends AbstractMenuState {

	/**
	 * Constructs the main state and associates it with the given resource
	 * manager.
	 * 
	 * @param resourceManager
	 *            the resource manager behind all resource loading
	 */
	public MainMenuState(ResourceManager resourceManager) {
		super(resourceManager);
	}

	@Override
	public void preRender(List<MenuItem> menuItems,
		ResourceManager resourceManager) {
		menuItems.add(new MenuItem("Start the game", State.GAME));
		menuItems.add(new MenuItem("Options", State.OPTIONS_MENU));

		// MUSIC!
		Music m = resourceManager.getMusic(Audio.BACKGROUND);
		m.loop();
	}

	@Override
	public int getID() {
		return State.MAIN_MENU.ordinal();
	}
}
