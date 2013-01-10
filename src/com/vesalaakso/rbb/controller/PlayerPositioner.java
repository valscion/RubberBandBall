package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;
import com.vesalaakso.rbb.util.Utils;

/**
 * A controller to position player somewhere in the spawn area when the game
 * starts.
 * 
 * @author Vesa Laakso
 */
public class PlayerPositioner extends MouseAdapter {

	/** Player to position. */
	private Player player;

	/** Map is queried from this container */
	private TileMapContainer mapContainer;

	/**
	 * Constructs a player positioner and associates it with the given player
	 * and map container.
	 * 
	 * @param player
	 *            the player to position
	 * @param mapContainer
	 *            the map to query for spawn area
	 */
	public PlayerPositioner(Player player, TileMapContainer mapContainer) {
		this.player = player;
		this.mapContainer = mapContainer;
	}

	/**
	 * Updates the player position
	 * 
	 * @param mouseX
	 *            mouse x-coordinate
	 * @param mouseY
	 *            mouse y-coordinate
	 * @return was the coordinates inside spawn area and player positioned there
	 *         or not
	 */
	private boolean updatePosition(int mouseX, int mouseY) {
		if (player.isStartPositioned()) {
			return false;
		}
		float x = Utils.screenToWorldX(mouseX);
		float y = Utils.screenToWorldY(mouseY);

		TileMapObject spawn = mapContainer.getMap().getSpawnArea();

		if (Utils.isCircleInsideRect(x, y, player.getRadius(), spawn.x,
				spawn.y, spawn.width, spawn.height)) {
			player.setPosition(x, y);
			return true;
		}
		return false;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		updatePosition(newx, newy);
	}

	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		updatePosition(newx, newy);
	}

	@Override
	public void mouseClicked(int button, int x, int y, int clickCount) {
		if (player.isStartPositioned()) {
			return;
		}
		if (updatePosition(x, y)) {
			player.setStartPositioned();
		}
	}
}
