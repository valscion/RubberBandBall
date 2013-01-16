package com.vesalaakso.rbb.model.resources;

import java.io.InputStream;
import java.net.URL;

import org.newdawn.slick.util.ResourceLocation;

/**
 * A <code>ResourceLocation</code> for all basic resources. Adding this to the
 * <code>ResourceLoader</code> (given by Swing), one can load resources from
 * separate packages nicely.
 * 
 * @author Vesa
 * 
 */
public class RbbResourceLocation implements ResourceLocation {

	/** Package to use as a root */
	private static final String PACKAGE = "/com/vesalaakso/rbb/data/";

	@Override
	public InputStream getResourceAsStream(String ref) {
		return getClass().getResourceAsStream(PACKAGE + ref);
	}

	@Override
	public URL getResource(String ref) {
		return getClass().getResource(PACKAGE + ref);
	}

}
