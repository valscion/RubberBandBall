package com.vesalaakso.rbb.model;

/**
 * Identifies the different types of tile map objects.
 * 
 * @author Vesa Laakso
 */
public enum TileMapObjectType {
	/** No type set */
	EMPTY,
	/** Area where player can spawn */
	SPAWN,
	/** Area that identifies the finish */
	FINISH,
	/** Area where the player can relaunch his ball */
	SAFE,
	/** Area which triggers an action */
	TRIGGER,
	/** Area which is used to do stuff when moving from level to another */
	TRANSITION,
	/** Area which responds to a triggered action */
	RESPONSIVE,
	/** An area representing a graphics detail */
	GRAPHIC,
	/** A gravity changing field. */
	GRAVITY;
}
