package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.WorldListener;

import com.vesalaakso.rbb.model.ParticleManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;

/**
 * Listens for collisions where the colliding object is the player.
 * 
 * @author Vesa Laakso
 */
public class PlayerCollisionListener implements WorldListener {

	/** The physics simulation where to get The Magic (tm) */
	private Physics physics;

	/** The player who is colliding */
	private Player player;

	/** Ooh, we can create particles, yes! */
	private ParticleManager particleManager;

	/**
	 * Constructs a new <code>PlayerCollisionListener</code> and lets it know
	 * what physics simulation handles the physics and who is the
	 * <code>Player</code> behind all the collisions.
	 * 
	 * @param physics
	 *            the physics simulation where The Magic (tm) happens
	 * @param player
	 *            the {@link Player}-model for the collisions
	 * @param particleManager
	 *            the ParticleManager which will be used to create particles
	 *            when needed
	 */
	public PlayerCollisionListener(Physics physics, Player player,
			ParticleManager particleManager) {
		this.physics = physics;
		this.player = player;
		this.particleManager = particleManager;
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#collided
	 */
	@Override
	public void collided(CollisionEvent event) {
		// Particles!
		particleManager.addExplosionEmitter(player.getX(), player.getY());

		// physics.stopPlayer();
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#separated
	 */
	@Override
	public void separated(CollisionEvent event) {
		// No-op
	}

}
