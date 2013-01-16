package com.vesalaakso.rbb.model;

import org.newdawn.slick.Image;
import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.tiled.TiledMapPlus;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.model.resources.Audio;
import com.vesalaakso.rbb.model.resources.AudioContainer;
import com.vesalaakso.rbb.model.resources.Font;
import com.vesalaakso.rbb.model.resources.FontContainer;
import com.vesalaakso.rbb.model.resources.Gfx;
import com.vesalaakso.rbb.model.resources.GfxContainer;
import com.vesalaakso.rbb.model.resources.TiledMapPlusContainer;

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

	/** The graphics container images are stored in */
	private GfxContainer gfxContainer = new GfxContainer();

	/** The tiled map container maps are stored in */
	private TiledMapPlusContainer mapContainer = new TiledMapPlusContainer();

	/**
	 * Initializes the resource manager
	 * 
	 * @throws SlickException
	 *             if some resource failed to load
	 */
	public void init() throws SlickException {
		fontContainer.init();
		audioContainer.init();
		gfxContainer.init();
		mapContainer.init();
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

	/**
	 * Returns a specific sound.
	 * 
	 * @param audio
	 *            the sound to get
	 * @return the sound one wanted
	 */
	public Sound getSound(Audio audio) {
		return audioContainer.getSound(audio);
	}

	/**
	 * Returns a specific image
	 * 
	 * @param gfx
	 *            the gfx to get
	 * @return the graphics one wanted
	 */
	public Image getImage(Gfx gfx) {
		return gfxContainer.getImage(gfx);
	}

	/**
	 * Returns a specific tiled map
	 * 
	 * @param level
	 *            the level to get
	 * @return the tiled map corresponding to the given level
	 */
	public TiledMapPlus getMap(int level) {
		return mapContainer.getMap(level);
	}
}
