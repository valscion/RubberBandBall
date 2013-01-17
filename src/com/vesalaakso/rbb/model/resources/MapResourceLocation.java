package com.vesalaakso.rbb.model.resources;

import java.io.InputStream;
import java.net.URL;

import org.newdawn.slick.util.ResourceLocation;

/**
 * A <code>ResourceLocation</code> for all map resources. Adding this to the
 * <code>ResourceLoader</code> (given by Slick), one can load resources from
 * separate packages nicely.
 * 
 * @author Vesa
 * 
 */
public class MapResourceLocation implements ResourceLocation {

	/** Package to use as a root */
	private static final String PACKAGE = "com/vesalaakso/rbb/data/levels/";

	@Override
	public InputStream getResourceAsStream(String ref) {
		return ClassLoader.getSystemResourceAsStream(PACKAGE + ref);
	}

	@Override
	public URL getResource(String ref) {
		return ClassLoader.getSystemResource(PACKAGE + ref);
	}

}
