package com.vesalaakso.rbb.model;

/**
 * All audio files mapped to a sane enumeration
 * 
 * @author Vesa Laakso
 */
public enum Audio {
	/** Background music */
	BACKGROUND("darkware_-_avenger_theme.xm");

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
