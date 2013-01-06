package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.Body;
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
	 * The physics body where the player last collided to but has not yet been
	 * separated from
	 */
	private Body<?> lastCollisionBody;

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

		lastCollisionBody = getCollisionBody(event);
		physics.startSimulatingFriction(lastCollisionBody);
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#separated
	 */
	@Override
	public void separated(CollisionEvent event) {
		Body<?> otherBody = getCollisionBody(event);
		
		if (otherBody == lastCollisionBody) {
			physics.stopSimulatingFriction();
		}
	}

	/**
	 * A helper method which returns the body which was not the player from the
	 * given <code>CollisionEvent</code>.
	 * 
	 * @param event
	 *            CollisionEvent to investigate
	 * 
	 * @return the body taking part in the collision that was not the body of
	 *         the player.
	 */
	private Body<?> getCollisionBody(CollisionEvent event) {
		Body<?> player = physics.getPlayerBody();
		
		Body<?> ret = event.getBodyA();
		if (ret == player) {
			ret = event.getBodyB();
		}
		return ret;
	}

}
