package com.vesalaakso.rbb.tests;

import org.newdawn.fizzy.Body;
import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

public class TestGame extends BasicGame implements WorldListener {

	private static final int TILE_SIZE = 32;
	private static float CAMERA_SPEED = 0.3f;
	private float cameraX, cameraY;
	
	private Body ball;

	private TiledMap map;
	private int backLayer;
	private int overLayer;
	private int metaLayer;
	private boolean[][] hitData;
	private int widthInTiles;
	private int heightInTiles;
	
	private World world;

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

		g.setColor(Color.gray);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(Color.white);

		
		map.render(camOffsetX, camOffsetY,
				camTileX,
				camTileY,
				widthInTiles + 3, heightInTiles + 3,
				backLayer, false);
		
		//map.render(0, 0, backLayer);
		//map.render((int) (cameraX - container.getWidth() / 2), (int) (cameraY - container.getHeight() / 2), camTileX, camTileY, 20, 20, backLayer, false);

		g.drawString(String.format("Camera coords: (%.2f; %.2f)",
				cameraX, cameraY), 20, 20);
		
		float x = ball.getX();
		float y = ball.getY();
		
		g.drawString(String.format("Ball coords: (%.2f, %.2f)", x, y), 20, 60);
		
		g.translate(-cameraX, -cameraY);
		g.fillOval(x, y, 32, 32);
		g.resetTransform();
		
		map.render(camOffsetX, camOffsetY,
				camTileX,
				camTileY,
				widthInTiles + 3, heightInTiles + 3,
				overLayer, false);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		// Load tilemap
		map = new TiledMap("data/testmap.tmx");

		backLayer = map.getLayerIndex("back");
		overLayer = map.getLayerIndex("over");
		metaLayer = map.getLayerIndex("meta");
		
		world = new World(map.getWidth() * TILE_SIZE, map.getHeight() * TILE_SIZE);

		hitData = new boolean[map.getWidth()][map.getHeight()];
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int tileID = map.getTileId(x, y, metaLayer);
				String value =
						map.getTileProperty(tileID, "collidable", "false");
				if ("true".equals(value)) {
					hitData[x][y] = true;
					
					Body rect = new Body(new Rectangle(32f, 32f),
							x * TILE_SIZE, y * TILE_SIZE, true);
					world.add(rect);
				}
			}
		}
		
		ball = new Body(new Circle(16f), 100, 100);
		world.add(ball);
		world.addListener(this);
		ball.setVelocity(200f, -500f);

		// caculate some layout values for rendering the tilemap. How many tiles
		// do we need to render to fill the screen in each dimension and how far
		// is it from the centre of the screen
		widthInTiles = container.getWidth() / TILE_SIZE;
		heightInTiles = container.getHeight() / TILE_SIZE;

		//cameraX = leftOffsetInTiles;
		//cameraY = topOffsetInTiles;

		container.setShowFPS(true);
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
		
		world.update(1f/20);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new TestGame());

		app.setDisplayMode(800, 600, false);
		app.start();
	}

	@Override
	public void collided(CollisionEvent event) {
		System.out.println("Collision");
	}

	@Override
	public void separated(CollisionEvent event) {
		System.out.println("Separation");
	}
}
