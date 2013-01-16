package com.vesalaakso.rbb.model.resources;

/**
 * All audio files mapped to a sane enumeration
 * 
 * @author Vesa Laakso
 */
public enum Audio {
	/** Background music */
	MUSIC_BACKGROUND("darkware_-_avenger_theme.xm"),
	/** Sound effect for small speed hit */
	SOUND_HIT_SMALL("hit1.ogg"),
	/** Sound effect for regular speed hit */
	SOUND_HIT_NORMAL("hit2.ogg"),
	/** Sound effect for high speed hit */
	SOUND_HIT_BIG("hit3.ogg");

	/** The string for the file name of audio file */
	public final String fileName;

	/**
	 * Constructs the enum and associates it with the given audio file name.
	 * 
	 * @param fileName
	 *            the audio file name to associate with the enum
	 */
	Audio(String fileName) {
		this.fileName = fileName;
	}
}
