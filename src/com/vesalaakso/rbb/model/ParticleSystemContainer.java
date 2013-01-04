package com.vesalaakso.rbb.model;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.particles.ParticleSystem;

/**
 * A class responsible for all the particle systems in the game.
 * 
 * @author Vesa Laakso
 */
public class ParticleSystemContainer {

	/** A list of all the added particle systems, backed by a LinkedList. */
	List<ParticleSystem> systems = new LinkedList<ParticleSystem>();

	/**
	 * Constructs a new <code>ParticleSystemContainer</code>.
	 */
	public ParticleSystemContainer() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Get the list of particle systems. The list will be backed by a
	 * <code>LinkedList</code>.
	 * 
	 * @return list of particle systems
	 */
	public List<ParticleSystem> getSystems() {
		return systems;
	}

}
