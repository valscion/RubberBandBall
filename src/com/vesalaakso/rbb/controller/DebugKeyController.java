package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;

/**
 * Used to allow various keys to aid in debugging the game.
 * 
 * @author Vesa Laakso
 */
public class DebugKeyController extends KeyAdapter implements Updateable {

	/** <code>Player</code> to debug. */
	private Player player;

	/** In what direction will the happiness of the player be modified. */
	private float playerChangeHappiness;

	/** In what direction will the scaling of the camera be modified. */
	private float cameraChangeScale;

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
		switch (key) {
			case DBG_PLAYER_HAPPINESS_ADD:
				playerChangeHappiness = 1.0f;
				break;
			case DBG_PLAYER_HAPPINESS_SUB:
				playerChangeHappiness = -1.0f;
				break;
			case DBG_SCALE_CAMERA_UP:
				cameraChangeScale = 1.0f;
				break;
			case DBG_SCALE_CAMERA_DOWN:
				cameraChangeScale = -1.0f;
				break;
			default:
				// Not ours.
		}
	}

	@Override
	public void keyReleased(Key key, char c) {
		if (key == Key.DBG_PLAYER_HAPPINESS_ADD
				|| key == Key.DBG_PLAYER_HAPPINESS_SUB) {
			playerChangeHappiness = 0.0f;
		}
		else if (key == Key.DBG_SCALE_CAMERA_UP
				|| key == Key.DBG_SCALE_CAMERA_DOWN) {
			cameraChangeScale = 0.0f;
		}
	}

	@Override
	public void update(int delta) {
		float factor = delta / 1000.0f;
		player.changeHappiness(player.getHappiness() + playerChangeHappiness
				* factor);

		float oldScale = Camera.get().getScaling();
		float newScale = oldScale + cameraChangeScale * factor;
		Camera.get().setScaling(newScale);
	}

}
