package com.vesalaakso.rbb.model;

import java.util.List;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;

/**
 * Class for handling the interaction with the physics engine, fizzy.
 * 
 * @author Vesa Laakso
 */
public class Physics {
	
	/** Default gravity */
	private static final float DEFAULT_GRAVITY = 9.81f;
	
	/** The <code>World</code> in which The Magic (tm) happens. */
	private World world;
	
	/** The bodies associated with the map. */
	private List<Body<Shape>> mapBodies;
	
	/**
	 * Constructs the physics engine and boots it up.
	 */
	public Physics() {
		world = new World(DEFAULT_GRAVITY);
	}
	
	/** Makes the physics engine tick. Call on every update. */
	public void update() {
		world.update(1/20f);
	}
	
	/**
	 * Adds collision shapes from the given map to the world.
	 */
	public void addCollidablesFromMap(TileMap map) {
		// TODO
	}
}
