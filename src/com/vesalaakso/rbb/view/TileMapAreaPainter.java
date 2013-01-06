package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapArea;
import com.vesalaakso.rbb.model.TileMapContainer;

/**
 * A {@link Painter} used to draw the special areas of the map in a pretty way.
 * 
 * @author Vesa Laakso
 */
public class TileMapAreaPainter implements Painter {

	/**
	 * The {@link TileMapContainer} which will be queried to get the current map
	 * to be drawn.
	 */
	private TileMapContainer mapContainer;

	/** The color used to draw safe areas */
	private static Color colorSafeArea = calcSafeAreaColor();

	/**
	 * Constructs a new painter and associates it with the given map.
	 * 
	 * @param currentMapContainer
	 *            The {@link TileMapContainer} which will be queried to get the
	 *            current map to be drawn.
	 */
	public TileMapAreaPainter(TileMapContainer currentMapContainer) {
		this.mapContainer = currentMapContainer;
	}

	/** Used to initialize the color for safe areas */
	private static Color calcSafeAreaColor() {
		java.awt.Color tmpC = java.awt.Color.getHSBColor(0.3f, 0.5f, 0.3f);
		return new Color(tmpC.getRed(), tmpC.getGreen(), tmpC.getBlue());
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>false</code>, as special areas are drawn precisely and the
	 *         drawing coordinates are calculated by hand.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return false;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(org.newdawn.slick.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		TileMap map = mapContainer.getMap();

		List<TileMapArea> safeAreas = map.getSafeAreas();
		TileMapArea spawnArea = map.getSpawnArea();
		TileMapArea finishArea = map.getFinishArea();

		// First things first: Set the color we will draw with.
		g.setColor(colorSafeArea);

		for (TileMapArea area : safeAreas) {
			paintArea(g, area);
		}
		paintArea(g, spawnArea);
		paintArea(g, finishArea);
	}

	/**
	 * A helper which draws a single TileMapArea with the current graphics
	 * context properly set.
	 */
	private void paintArea(Graphics g, TileMapArea area) {
		Camera cam = Camera.get();

		g.fillRect(area.x - cam.getX(), area.y - cam.getY(), area.width,
				area.height);
	}

}
