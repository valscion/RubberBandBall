package com.vesalaakso.rbb.tests;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Polygon;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.Shape;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

public class TestGame extends BasicGame implements WorldListener {

	private static final int TILE_SIZE = 32;
	private static float CAMERA_SPEED = 0.3f;

	private static final float PLAYER_SIZE = 16f;

	private static final int SCREEN_W = 800;
	private static final int SCREEN_H = 600;

	private static final float MOVE_FORCE = 1500f;

	private float cameraX, cameraY;

	private DynamicBody<Circle> ball;

	private TiledMap map;
	private int backLayer;
	private int overLayer;
	private int metaLayer;
	private int widthInTiles;
	private int heightInTiles;

	private World world;

	private List<Body<?>> bodies = new LinkedList<Body<?>>();

	public TestGame() {
		super("Test game");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		int camTileX = (int) cameraX / TILE_SIZE;
		int camTileY = (int) cameraY / TILE_SIZE;

		int camOffsetX = (int) (camTileX * TILE_SIZE - cameraX);
		int camOffsetY = (int) (camTileY * TILE_SIZE - cameraY);

		g.setColor(Color.black);
		g.fillRect(0, 0, SCREEN_W, SCREEN_H);
		g.setColor(Color.white);


		map.render(camOffsetX, camOffsetY,
				camTileX,
				camTileY,
				widthInTiles + 3, heightInTiles + 3,
				backLayer, false);

		float x = ball.getX();
		float y = ball.getY();

		g.translate(-cameraX, -cameraY);

		g.fillOval(x - PLAYER_SIZE, y - PLAYER_SIZE, PLAYER_SIZE * 2, PLAYER_SIZE * 2);

		// Draw static collidable bodies
		for (Body<?> b : bodies) {
			float bx = b.getX();
			float by = b.getY();

			if (b.isTouching(ball)) {
				g.setColor(Color.red);
			}

			Shape shape = b.getShape();

			if (shape instanceof Rectangle) {
				Rectangle rect = (Rectangle) shape;
				g.drawRect(bx, by, rect.getWidth(), rect.getHeight());
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

			g.setColor(Color.white);
		}
		g.resetTransform();

		/*
		map.render(camOffsetX, camOffsetY,
				camTileX,
				camTileY,
				widthInTiles + 3, heightInTiles + 3,
				overLayer, false);
		 */

		drawDebugInfo(g, 50, 10, 15);
	}

	private void drawDebugInfo(Graphics g, float firstLineY, float x, float lineHeight) {
		List<String> strings = new LinkedList<String>();

		// Camera
		strings.add(String.format("Camera coords: (%.2f; %.2f)",
				cameraX, cameraY));

		// Ball
		strings.add(String.format("Ball (radius %.2f) located at (%.2f; %.2f)",
				((Circle) ball.getShape()).getRadius(), ball.getX(), ball.getY()));
		strings.add(String.format("    Velocity: (%.2f; %.2f)",
				ball.getXVelocity(), ball.getYVelocity()));

		int i = 0;
		for (String str : strings) {
			g.drawString(str, x, firstLineY + i * lineHeight);
			i++;
		}
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Load tilemap
		map = new TiledMap("data/testmap.tmx");

		backLayer = map.getLayerIndex("back");
		overLayer = map.getLayerIndex("over");
		metaLayer = map.getLayerIndex("meta");

		world = new World(9.81f);
		//world = new World(0.0f);

		// Different types of static triangle shapes for angled sides.
		Map<String, Polygon> triangles = new HashMap<String, Polygon>();
		triangles.put("top-left", createTrianglePolygon("top-left"));
		triangles.put("bottom-left", createTrianglePolygon("bottom-left"));
		triangles.put("bottom-right", createTrianglePolygon("bottom-right"));
		triangles.put("top-right", createTrianglePolygon("top-right"));


		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int tileID = map.getTileId(x, y, metaLayer);
				String value =
						map.getTileProperty(tileID, "collision", "false");
				if ("all".equals(value)) {
					Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
					StaticBody<Rectangle> body = new StaticBody<Rectangle>(
							rect, x * TILE_SIZE, y * TILE_SIZE);
					bodies.add(body);
					world.add(body);
				}

				else if (triangles.containsKey(value)) {
					Polygon triangle = triangles.get(value);
					StaticBody<Polygon> body = new StaticBody<Polygon>(triangle,
							x * TILE_SIZE, y * TILE_SIZE);
					bodies.add(body);
					world.add(body);
				}

			}
		}


		Circle ballCircle = new Circle(PLAYER_SIZE);
		ball = new DynamicBody<Circle>(ballCircle, TILE_SIZE * 4, TILE_SIZE * 4);
		world.add(ball);
		world.addListener(this);

		// caculate some layout values for rendering the tilemap. How many tiles
		// do we need to render to fill the screen in each dimension and how far
		// is it from the centre of the screen
		widthInTiles = SCREEN_W / TILE_SIZE;
		heightInTiles = SCREEN_H / TILE_SIZE;

		//cameraX = leftOffsetInTiles;
		//cameraY = topOffsetInTiles;

		container.setShowFPS(true);
	}

	private Polygon createTrianglePolygon(String angle) {
		Vec2[] points = null;
		if (angle.equals("top-left")) {
			points = new Vec2[] {
					new Vec2(0, 0), // Left, Top
					new Vec2(TILE_SIZE, 0), // Right, Top
					new Vec2(0, TILE_SIZE) // Left, Bottom
			};
		}
		else if (angle.equals("bottom-left")) {
			points = new Vec2[] {
					new Vec2(0, 0), // Left, Top
					new Vec2(TILE_SIZE, TILE_SIZE), // Right, Bottom
					new Vec2(0, TILE_SIZE) // Left, Bottom
			};
		}
		else if (angle.equals("bottom-right")) {
			points = new Vec2[] {
					new Vec2(0, TILE_SIZE), // Left, Bottom
					new Vec2(TILE_SIZE, 0), // Right, Top
					new Vec2(TILE_SIZE, TILE_SIZE) // Right, Bottom
			};
		}
		else if (angle.equals("top-right")) {
			points = new Vec2[] {
					new Vec2(0, 0), // Left, Top
					new Vec2(TILE_SIZE, 0), // Right, Top
					new Vec2(TILE_SIZE, TILE_SIZE) // Right, Bottom
			};
		}

		if (points == null) {
			return null;
		}
		else {
			Polygon triangle = new Polygon();
			triangle.setPoints(points);
			return triangle;
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		if (container.getInput().isKeyDown(Input.KEY_LEFT)) {
			cameraX -= delta * CAMERA_SPEED;
		}
		if (container.getInput().isKeyDown(Input.KEY_RIGHT)) {
			cameraX += delta * CAMERA_SPEED;
		}
		if (container.getInput().isKeyDown(Input.KEY_UP)) {
			cameraY -= delta * CAMERA_SPEED;
		}
		if (container.getInput().isKeyDown(Input.KEY_DOWN)) {
			cameraY += delta * CAMERA_SPEED;
		}
		if (container.getInput().isKeyDown(Input.KEY_ESCAPE)) {
			container.exit();
		}

		if (container.getInput().isKeyDown(Input.KEY_W)) {
			ball.applyForce(0, -MOVE_FORCE);
		}
		if (container.getInput().isKeyDown(Input.KEY_S)) {
			ball.applyForce(0, MOVE_FORCE);
		}
		if (container.getInput().isKeyDown(Input.KEY_A)) {
			ball.applyForce(-MOVE_FORCE, 0);
		}
		if (container.getInput().isKeyDown(Input.KEY_D)) {
			ball.applyForce(MOVE_FORCE, 0);
		}
		if (container.getInput().isKeyPressed(Input.KEY_SPACE)) {
			ball.applyForce(0, -MOVE_FORCE * 100);
		}

		float targetX = ball.getX() - SCREEN_W / 2;
		float targetY = ball.getY() - SCREEN_H / 2;

		// oldV + (newV - oldV) / smoothness);
		cameraX = cameraX + (targetX - cameraX) / 5.5f;
		cameraY = cameraY + (targetY - cameraY) / 5.5f;

		world.update(1f/20);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new TestGame());

		app.setDisplayMode(SCREEN_W, SCREEN_H, false);
		//app.setTargetFrameRate(60);
		app.setVSync(true);
		app.start();
	}

	@Override
	public void collided(CollisionEvent event) {
		// TODO: Something here?
	}

	@Override
	public void separated(CollisionEvent event) {
		// TODO: Something here?
	}
}
