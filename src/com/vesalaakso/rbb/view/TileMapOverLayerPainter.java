package com.vesalaakso.rbb.view;

import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.TileMapContainer;

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
	 * @param mapContainer
	 *            The {@link TileMapContainer} which will be queried to get the
	 *            current map to be drawn.
	 */
	public TileMapOverLayerPainter(TileMapContainer mapContainer) {
		super(mapContainer);
	}

	/**
	 * Paints the over layer of a map.
	 * 
	 * @see Painter#paint
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		int overlayer = this.getMap().getIndexOfOverLayer();
		super.drawLayer(overlayer, g);
	}

}
