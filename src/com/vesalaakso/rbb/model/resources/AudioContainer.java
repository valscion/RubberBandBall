package com.vesalaakso.rbb.model.resources;

import java.util.EnumMap;

import org.newdawn.slick.Music;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Handles audio and music loading and saving.
 * 
 * @author Vesa Laakso
 */
public class AudioContainer {

	/** All music files are stored in this map */
	private EnumMap<Audio, Music> musics =
			new EnumMap<Audio, Music>(Audio.class);

	/** All sound effect files are stored in this map */
	private EnumMap<Audio, Sound> sounds =
			new EnumMap<Audio, Sound>(Audio.class);

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
		m = new Music(ResourceLoader.getResource(audio.fileName));
		return m;
	}

	/** A helper to load a sound file */
	private Sound loadSound(Audio audio) throws SlickException {
		Sound s;
		s = new Sound(ResourceLoader.getResource(audio.fileName));
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
