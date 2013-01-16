package com.vesalaakso.rbb.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.controller.Resetable;
import com.vesalaakso.rbb.controller.Updateable;
import com.vesalaakso.rbb.model.resources.Audio;
import com.vesalaakso.rbb.util.Utils;

/**
 * A class responsible for all the nice effects in the game.
 * 
 * @author Vesa Laakso
 */
public class EffectManager implements Updateable, Resetable {

	/**
	 * When a collision force smaller than this value is given for effectifying
	 * collisions, simulate a small collision.
	 */
	private static final float COLLISION_LIMIT_SMALL = 20.0f;

	/**
	 * When a collision force bigger than {@link #COLLISION_LIMIT_SMALL} and
	 * smaller than this value is given for effectifying collisions, simulate a
	 * normal sized collision.
	 */
	private static final float COLLISION_LIMIT_NORMAL = 40.0f;

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
			explosions.setVisible(false);
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
			if (system.isVisible() && system.getParticleCount() == 0) {
				// It has most likely stopped. Force it. Stops it from bugging.
				system.setVisible(false);
			}
		}
	}

	/**
	 * Adds effects for a collision in the given world coordinates and the given
	 * force.
	 * 
	 * @param worldX
	 *            the x-coordinate in the world where the collision happened
	 * @param worldY
	 *            the y-coordinate in the world where the collision happened
	 * @param force
	 *            the force of the collision
	 */
	public void addCollisionEffect(float worldX, float worldY, float force) {
		if (force > COLLISION_LIMIT_NORMAL) {
			// BOOM!
			Sound boom = resourceManager.getSound(Audio.SOUND_HIT_BIG);
			float pitch = 1.0f;
			float volume = (force / COLLISION_LIMIT_NORMAL) * 0.5f;
			boom.play(pitch, Utils.clamp(volume, 0.5f, 1.5f));
			addExplosionEmitter(worldX, worldY);
		}
		else if (force > COLLISION_LIMIT_SMALL) {
			// A normal hit sound
			Sound clank = resourceManager.getSound(Audio.SOUND_HIT_NORMAL);
			float pitch = 1.0f;
			float volume = (force / COLLISION_LIMIT_SMALL) * 0.5f;
			clank.play(pitch, Utils.clamp(volume, 0.5f, 1.5f));
		}
		else {
			// A small hit sound
			Sound hit = resourceManager.getSound(Audio.SOUND_HIT_SMALL);
			float pitch = 1.0f;
			float volume = (force / COLLISION_LIMIT_SMALL) * 2.0f;
			hit.play(pitch, Utils.clamp(volume, 0.5f, 1.5f));
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
	private void addExplosionEmitter(float worldX, float worldY) {
		// Get the explosion system to set active
		ParticleSystem system = explosionSystems.get(nextExplosionID);

		// Set the systems position and reset it and show it.
		system.reset();
		system.setPosition(worldX, worldY);
		system.setVisible(true);

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
			ParticleSystem system = explosionSystems.get(i);
			system.reset();
			system.setVisible(false);
		}
		runningExplosionSystems = 0;
		nextExplosionID = 0;
	}

}
