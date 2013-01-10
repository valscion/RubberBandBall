package com.vesalaakso.rbb.states;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.controller.MapChanger;
import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * A state used to handle map changing in a nice way.
 * 
 * @author Vesa Laakso
 */
public class MapChangeState extends BasicGameState {

	/** The one responsible for telling the game when the map has changed. */
	private MapChanger mapChanger;

	/** The state id to change to once the mapChanger is finished initializing. */
	private int newState;

	/**
	 * Setups the routine for changing map.
	 * 
	 * @param mapChanger
	 *            the MapChanger responsive for updating the models and stuff.
	 * @param newState
	 *            the state id to change to when map is ready
	 */
	public void setupChange(MapChanger mapChanger, int newState) {
		this.mapChanger = mapChanger;
		this.newState = newState;
	}

	/**
	 * @see BasicGameState#enter(org.newdawn.slick.GameContainer,
	 *      org.newdawn.slick.state.StateBasedGame)
	 */
	@Override
	public void enter(GameContainer container, StateBasedGame game)
			throws SlickException {
		try {
			mapChanger.runChange();
		}
		catch (MapException e) {
			Log.error("Failed to change map!");
			throw new SlickException("Failed to change map!", e);
		}
	}

	@Override
	public void init(GameContainer container, StateBasedGame game)
			throws SlickException {
		// NO-OP
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g)
			throws SlickException {
		g.clear();
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta)
			throws SlickException {
		game.enterState(newState, new FadeOutTransition(), new FadeInTransition());
	}

	@Override
	public int getID() {
		return State.MAP_CHANGE.ordinal();
	}

}
