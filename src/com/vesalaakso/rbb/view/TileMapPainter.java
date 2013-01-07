package com.vesalaakso.rbb.view;

import org.newdawn.slick.tiled.TiledMap;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;

/**
 * Handles the drawing of the tile map.
 */
public abstract class TileMapPainter implements Painter {

	/**
	 * The {@link TileMapContainer} which will be queried to get the current map
	 * to be drawn.
	 */
	private TileMapContainer mapContainer;

	/**
	 * An empty constructor with no access modifier to prevent other than
	 * the classes in this package instantiating this class.
	 * @param mapContainer 
	 */
	TileMapPainter(TileMapContainer mapContainer) {
		this.mapContainer = mapContainer;
	}

	/**
	 * Gets the current {@link TileMap} that is to be drawn.
	 * 
	 * @return current {@link TileMap} that is to be drawn.
	 */
	protected TileMap getMap() {
		return mapContainer.getMap();
	}

	/**
	 * Draws the given layer of the map.
	 * 
	 * @param layer
	 *            which layer should be drawn
	 */
	protected void drawLayer(int layer) {
		Camera cam = Camera.get();
		TileMap map = mapContainer.getMap();
		TiledMap tmap = map.getTiledMap();

		// Calculate the top left coordinates in screen dimensions after the
		// camera has moved.
		float scrX = cam.getX() - RubberBandBall.SCREEN_WIDTH * 0.5f;
		float scrY = cam.getY() - RubberBandBall.SCREEN_HEIGHT * 0.5f;

		// Calculate the top left tile that will be drawn first
		int firstTileX = (int) scrX / TileMap.TILE_SIZE;
		int firstTileY = (int) scrY / TileMap.TILE_SIZE;

		// How many pixels will the whole drawing be offset
		int offsetX = (int) (firstTileX * TileMap.TILE_SIZE - scrX + .5);
		int offsetY = (int) (firstTileY * TileMap.TILE_SIZE - scrY + .5);

		// Drawing it now.
		tmap.render(offsetX, offsetY, firstTileX, firstTileY,
				map.getWidthInTiles() + 3, map.getHeightInTiles() + 3,
				layer, false);
	}

	/**
	 * @see Painter#isDrawnToWorldCoordinates
	 * 
	 * @return <code>false</code>, tile maps are always drawn precisely.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return false;
	}

}
