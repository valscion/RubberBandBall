package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.slick.tiled.GroupObject;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.ParticleManager;
import com.vesalaakso.rbb.model.Physics;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMapObject;

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
	 * Try to fix the fact that somehow the player can get stuck bouncing
	 * forever. This attribute contains the timestamp of when the last bounce
	 * happened to the same body.
	 */
	private long lastBounceFromSameBody;

	/**
	 * Still trying to fix the infinite bouncing problem. This attribute
	 * contains the count of how many times the player has bounced in a small
	 * time from the same body.
	 */
	private int bounceCountFromSameBody;

	/**
	 * This constant defines how many bounces there can be at maximum in a small
	 * time before the player is forcefully stopped from bouncing.
	 */
	private final static int MAX_BOUNCE_COUNT_FROM_SAME_BODY = 8;

	/** This constant defines the delta to check bounce fixing against */
	private final static long BOUNCE_BUG_MIN_TIME_DIFF = 150;

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
		TileMapObject tile = physics.getTileMapObject(lastCollisionBody);

		if (tile.objectType == GroupObject.ObjectType.RECTANGLE) {
			// Simulate friction for objects that are below us.
			if (player.getY() < lastCollisionBody.getY()) {
				physics.startSimulatingFriction(lastCollisionBody);
				// Also if the player has bounced waaay too many times, stop it.
				if (bounceCountFromSameBody >= MAX_BOUNCE_COUNT_FROM_SAME_BODY) {
					physics.stopPlayerRising();
				}
			}
		}
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#separated
	 */
	@Override
	public void separated(CollisionEvent event) {
		Body<?> otherBody = getCollisionBody(event);

		if (otherBody == lastCollisionBody) {
			// Stop friction simulation as player is no longer on ground.
			physics.stopSimulatingFriction();
			// Try to fix the infinite bouncing problem.
			long timeNow = RubberBandBall.getContainer().getTime();
			if (lastBounceFromSameBody == 0
					|| lastBounceFromSameBody + BOUNCE_BUG_MIN_TIME_DIFF > timeNow) {
				System.out.println("Tock, " + bounceCountFromSameBody);
				lastBounceFromSameBody = timeNow;
				bounceCountFromSameBody++;
			}
			else {
				lastBounceFromSameBody = 0;
				bounceCountFromSameBody = 0;
			}
		}
		else {
			lastBounceFromSameBody = 0;
			bounceCountFromSameBody = 0;
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
