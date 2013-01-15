package com.vesalaakso.rbb.model;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.util.Log;

/**
 * Handles loading and storing of different resources.
 * 
 * @author Vesa Laakso
 */
public class ResourceManager {

	/** The font container fonts are stored in */
	private FontContainer fontContainer = new FontContainer();

	/** The audio container musics and sound effects are stored in */
	private AudioContainer audioContainer = new AudioContainer();

	/**
	 * Initializes the resource manager
	 * 
	 * @throws SlickException
	 *             if some resource failed to load
	 */
	public void init() throws SlickException {
		fontContainer.init();
		audioContainer.init();
		Log.info("Resource Manager initialized successfully");
	}

	/**
	 * Returns a specific font.
	 * 
	 * @param font
	 *            the font to get
	 * @return the font the user wanted
	 */
	public UnicodeFont getFont(Font font) {
		return fontContainer.getFont(font);
	}

	/**
	 * Returns a specific music.
	 * 
	 * @param audio
	 *            the {@link Audio} mapped to the music to return
	 * @return the <code>Music</code> caller wanted.
	 */
	public Music getMusic(Audio audio) {
		return audioContainer.getMusic(audio);
	}
}
