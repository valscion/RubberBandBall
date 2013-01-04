package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.WorldListener;

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

	/**
	 * Constructs a new <code>PlayerCollisionListener</code> and lets it know
	 * what physics simulation handles the physics and who is the
	 * <code>Player</code> behind all the collisions.
	 * 
	 * @param physics
	 *            the physics simulation where The Magic (tm) happens
	 * @param player
	 *            the {@link Player}-model for the collisions.
	 */
	public PlayerCollisionListener(Physics physics, Player player) {
		this.physics = physics;
		this.player = player;
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#collided(org.newdawn.fizzy.CollisionEvent)
	 */
	@Override
	public void collided(CollisionEvent event) {
		// TODO: LOTS OF PARTICLES!!!!111oneone
		System.out.printf("Thuck @ (%.1f; %.1f) or (%.1f; %.1f)%n",
				player.getX(), player.getY(),
				event.getBodyB().getX(), event.getBodyB().getY());
	}

	/**
	 * @see org.newdawn.fizzy.WorldListener#separated(org.newdawn.fizzy.CollisionEvent)
	 */
	@Override
	public void separated(CollisionEvent event) {
		System.out.printf("Bloink @ (%.1f; %.1f) or (%.1f; %.1f)%n",
				player.getX(), player.getY(),
				event.getBodyB().getX(), event.getBodyB().getY());
	}

}
