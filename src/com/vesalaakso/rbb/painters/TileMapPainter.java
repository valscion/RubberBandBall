package com.vesalaakso.rbb.painters;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.game.TileMap;

/**
 * Handles the drawing of the tile map.
 */
public class TileMapPainter implements Painter {

	/** The {@link TileMap} we want to draw. */
	private TileMap map;
	
	/**
	 * @see Painter#isDrawnToWorldCoordinates
	 * 
	 * @return <code>false</code>, tile maps are always drawn precisely.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return false;
	}

	@Override
	public void paint(Graphics g, float cameraX, float cameraY) {
		// TODO Auto-generated method stub

	}

}
