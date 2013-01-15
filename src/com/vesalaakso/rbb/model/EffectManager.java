package com.vesalaakso.rbb.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.controller.Updateable;

/**
 * A class responsible for all the nice effects in the game.
 * 
 * @author Vesa Laakso
 */
public class EffectManager implements Updateable, Resetable {

	/** The resource manager to query for resources. */
	private final ResourceManager resourceManager;

	/** Maximum amount of explosions */
	private static final int MAX_EXPLOSIONS = 40;

	/** 40 explosions running at max, stored here. */
	private List<ParticleSystem> explosionSystems =
		new ArrayList<ParticleSystem>(MAX_EXPLOSIONS);

	/** Amount of running explosion systems */
	private int runningExplosionSystems = 0;

	/** The ID for the next explosionSystem to initialize. */
	private int nextExplosionID = 0;

	/**
	 * Constructs a new <code>EffectManager</code> and associates it with the
	 * given <code>ResourceManager</code>.
	 * 
	 * @param resourceManager
	 *            the resource manager to query for sound fx
	 */
	public EffectManager(ResourceManager resourceManager) {
		String dir = "data/slick_particle_effects/";
		String file = "explosion.xml";
		ParticleSystem.setRelativePath(dir);
		try {
			ParticleSystem explosions =
				ParticleIO.loadConfiguredSystem(dir + file);
			explosionSystems.add(explosions);
			for (int i = 0; i < MAX_EXPLOSIONS - 1; i++) {
				explosionSystems.add(explosions.duplicate());
			}
		}
		catch (IOException e) {
			Log.error("Could not load emitter explosion.xml", e);
		}
		catch (SlickException e) {
			Log.error("Couldn't duplicate explosions ParticleSystem", e);
		}

		this.resourceManager = resourceManager;
	}

	/**
	 * Updates the particle systems.
	 */
	@Override
	public void update(int delta) {
		for (int i = 0; i < runningExplosionSystems; i++) {
			ParticleSystem system = explosionSystems.get(i);
			system.update(delta);
		}
	}

	/**
	 * Adds an explosion emitter to the given coordinates (in world space)
	 * 
	 * @param worldX
	 *            the x-coordinate in the world to add the explosion into
	 * @param worldY
	 *            the y-coordinate in the world to add the explosion into
	 */
	public void addExplosionEmitter(float worldX, float worldY) {
		// Get the explosion system to set active
		ParticleSystem system = explosionSystems.get(nextExplosionID);

		// Set the systems position and reset it
		system.setPosition(worldX, worldY);
		system.reset();

		// We have a new running system.
		if (runningExplosionSystems < MAX_EXPLOSIONS) {
			runningExplosionSystems++;
		}

		// Next time, get the next explosion.
		nextExplosionID++;
		if (nextExplosionID == MAX_EXPLOSIONS) {
			nextExplosionID = 0;
		}
	}

	/**
	 * Get the list of particle systems. The list will be backed by a
	 * <code>LinkedList</code>.
	 * 
	 * @return list of particle systems
	 */
	public List<ParticleSystem> getParticleSystems() {
		List<ParticleSystem> systems = new LinkedList<ParticleSystem>();
		for (int i = 0; i < runningExplosionSystems; i++) {
			systems.add(explosionSystems.get(i));
		}
		return systems;
	}

	/** Method to reset the systemz */
	@Override
	public void reset() {
		for (int i = 0; i < runningExplosionSystems; i++) {
			explosionSystems.get(i).reset();
		}
		runningExplosionSystems = 0;
		nextExplosionID = 0;
	}

}
