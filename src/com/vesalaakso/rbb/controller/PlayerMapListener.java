package com.vesalaakso.rbb.controller;

import java.util.List;

import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;

/**
 * A class which checks where the player moves and if the player happens to
 * touch special stuff in map, do something special.
 * 
 * @author Vesa Laakso
 */
public class PlayerMapListener implements Updateable {

	/** The map container to read map from which to read areas from. */
	private TileMapContainer mapContainer;

	/** The player to check for movements */
	private Player player;

	/**
	 * Constructs a new listener for player movements and associates it with the
	 * given map and player.
	 * 
	 * @param mapContainer
	 *            the map to get the map to read areas from
	 * @param player
	 *            the player to listen for moving
	 */
	public PlayerMapListener(TileMapContainer mapContainer, Player player) {
		this.mapContainer = mapContainer;
		this.player = player;
	}

	/**
	 * @see com.vesalaakso.rbb.controller.Updateable#update(int)
	 */
	@Override
	public void update(int delta) {
		TileMap map = mapContainer.getMap();

		List<TileMapObject> safeAreas = map.getSafeAreas();

		for (TileMapObject area : safeAreas) {
			if (player.isInsideArea(area.x, area.y, area.width, area.height)) {
				player.changeHappiness(1);
			}
		}
	}

}
