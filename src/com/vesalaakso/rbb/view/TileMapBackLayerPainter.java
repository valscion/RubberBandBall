package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.TileMap;

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
	public void paint(Graphics g) {
		int backlayer = this.getMap().getIndexOfBackLayer();
		super.drawLayer(backlayer);
	}

}
