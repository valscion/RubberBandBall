package com.vesalaakso.rbb.model;

import java.util.List;
import java.util.Properties;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.tiled.GroupObject;

import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * Represents a single GroupObject, in an easier-to-digest way.
 * 
 * @author Vesa Laakso
 */
public class TileMapObject {

	/** The index of this object */
	public final int index;
	/** The name of this object */
	public final String name;
	/** The type of this object */
	public final TileMapObjectType type;
	/** The object type of this object */
	public final GroupObject.ObjectType objectType;
	/** The x-coordinate of this object */
	public final int x;
	/** The y-coordinate of this object */
	public final int y;
	/** The width of this object */
	public final int width;
	/** The height of this object */
	public final int height;
	/** the properties of this object */
	public final Properties props;

	/** The map this object resides in */
	private final TileMap map;
	/** The underlying GroupObject */
	private final GroupObject groupObject;

	/**
	 * Constructs a new object based on the given <code>GroupObject</code>.
	 * 
	 * @param obj
	 *            the <code>GroupObject</code> to base this new object on
	 * @param map
	 *            the <code>TileMap</code> this <code>TileMapObject</code>
	 *            resides in
	 * @throws MapException
	 *             if the type of this object was set and was unknown.
	 */
	public TileMapObject(GroupObject obj, TileMap map) throws MapException {
		groupObject = obj;

		index = obj.index;
		name = obj.name;
		if (obj.type != null && !obj.type.isEmpty()) {
			try {
				type = TileMapObjectType.valueOf(obj.type.toUpperCase());
			}
			catch (IllegalArgumentException e) {
				throw new MapException("Unknown object type: " + obj.type, e);
			}
		}
		else {
			type = TileMapObjectType.EMPTY;
		}
		objectType = obj.getObjectType();
		x = obj.x;
		y = obj.y;
		width = obj.width;
		height = obj.height;
		props = obj.props;
		this.map = map;
	}

	/**
	 * Finds the object above this <code>TileMapObject</code> in the map. This
	 * doesn't search for collision objects and this can't be used to search for
	 * objects above a polygon.
	 * 
	 * @return the object above this one or <code>null</code> if none was found.
	 */
	public TileMapObject findObjectAbove() {
		if (map == null || this.objectType == GroupObject.ObjectType.POLYGON) {
			return null;
		}

		// First look if there's a safe area above us
		List<TileMapObject> safeAreas = map.getSafeAreas();
		for (TileMapObject area : safeAreas) {
			if (this.isBelow(area)) {
				return area;
			}
		}

		// Then look for trigger areas
		List<TileMapObject> triggerAreas = map.getTriggerAreas();
		for (TileMapObject area : triggerAreas) {
			if (this.isBelow(area)) {
				return area;
			}
		}

		// Then we look if it was the spawn area.
		TileMapObject spawnArea = map.getSpawnArea();
		if (this.isBelow(spawnArea)) {
			return spawnArea;
		}

		// And finally the finish area, no more.
		TileMapObject finishArea = map.getFinishArea();
		if (this.isBelow(finishArea)) {
			return finishArea;
		}

		// If none was found, it's null time.
		return null;
	}

	/** Used to check whether the given object was directly above this one */
	private boolean isBelow(TileMapObject other) {
		int yDiff = this.y - other.y - other.height;
		if (yDiff == 0) {
			int left1 = this.x;
			int left2 = other.x;
			int right1 = this.x + this.width - 1;
			int right2 = other.x + other.width - 1;
			
			// Check if other goes over from right
			if (right1 < left2) {
				return false;
			}
			// Check if other goes over from left
			if (left1 > right2) {
				return false;
			}
		}
		// Only directly above the tiles can there be one to find
		return false;
	}

	/**
	 * Returns the <code>Polygon</code> used to construct a polygon shape, if
	 * this shape is indeed a polygon.
	 * 
	 * @return <code>Polygon</code> constructing this object
	 * @throws MapException
	 *             if the shape was not a polygon
	 */
	public Polygon getPolygon() throws MapException {
		if (objectType != GroupObject.ObjectType.POLYGON) {
			String str =
				String.format(
						"The object \"%s\" at (%d, %d) was not a polygon!",
						name, x, y);
			throw new MapException(str);
		}

		try {
			return groupObject.getPolygon();
		}
		catch (SlickException e) {
			throw new MapException(String.format(
					"Couldn't get polygon for the object \"%s\" at (%d, %d)",
					name, x, y), e);
		}
	}

	/**
	 * Gets the <code>String</code> representation of this
	 * <code>TileMapObject</code>.
	 */
	@Override
	public String toString() {
		String str =
			"TileMapObject={" + "index:" + index + ",name:" + name + ",type:"
					+ type + ",objectType:" + objectType + ",x:" + x + ",y:"
					+ y + ",width:" + width + ",height:" + height + ",props:"
					+ props + "}";
		return str;
	}
}
