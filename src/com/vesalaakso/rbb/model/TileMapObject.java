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
	/**
	 * The TileMapObject above this object, if any (not from collision layer).
	 */
	public final TileMapObject objectAbove;

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
		objectAbove = findObjectAbove();
	}

	/**
	 * A helper to find the object above this <code>TileMapObject</code> in the
	 * map.
	 */
	private TileMapObject findObjectAbove() {
		if (map == null) {
			return null;
		}

		// First look if there's a safe area above us
		List<TileMapObject> safeAreas = map.getSafeAreas();

		// Then look for trigger areas
		List<TileMapObject> triggerAreas = map.getTriggerAreas();

		// Then we look if it was the spawn area.
		TileMapObject spawnArea = map.getSpawnArea();
		
		// And finally the finish area, no more.
		TileMapObject finishArea = map.getFinishArea();
		
		// If none was found, it's null time.
		return null;
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
			String str = String.format(
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
		String str = "TileMapObject={"
				+ "index:" + index
				+ ",name:" + name
				+ ",type:" + type
				+ ",objectType:" + objectType
				+ ",x:" + x
				+ ",y:" + y
				+ ",width:" + width
				+ ",height:" + height
				+ ",props:" + props
				+ "}";
		return str;
	}
}
