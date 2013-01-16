package com.vesalaakso.rbb.model.resources;

import java.io.InputStream;
import java.net.URL;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;
import org.newdawn.slick.util.ResourceLoader;
import org.newdawn.slick.util.ResourceLocation;

import com.vesalaakso.rbb.RubberBandBall;

/**
 * A class used to load all the Tiled tile maps and to query for different
 * levels when needed.
 * 
 * @author Vesa Laakso
 */
public class TiledMapPlusContainer {

	/** Package path to maps */
	private final static String LEVELS_PACKAGE_PATH =
		"/com/vesalaakso/rbb/data/levels/";

	/** Slick <code>ResourceLocation</code> to get resources from packages */
	private final static ResourceLocation MAP_RESOURCE_LOCATION =
		new ResourceLocation() {

			@Override
			public InputStream getResourceAsStream(String ref) {
				return TiledMapPlusContainer.class
						.getResourceAsStream(LEVELS_PACKAGE_PATH + ref);
			}

			@Override
			public URL getResource(String ref) {
				return TiledMapPlusContainer.class
						.getResource(LEVELS_PACKAGE_PATH + ref);
			}
		};

	/** All the levels are stored in this array */
	private TiledMapPlus[] maps = new TiledMapPlus[RubberBandBall.LEVEL_COUNT];

	/**
	 * Initializes all the maps.
	 * 
	 * @throws SlickException
	 *             if something went wrong with map loading
	 */
	public void init() throws SlickException {
		// Add the default resource location for location loaders
		ResourceLoader.addResourceLocation(MAP_RESOURCE_LOCATION);

		// Load all levels
		for (int level = 1; level <= RubberBandBall.LEVEL_COUNT; level++) {
			try {
				maps[level - 1] = loadMap(level);
			}
			catch (SlickException e) {
				throw new SlickException("Failed to load level " + level, e);
			}
			Log.info("Loaded level " + level);
		}

		// Remove the resource location from resource loader
		ResourceLoader.removeResourceLocation(MAP_RESOURCE_LOCATION);
	}

	/** A helper to load a single map */
	private TiledMapPlus loadMap(int level) throws SlickException {
		String mapFilename = level + ".tmx";
		if (level >= 0 && level <= 9) {
			// Keep map file name zero padded if necessary
			mapFilename = "0" + mapFilename;
		}

		TiledMapPlus map = new TiledMapPlus("/" + mapFilename);
		return map;
	}

	/**
	 * Gets the map for the given level or <code>null</code>, if map for the
	 * given level was not found.
	 * 
	 * @param level
	 *            the level to get
	 * @return the map of the given level or <code>null</code> if there was none
	 *         loaded
	 */
	public TiledMapPlus getMap(int level) {
		if (level < 1 || level > RubberBandBall.LEVEL_COUNT) {
			Log.warn("Tried to get map for a non-existent level " + level);
			return null;
		}

		TiledMapPlus map = maps[level - 1];

		if (map == null) {
			Log.warn("A null map was stored in TiledMapPlusContainer for level "
					+ level);
		}

		return map;
	}
}
