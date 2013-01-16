package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;

import com.vesalaakso.rbb.model.GravityArea;
import com.vesalaakso.rbb.model.ResourceManager;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.model.TileMapObject;
import com.vesalaakso.rbb.model.resources.Gfx;
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
	 * @see com.vesalaakso.rbb.view.Painter#paint(Graphics, ResourceManager)
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		TileMap map = mapContainer.getMap();

		List<TileMapObject> safeAreas = map.getSafeAreas();
		TileMapObject spawnArea = map.getSpawnArea();
		TileMapObject finishArea = map.getFinishArea();

		// Calculate a modulating value to change the area colors dynamically.
		float modulate = (Utils.getTime() % 4000) / 2000.0f;

		if (modulate > 1.0f) {
			modulate = 1.0f - (modulate - 1.0f);
		}

		for (TileMapObject area : safeAreas) {
			paintArea(g, area, modulate);
		}
		paintArea(g, spawnArea, modulate);
		paintArea(g, finishArea, modulate);

		// Paint gravity areas
		List<GravityArea> gravityAreas = map.getGravityAreas();
		Image gravityAreaImage = resManager.getImage(Gfx.MAP_GRAV_ARROW);
		for (GravityArea area : gravityAreas) {
			Color c = area.getColor();
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
	private void paintArea(Graphics g, TileMapObject area, float modulate) {
		Color c = area.getColor();
		c = c.brighter(modulate * .75f);
		g.setColor(new Color(c.r, c.g, c.b, 0.33f));
		g.fillRect(area.x, area.y, area.width, area.height);
	}

}
