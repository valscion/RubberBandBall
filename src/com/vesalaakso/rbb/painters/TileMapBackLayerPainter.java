package com.vesalaakso.rbb.painters;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.game.Camera;
import com.vesalaakso.rbb.game.TileMap;

/**
 * Handles the painting of the back layer of a map.
 * 
 * @author Vesa Laakso
 */
public class TileMapBackLayerPainter extends TileMapPainter {

	/**
	 * Constructs a new {@link TileMapBackLayerPainter} and initializes the map
	 * it draws.
	 * 
	 * @param map the map which will be drawn
	 */
	public TileMapBackLayerPainter(TileMap map) {
		this.setMap(map);
	}

	/**
	 * Paints the back layer of a map.
	 * 
	 * @see Painter#paint
	 */
	@Override
	public void paint(Graphics g, Camera cam) {
		int backlayer = this.getMap().getIndexOfBackLayer();
		super.drawLayer(backlayer, cam);
	}

}
