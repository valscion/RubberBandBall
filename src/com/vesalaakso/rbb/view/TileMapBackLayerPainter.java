package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.TileMapContainer;

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
	 * @param mapContainer
	 *            The {@link TileMapContainer} which will be queried to get the
	 *            current map to be drawn.
	 */
	public TileMapBackLayerPainter(TileMapContainer mapContainer) {
		super(mapContainer);
	}

	/**
	 * Paints the back layer of a map.
	 * 
	 * @see Painter#paint
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		int backlayer = this.getMap().getIndexOfBackLayer();
		super.drawLayer(backlayer, g);
	}

}
