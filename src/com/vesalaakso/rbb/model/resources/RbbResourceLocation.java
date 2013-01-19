package com.vesalaakso.rbb.model.resources;

import java.io.InputStream;
import java.net.URL;

import org.newdawn.slick.util.ResourceLocation;

/**
 * A <code>ResourceLocation</code> for all basic resources. Adding this to the
 * <code>ResourceLoader</code> (given by Slick), one can load resources from
 * separate packages nicely.
 * 
 * @author Vesa
 * 
 */
public class RbbResourceLocation implements ResourceLocation {

	/** Package to use as a root */
	private static final String PACKAGE = "com/vesalaakso/rbb/data/";

	/** Class loader to use when fetching stuff */
	private final ClassLoader loader = Thread.currentThread()
			.getContextClassLoader();

	@Override
	public InputStream getResourceAsStream(String ref) {
		return loader.getResourceAsStream(PACKAGE + ref);
	}

	@Override
	public URL getResource(String ref) {
		return loader.getResource(PACKAGE + ref);
	}

}
