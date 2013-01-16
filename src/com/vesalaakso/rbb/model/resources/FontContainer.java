package com.vesalaakso.rbb.model.resources;

import java.awt.Color;
import java.util.List;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.font.effects.Effect;
import org.newdawn.slick.font.effects.ShadowEffect;
import org.newdawn.slick.util.Log;

import com.google.common.collect.Maps;

/**
 * A class used to load all the fonts and to query for different fonts when
 * needed.
 * 
 * @author Vesa Laakso
 */
public class FontContainer {

	/** All the fonts are stored in this map. */
	private Map<Font, UnicodeFont> fonts = Maps.newHashMap();

	/**
	 * Initializes all the fonts.
	 * 
	 * @throws SlickException
	 *             if something went wrong with font loading
	 */
	public void init() throws SlickException {
		UnicodeFont font;
		List<Effect> fEffects;

		// Menu item
		font = loadFont(Font.MENU_ITEM);
		fEffects = font.getEffects();
		fEffects.add(new ColorEffect(Color.getHSBColor(0.55f, 1.0f, 0.75f)));
		font.addAsciiGlyphs();
		font.loadGlyphs();
		fonts.put(Font.MENU_ITEM, font);

		// Menu item hilighted
		font = loadFont(Font.MENU_ITEM_HILIGHTED);
		fEffects = font.getEffects();
		fEffects.add(new ShadowEffect(new Color(255, 255, 255), 1, 1, 0.75f));
		fEffects.add(new ColorEffect(Color.getHSBColor(0.55f, 1.0f, 0.75f)));
		font.addAsciiGlyphs();
		font.loadGlyphs();
		fonts.put(Font.MENU_ITEM_HILIGHTED, font);

		// Regular text
		font = loadFont(Font.REGULAR);
		fEffects = font.getEffects();
		fEffects.add(new ColorEffect(Color.getHSBColor(0.0f, 0.0f, 0.95f)));
		font.addAsciiGlyphs();
		font.loadGlyphs();
		fonts.put(Font.REGULAR, font);

		// Scores in game
		font = loadFont(Font.SCORE);
		fEffects = font.getEffects();
		fEffects.add(new ColorEffect(Color.getHSBColor(0.0f, 0.0f, 0.95f)));
		font.addAsciiGlyphs();
		font.loadGlyphs();
		fonts.put(Font.SCORE, font);
	}

	/**
	 * Returns <code>true</code> if the font container has been initialized.
	 * 
	 * @return <code>true</code>, the font container has been initialized
	 */
	public boolean isInitialized() {
		return !fonts.isEmpty();
	}

	/** A helper to load a unicode font */
	private UnicodeFont loadFont(Font font) throws SlickException {
		UnicodeFont f;

		String filePath = "data/fonts/" + font.fileName;

		f = new UnicodeFont(filePath, font.size, false, false);

		return f;
	}

	/**
	 * Gets the special font.
	 * 
	 * @param font
	 *            to get
	 * @return the font one wanted
	 */
	public UnicodeFont getFont(Font font) {
		UnicodeFont ret = fonts.get(font);
		if (ret == null) {
			Log.warn("FontContainer did not contain font " + font);
		}
		return ret;
	}
}
