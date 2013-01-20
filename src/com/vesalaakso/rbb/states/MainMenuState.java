package com.vesalaakso.rbb.states;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.resources.Audio;
import com.vesalaakso.rbb.model.resources.Gfx;

/**
 * A game state responsible for main menu logic and drawing. From here we can
 * get to the real game.
 * 
 * @author Vesa Laakso
 */
public class MainMenuState extends AbstractMenuState {

	/** ID for start button */
	private static final int START_BUTTON = 1;
	/** ID for options button */
	private static final int OPTIONS_BUTTON = 2;
	/** ID for exit button */
	private static final int EXIT_BUTTON = 3;

	/** True if on next update, the game should exit. */
	private boolean exitOnUpdate = false;

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
		menuItems.add(new MenuItem(START_BUTTON, "Start the game", State.GAME));
		menuItems.add(new MenuItem(OPTIONS_BUTTON, "Gameplay",
				State.OPTIONS_MENU));
		menuItems.add(new MenuItem(EXIT_BUTTON, "Exit", null));

		// MUSIC!
		Music m = resourceManager.getMusic(Audio.MUSIC_BACKGROUND);
		m.loop();
		m.setVolume(0.33f);
	}

	@Override
	public void onMenuItemSelected(MenuItem selectedItem) {
		if (selectedItem.id == EXIT_BUTTON) {
			exitOnUpdate = true;
		}
		else {
			super.onMenuItemSelected(selectedItem);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (exitOnUpdate) {
			container.exit();
		}
		else {
			super.update(container, game, delta);
		}
	}

	@Override
	public int getID() {
		return State.MAIN_MENU.ordinal();
	}

	@Override
	public void renderBackground(Graphics g, ResourceManager resourceManager) {
		// Background, please
		Image bgImg = resourceManager.getImage(Gfx.BACKGROUND_MENU);
		g.drawImage(bgImg, 0, 0);
	}
}
