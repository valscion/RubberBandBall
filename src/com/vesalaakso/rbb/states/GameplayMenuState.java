package com.vesalaakso.rbb.states;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.resources.Gfx;

/**
 * A state responsible for changing settings, such as music volume or muting.
 * 
 * @author Vesa Laakso
 */
public class GameplayMenuState extends AbstractMenuState {

	/** ID for returning to main menu button */
	private static final int RETURN_BUTTON = 1;

	/**
	 * Constructs a state responsible for changing settings.
	 * 
	 * @param resourceManager
	 *            the resource manager to consult for resources
	 */
	public GameplayMenuState(ResourceManager resourceManager) {
		super(resourceManager);
	}

	@Override
	public void preRender(List<MenuItem> menuItems,
		ResourceManager resourceManager) {
		menuItems.add(new MenuItem(RETURN_BUTTON, "Back to main menu",
				State.MAIN_MENU));
	}

	@Override
	public int getID() {
		return State.OPTIONS_MENU.ordinal();
	}

	@Override
	public void renderBackground(Graphics g, ResourceManager resourceManager) {
		Image bgImg = resourceManager.getImage(Gfx.GAMEPLAY);
		g.drawImage(bgImg, 0, 0);
	}
}
