package com.vesalaakso.rbb.model;

import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;

import com.google.common.collect.Maps;

/**
 * Handles audio and music loading and saving.
 * 
 * @author Vesa Laakso
 */
public class AudioContainer {

	/** All music files are stored in this map */
	private Map<Audio, Music> musics = Maps.newHashMap();

	/**
	 * Initializes all the audio files.
	 * 
	 * @throws SlickException
	 *             if something went wrong with audio loading
	 */
	public void init() throws SlickException {
		musics.put(Audio.BACKGROUND, loadMusic(Audio.BACKGROUND));
	}

	/** A helper to load a music file */
	private Music loadMusic(Audio audio) throws SlickException {
		Music m;

		String filePath = "data/" + audio.fileName;

		m = new Music(filePath);

		return m;
	}

	/**
	 * Gets the special music.
	 * 
	 * @param audio
	 *            the music to get
	 * @return the music one wanted
	 */
	public Music getMusic(Audio audio) {
		Music m = musics.get(audio);

		return m;
	}
}
