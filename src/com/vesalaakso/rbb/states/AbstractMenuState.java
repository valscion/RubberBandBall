package com.vesalaakso.rbb.states;

import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Font;
import com.vesalaakso.rbb.model.ResourceManager;

/**
 * An abstract class for variety of menus
 * 
 * @author Vesa Laakso
 */
public abstract class AbstractMenuState extends BasicGameState {

	/** Width of a single item. */
	private static final int ITEM_WIDTH = RubberBandBall.SCREEN_WIDTH / 2;

	/** Height of a single item. */
	private static final int ITEM_HEIGHT = ITEM_WIDTH / 6;

	/** Left x position for all items. */
	private static final int ITEM_X = ITEM_WIDTH / 2;

	/** Start y-coordinate for items. */
	private static final int ITEM_START_Y = ITEM_HEIGHT * 2;

	/** Y offset for all items */
	private static final int ITEM_OFFSET_Y = ITEM_HEIGHT;

	/** The resource manager to consult for resources */
	private final ResourceManager resourceManager;

	/** All the menu items. */
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	/** Current item selected */
	private MenuItem selected;

	/** The state to move to on next update call. */
	private State moveToState;

	/**
	 * Constructs a new menu state and associates it with the given resource
	 * manager.
	 * 
	 * @param resourceManager
	 *            the <code>ResourceManager</code> to associate the new menu
	 *            state with.
	 */
	public AbstractMenuState(ResourceManager resourceManager) {
		this.resourceManager = resourceManager;
	}

	/**
	 * Called before any menu items are added in the first render call.
	 * 
	 * @param menuItems
	 *            the list to add menu items to
	 * @param resourceManager
	 *            the resource manager to get resources from, if needed.
	 */
	public abstract void preRender(List<MenuItem> menuItems,
		ResourceManager resourceManager);

	@Override
	public void init(GameContainer container, StateBasedGame game) {
		// NO-OP
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		// If menu items haven't been added yet, run a method responsible for
		// adding them.
		if (menuItems.isEmpty()) {
			preRender(menuItems, resourceManager);
		}

		g.setFont(resourceManager.getFont(Font.MENU_ITEM));

		float lineHeight = g.getFont().getLineHeight();

		for (int i = 0, size = menuItems.size(); i < size; i++) {
			MenuItem item = menuItems.get(i);

			// If we've currently selected this item, hilight it.
			if (item == selected) {
				g.setFont(resourceManager.getFont(Font.MENU_ITEM_HILIGHTED));
			}
			else {
				g.setFont(resourceManager.getFont(Font.MENU_ITEM));
			}

			float topY = ITEM_START_Y + i * ITEM_OFFSET_Y;

			g.drawRoundRect(ITEM_X, topY, ITEM_WIDTH, ITEM_HEIGHT, 20);

			float textX = ITEM_X + (ITEM_WIDTH - item.textWidth) / 2;
			float textY = topY + (ITEM_HEIGHT - lineHeight) / 2;
			g.drawString(item.text, textX, textY);
		}
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		if (moveToState != null) {
			Transition leave = new FadeOutTransition();
			Transition enter = new FadeInTransition();
			game.enterState(moveToState.ordinal(), leave, enter);
			moveToState = null;
			selected = null;
		}
	}

	/**
	 * @see org.newdawn.slick.InputListener#mouseMoved(int, int, int, int)
	 */
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		selected = findItemForCoordinates(newx, newy);
	}

	/**
	 * @see org.newdawn.slick.InputListener#mouseClicked(int, int, int, int)
	 */
	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (selected != null) {
			onMenuItemSelected(selected);
		}
	}

	/**
	 * When a <code>MenuItem</code> is selected, this method gets called. By
	 * default it moves on to the state that was given to the menu item and if
	 * no state was given, it does nothing. So for anything special, you should
	 * override this method.
	 * 
	 * @param selectedItem
	 *            the MenuItem which was selected
	 */
	public void onMenuItemSelected(MenuItem selectedItem) {
		if (selectedItem.nextState != null) {
			moveToState = selectedItem.nextState;
		}
	}

	/** Finds the item within the given coordinates */
	private MenuItem findItemForCoordinates(int x, int y) {
		// x-coordinate can be checked first
		int left = ITEM_X;
		int right = ITEM_X + ITEM_WIDTH;

		if (x < left || x > right) {
			return null;
		}

		for (int i = 0, size = menuItems.size(); i < size; i++) {
			int topY = ITEM_START_Y + i * ITEM_OFFSET_Y;
			if (y < topY) {
				// Over the top.
				continue;
			}
			if (y > topY + ITEM_HEIGHT) {
				continue;
			}
			return menuItems.get(i);
		}

		// None was found if we got to here.
		return null;
	}

	/** Represents a single menu item */
	protected class MenuItem {
		/** The text to draw */
		final String text;
		/** The state to move to when this item is picked */
		final State nextState;
		/** The width of the given text in pixels */
		final float textWidth;
		/** The numerical ID for this menu */
		final int id;

		/**
		 * Creates a new menu item with the specified text, action and ID.
		 * 
		 * @param id
		 *            the ID to set for the new menu item
		 * @param text
		 *            text to display
		 * @param nextState
		 *            state to move to when item is selected
		 */
		MenuItem(int id, String text, State nextState) {
			this.id = id;
			this.text = text;
			this.nextState = nextState;
			this.textWidth =
				resourceManager.getFont(Font.MENU_ITEM).getWidth(text);
		}
	}
}
