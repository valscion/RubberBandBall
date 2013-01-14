package com.vesalaakso.rbb.controller;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;

/**
 * Limits the camera position and scaling to the current map.
 * 
 * @author Vesa Laakso
 */
public class CameraLimiter implements Resetable, Updateable {

	/** The map container to query the current map from */
	private TileMapContainer mapContainer;
	
	/**
	 * Construct a new camera position limiter and associate it with the given
	 * <code>TileMapContainer</code>.
	 * 
	 * @param mapContainer the map container to query the current map from
	 */
	public CameraLimiter(TileMapContainer mapContainer) {
		this.mapContainer = mapContainer;
	}

	@Override
	public void update(int delta) {
		Camera cam = Camera.get();
		TileMap map = mapContainer.getMap();

		// Limit camera position based on map height.
		int mapWidth = map.getWidthInTiles() * TileMap.TILE_SIZE;
		int mapHeight = map.getHeightInTiles() * TileMap.TILE_SIZE;

		// Position to set the camera to
		float camX = cam.getX();
		float camY = cam.getY();

		// Current viewport width and height halves in pixels
		float halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
		float halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());

		// Limit scaling
		if (halfScrW * 2 > mapWidth) {
			float newScale = ((float) RubberBandBall.SCREEN_WIDTH / mapWidth);
			cam.setScaling(newScale);
			halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
			halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());
		}
		if (halfScrH * 2 > mapHeight) {
			float newScale = ((float) RubberBandBall.SCREEN_HEIGHT / mapHeight);
			cam.setScaling(newScale);
			halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
			halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());
		}

		if (camX < halfScrW) {
			// Over the left
			camX = halfScrW;
		}
		else if (camX > mapWidth - halfScrW) {
			// Over the right
			camX = mapWidth - halfScrW;
		}
		if (camY < halfScrH) {
			// Over the top
			camY = halfScrH;
		}
		else if (camY > mapHeight - halfScrH) {
			// Over the bottom
			camY = mapHeight - halfScrH;
		}

		cam.setPosition(camX, camY);
	}

	@Override
	public void reset() {
		update(0);
	}

}
