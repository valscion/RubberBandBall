package com.vesalaakso.rbb.painters;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.game.Camera;
import com.vesalaakso.rbb.game.TileMap;

/**
 * Handles the painting of the over layer of a map.
 * 
 * @author Vesa Laakso
 */
public class TileMapOverLayerPainter extends TileMapPainter {

	/**
	 * Constructs a new {@link TileMapOverLayerPainter} and initializes the map
	 * it draws.
	 * 
	 * @param map the map which will be drawn
	 */
	public TileMapOverLayerPainter(TileMap map) {
		this.setMap(map);
	}

	/**
	 * Paints the over layer of a map.
	 * 
	 * @see Painter#paint
	 */
	@Override
	public void paint(Graphics g, Camera cam) {
		int overlayer = this.getMap().getIndexOfOverLayer();
		super.drawLayer(overlayer, cam);
	}

}
