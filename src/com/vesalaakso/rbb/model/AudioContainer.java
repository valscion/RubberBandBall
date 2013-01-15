package com.vesalaakso.rbb.model;

import java.util.Map;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import com.google.common.collect.Maps;

/**
 * Handles audio and music loading and saving.
 * 
 * @author Vesa Laakso
 */
public class AudioContainer {

	/** All music files are stored in this map */
	private Map<Audio, Music> musics = Maps.newHashMap();

	/** All sound effect files are stored in this map */
	private Map<Audio, Sound> sounds = Maps.newHashMap();

	/**
	 * Initializes all the audio files.
	 * 
	 * @throws SlickException
	 *             if something went wrong with audio loading
	 */
	public void init() throws SlickException {
		musics.put(Audio.MUSIC_BACKGROUND, loadMusic(Audio.MUSIC_BACKGROUND));
		sounds.put(Audio.SOUND_HIT_SMALL, loadSound(Audio.SOUND_HIT_SMALL));
		sounds.put(Audio.SOUND_HIT_NORMAL, loadSound(Audio.SOUND_HIT_NORMAL));
		sounds.put(Audio.SOUND_HIT_BIG, loadSound(Audio.SOUND_HIT_BIG));
	}

	/** A helper to load a music file */
	private Music loadMusic(Audio audio) throws SlickException {
		Music m;
		String filePath = "data/sounds/" + audio.fileName;
		m = new Music(filePath);
		return m;
	}

	/** A helper to load a sound file */
	private Sound loadSound(Audio audio) throws SlickException {
		Sound s;
		String filePath = "data/sounds/" + audio.fileName;
		s = new Sound(filePath);
		return s;
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

	/**
	 * Gets the special sound.
	 * 
	 * @param audio
	 *            the sound to get
	 * @return the sound one wanted
	 */
	public Sound getSound(Audio audio) {
		Sound s = sounds.get(audio);
		return s;
	}
}
