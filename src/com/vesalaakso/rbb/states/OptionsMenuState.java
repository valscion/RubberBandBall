package com.vesalaakso.rbb.states;

import java.util.List;

import com.vesalaakso.rbb.model.ResourceManager;

/**
 * A state responsible for changing settings, such as music volume or muting.
 * 
 * @author Vesa Laakso
 */
public class OptionsMenuState extends AbstractMenuState {

	/**
	 * Constructs a state responsible for changing settings.
	 * 
	 * @param resourceManager the resource manager to consult for resources
	 */
	public OptionsMenuState(ResourceManager resourceManager) {
		super(resourceManager);
	}

	@Override
	public void preRender(List<MenuItem> menuItems,
		ResourceManager resourceManager) {
		menuItems.add(new MenuItem("Back to main menu", State.MAIN_MENU));
	}

	@Override
	public int getID() {
		return State.OPTIONS_MENU.ordinal();
	}
}
