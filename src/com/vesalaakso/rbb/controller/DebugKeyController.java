package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.Player;

/**
 * Used to allow various keys to aid in debugging the game.
 * 
 * @author Vesa Laakso
 */
public class DebugKeyController extends KeyAdapter implements Updateable {

	/** <code>Player</code> to debug. */
	private Player player;

	/** How much the happiness of the player will be modified. */
	private float playerChangeHappiness;

	/**
	 * Constructs a new controller and allows it to debug the given instances.
	 * 
	 * @param player
	 *            we want to debug player, yes.
	 */
	public DebugKeyController(Player player) {
		this.player = player;
	}

	@Override
	public void keyPressed(Key key, char c) {
		if (key == Key.DBG_PLAYER_HAPPINESS_ADD) {
			playerChangeHappiness = 1.0f;
		}
		else if (key == Key.DBG_PLAYER_HAPPINESS_SUB) {
			playerChangeHappiness = -1.0f;
		}
	}

	@Override
	public void keyReleased(Key key, char c) {
		if (key == Key.DBG_PLAYER_HAPPINESS_ADD
				|| key == Key.DBG_PLAYER_HAPPINESS_SUB) {
			playerChangeHappiness = 0.0f;
		}
	}

	@Override
	public void update(int delta) {
		float factor = delta / 1000.0f;
		player.changeHappiness(player.getHappiness() + playerChangeHappiness
				* factor);
	}

}
