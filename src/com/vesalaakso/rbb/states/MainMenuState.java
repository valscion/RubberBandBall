package com.vesalaakso.rbb.states;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.Effect;
import org.newdawn.slick.font.effects.ShadowEffect;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.state.transition.Transition;

import com.vesalaakso.rbb.RubberBandBall;

/**
 * A game state responsible for main menu logic and drawing. From here we can
 * get to the real game.
 * 
 * @author Vesa Laakso
 */
public class MainMenuState extends BasicGameState {

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

	/** All the menu items. */
	private List<MenuItem> menuItems = new ArrayList<MenuItem>();

	/** The regular font. */
	private UnicodeFont regularFont;

	/** The font used to hilight the current selected item */
	private UnicodeFont hilightFont;

	/** Current item selected */
	private MenuItem selected;

	/** The state to move to on next update call. */
	private State moveToState;

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// TODO Some pics.

		// Load the regularFont used to draw the menu. TODO: Proper resource
		// path
		regularFont =
			new UnicodeFont("data/fonts/Rokkitt.ttf", 40, false, false);
		regularFont.addAsciiGlyphs();

		// Effectify the regularFont
		List<Effect> fEffects = regularFont.getEffects();
		fEffects.add(new ColorEffect(Color.getHSBColor(0.55f, 1.0f, 0.75f)));

		regularFont.loadGlyphs();

		// Load the hilightFont used to hilight the current item.
		hilightFont =
			new UnicodeFont("data/fonts/Rokkitt.ttf", 40, false, false);
		hilightFont.addAsciiGlyphs();

		// Effectify the hilightFont
		fEffects = hilightFont.getEffects();
		fEffects.add(new ShadowEffect(new Color(255, 255, 255), 1, 1, 0.75f));
		fEffects.add(new ColorEffect(Color.getHSBColor(0.55f, 1.0f, 0.75f)));

		hilightFont.loadGlyphs();

		// Add menu items
		menuItems.add(new MenuItem("Start the game", State.GAME));
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {

		g.setFont(regularFont);

		float lineHeight = regularFont.getLineHeight();

		for (int i = 0, size = menuItems.size(); i < size; i++) {
			MenuItem item = menuItems.get(i);

			// If we've currently selected this item, hilight it.
			if (item == selected) {
				g.setFont(hilightFont);
			}
			else {
				g.setFont(regularFont);
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
			moveToState = selected.nextState;
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

	@Override
	public int getID() {
		return State.MAIN_MENU.ordinal();
	}

	/** Represents a single menu item */
	private class MenuItem {
		/** The text to draw */
		final String text;
		/** The state to move to when this item is picked */
		final State nextState;
		/** The width of the given text in pixels */
		final float textWidth;

		/**
		 * Creates a new menu item with the specified text and action.
		 * 
		 * @param text
		 *            text to display
		 * @param nextState
		 *            state to move to when item is selected
		 */
		MenuItem(String text, State nextState) {
			this.text = text;
			this.nextState = nextState;
			this.textWidth = regularFont.getWidth(text);
		}
	}
}
