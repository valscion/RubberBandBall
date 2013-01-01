package com.vesalaakso.rbb.model;

import org.newdawn.slick.tiled.TiledMap;

/**
 * Holds information about areas specified in tile maps.
 * 
 * @author Vesa Laakso
 */
public class TileMapArea {

	/** The name of the area. */
	public final String name;

	/** The type of the area. */
	public final String type;

	/** The x-coordinate of the left side. */
	public final int x;

	/** The y-coordinate of the top side. */
	public final int y;

	/** The width of the area. */
	public final int width;

	/** The height of the area. */
	public final int height;

	/**
	 * Constructs a new <code>TileMapArea</code> which stores the information
	 * from the given <code>TiledMap</code> and the given object.
	 * 
	 * @param tmap
	 *            the TiledMap to get information from
	 * @param groupID
	 *            the ID of the group where the object resides
	 * @param objectID
	 *            the ID of the object in the given group
	 */
	public TileMapArea(TiledMap tmap, int groupID, int objectID) {
		name = tmap.getObjectName(groupID, objectID);
		type = tmap.getObjectType(groupID, objectID);
		x = tmap.getObjectX(groupID, objectID);
		y = tmap.getObjectY(groupID, objectID);
		width = tmap.getObjectWidth(groupID, objectID);
		height = tmap.getObjectWidth(groupID, objectID);
		
		// TODO: If the area has properties, add them as well.
	}

	/**
	 * Gets the <code>String</code> representation of this
	 * <code>TileMapArea</code>.
	 */
	@Override
	public String toString() {
		String str = "TileMapArea={"
				+ "name:" + name
				+ ",type:" + type
				+ ",x:" + x
				+ ",y:" + y
				+ ",width:" + width
				+ ",height:" + height
				+ "}";
		return str;
	}

}
