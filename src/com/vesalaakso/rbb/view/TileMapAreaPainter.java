package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.GravityArea;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;
import com.vesalaakso.rbb.util.Utils;

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

	/** The image used to draw gravity fields */
	private Image gravityAreaImage;

	/** The color used to draw safe areas */
	private static Color colorSafeArea = calcSafeAreaColor();

	/**
	 * Constructs a new painter and associates it with the given map. Also loads
	 * the gravity area image.
	 * 
	 * @param currentMapContainer
	 *            The {@link TileMapContainer} which will be queried to get the
	 *            current map to be drawn.
	 */
	public TileMapAreaPainter(TileMapContainer currentMapContainer) {
		this.mapContainer = currentMapContainer;

		// XXX Proper loading
		try {
			gravityAreaImage = new Image("data/levels/grav-arrow.png");
		}
		catch (SlickException e) {
			Log.warn("Failed to load image for gravity arrow", e);
		}
	}

	/** Used to initialize the color for safe areas */
	private static Color calcSafeAreaColor() {
		java.awt.Color tmpC = java.awt.Color.getHSBColor(0.3f, 0.5f, 0.3f);
		return new Color(tmpC.getRed(), tmpC.getGreen(), tmpC.getBlue());
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>true</code>, as special areas are in world coordinates.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return true;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(org.newdawn.slick.Graphics)
	 */
	@Override
	public void paint(Graphics g) {
		TileMap map = mapContainer.getMap();

		List<TileMapObject> safeAreas = map.getSafeAreas();
		TileMapObject spawnArea = map.getSpawnArea();
		TileMapObject finishArea = map.getFinishArea();

		// First things first: Set the color we will draw with.
		g.setColor(colorSafeArea);

		for (TileMapObject area : safeAreas) {
			paintArea(g, area);
		}
		paintArea(g, spawnArea);
		paintArea(g, finishArea);

		// Calculate a modulating value to change the fields colors dynamically.
		float modulate = (Utils.getTime() % 4000) / 2000.0f;

		if (modulate > 1.0f) {
			modulate = 1.0f - (modulate - 1.0f);
		}

		// Paint gravity areas
		List<GravityArea> gravityAreas = map.getGravityAreas();
		for (GravityArea area : gravityAreas) {
			Color c = area.color;
			c = c.brighter(modulate * .75f);
			g.setColor(new Color(c.r, c.g, c.b, 0.15f));
			g.drawRoundRect(area.x, area.y, area.width, area.height, 10);
			g.drawImage(gravityAreaImage, area.x, area.y, c);
		}
	}

	/**
	 * A helper which draws a single <code>TileMapObject</code> with the current
	 * graphics context properly set.
	 */
	private void paintArea(Graphics g, TileMapObject area) {
		g.fillRect(area.x, area.y, area.width, area.height);
	}

}
