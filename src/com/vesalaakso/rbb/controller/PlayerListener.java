package com.vesalaakso.rbb.controller;

import java.util.List;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;

import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;
import com.vesalaakso.rbb.states.GameState;

/**
 * A class which checks where the player moves and if the player happens to
 * touch special stuff in map, do something special.
 * 
 * @author Vesa Laakso
 */
public class PlayerListener implements Updateable {

	/** The map container to read map from which to read areas from. */
	private TileMapContainer mapContainer;

	/** The player to check for movements */
	private Player player;

	/** Physics to consult player speed from. */
	private Physics physics;

	/** The game to modify when something triggerable happens */
	private GameState game;

	/**
	 * Constructs a new listener for player movements and associates it with the
	 * given map and player.
	 * 
	 * @param mapContainer
	 *            the map to get the map to read areas from
	 * @param player
	 *            the player to listen for moving
	 * @param physics
	 *            physics to consult player speed from
	 * @param game
	 *            the game to modify when something to trigger happens
	 */
	public PlayerListener(TileMapContainer mapContainer, Player player,
			Physics physics, GameState game) {
		this.mapContainer = mapContainer;
		this.player = player;
		this.physics = physics;
		this.game = game;
	}

	/**
	 * @see com.vesalaakso.rbb.controller.Updateable#update(int)
	 */
	@Override
	public void update(int delta) {
		TileMap map = mapContainer.getMap();
		Body<Circle> playerBody = physics.getPlayerBody();

		boolean happinessChanged = false;
		boolean inSafeArea = false;
		List<TileMapObject> safeAreas = map.getSafeAreas();

		for (TileMapObject area : safeAreas) {
			if (player.isInsideArea(area.x, area.y, area.width, area.height)) {
				player.setHappiness(1);
				happinessChanged = true;
				inSafeArea = true;
			}
		}

		// If player has stopped and is not in safe area, game over.
		if (playerBody.isActive() && playerBody.isSleeping()) {
			if (!inSafeArea) {
				// :(
				game.gameOver("Player has ended up in an unsafe environment.");
			}
		}

		// If happiness hasn't changed because of being inside safe area, set
		// it to zero if the player is ready to be launched.
		if (!happinessChanged && player.isReadyForLaunch()) {
			player.setHappiness(0);
			happinessChanged = true;
		}

		// If happiness hasn't been changed, change it by consulting player
		// speed.
		if (!happinessChanged) {
			float angVel = Math.abs(playerBody.getAngularVelocity());
			if (angVel > 2) {
				player.setHappiness(-angVel / 10);
				happinessChanged = true;
			}
		}

		// If still the happiness hasn't changed, it is based on the speed of
		// the player and can be positive, too.
		if (!happinessChanged) {
			float speed = playerBody.getXVelocity() + playerBody.getYVelocity();
			speed = Math.abs(speed);
			player.setHappiness((speed * 5 - 50) / 100);
		}
	}

}
