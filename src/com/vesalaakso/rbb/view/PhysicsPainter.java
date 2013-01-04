package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Physics;

/**
 * This class handles the drawing of the physics world. Useful when debugging
 * the physics engine.
 * 
 * @author Vesa Laakso
 */
public class PhysicsPainter implements Painter {

	/** The associated <code>Physics</code>-model. */
	private Physics physics;

	/**
	 * Constructs the painter and associates it with the given
	 * <code>Physics</code> model.
	 * 
	 * @param physics
	 *            the <code>Physics</code> model holding the information about
	 *            every body in the world.
	 */
	public PhysicsPainter(Physics physics) {
		this.physics = physics;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>true</code>, physics objects are stored in world
	 *         coordinates so it only makses sense that drawing happens to world
	 *         coordinates, too
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return true;
	}

	@Override
	public void paint(Graphics g, Camera cam) {
		// Get all the physics bodies in the physics world.
		List<Body<?>> bodies = physics.getBodies();

		// Loop through them and draw them.
		for (Body<?> b : bodies) {
			g.setColor(Color.white);

			float bx = b.getX();
			float by = b.getY();

			/*
			 * TODO: Some cool effect showing that the body was RECENTLY hit.
			if (b.isTouching(ball)) {
				g.setColor(Color.red);
			}
			*/

			Shape shape = b.getShape();

			if (shape instanceof Rectangle) {
				Rectangle rect = (Rectangle) shape;
				g.drawRect(bx, by, rect.getWidth(), rect.getHeight());
			}
			else if (shape instanceof Circle) {
				Circle c = (Circle) shape;
				float r = c.getRadius();
				g.drawOval(bx - r, by - r, r * 2, r * 2);
			}
			else if (shape instanceof Polygon) {
				Polygon p = (Polygon) shape;
				int count = p.getPointCount();
				for (int i = 0; i < count; i++) {
					float x1 = p.getPointX(i);
					float y1 = p.getPointY(i);
					float x2, y2;
					if (i < count - 1) {
						x2 = p.getPointX(i + 1);
						y2 = p.getPointY(i + 1);
					}
					else {
						x2 = p.getPointX(0);
						y2 = p.getPointY(0);
					}
					g.drawLine(bx + x1, by + y1, bx + x2, by + y2);
				}
			}
		}
	}

}
