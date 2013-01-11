package com.vesalaakso.rbb.model;

import org.newdawn.slick.Color;
import org.newdawn.slick.tiled.GroupObject;

import com.vesalaakso.rbb.model.exceptions.MapException;

/**
 * A special <code>TileMapObject</code> to store gravity fields.
 * 
 * @author Vesa Laakso
 */
public class GravityArea extends TileMapObject {

	/** The x-gravity this area specifies */
	public final float xGravity;

	/** The y-gravity this area specifies */
	public final float yGravity;

	/** The background color for this area */
	public final Color color;

	/**
	 * Constructs a new gravity field.
	 * 
	 * @param obj
	 *            the GroupObject this object is based on
	 * @param map
	 *            the map this object belongs to
	 * @throws MapException
	 *             if the gravity area had unparseable gravity attributes
	 */
	public GravityArea(GroupObject obj, TileMap map) throws MapException {
		super(obj, map);

		String xGravityStr = obj.props.getProperty("gravity_x");
		String yGravityStr = obj.props.getProperty("gravity_y");

		if (xGravityStr == null || yGravityStr == null) {
			throw new MapException("Invalid properties for gravity area "
					+ obj.name + " in map " + map.getLevel());
		}

		try {
			xGravity = Float.parseFloat(xGravityStr);
			yGravity = Float.parseFloat(yGravityStr);
		}
		catch (NumberFormatException e) {
			throw new MapException("Invalid property values for gravity area "
					+ obj.name + " in map " + map.getLevel(), e);
		}

		color = initColor();
	}

	/** A helper to specify the background color */
	private Color initColor() {
		java.awt.Color tmpC;
		if (xGravity == 0) {
			if (yGravity < 0) {
				// Yellow
				tmpC = java.awt.Color.getHSBColor(0.2f, 0.5f, 0.5f);
			}
			else if (yGravity > 0) {
				// Pinkish
				tmpC = java.awt.Color.getHSBColor(0.8f, 0.5f, 0.5f);
			}
			else {
				// Gray
				tmpC = java.awt.Color.getHSBColor(0.0f, 0.0f, 0.5f);
			}
		}
		else {
			if (xGravity < 0) {
				// Reddish
				tmpC = java.awt.Color.getHSBColor(0.0f, 0.5f, 0.5f);
			}
			else {
				// Between blue and cyan
				tmpC = java.awt.Color.getHSBColor(0.6f, 0.0f, 0.5f);
			}
		}
		return new Color(tmpC.getRed(), tmpC.getGreen(), tmpC.getBlue());
	}
}
