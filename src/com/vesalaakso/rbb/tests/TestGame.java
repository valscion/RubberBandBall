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
	private int topOffsetInTiles;
	private int leftOffsetInTiles;
	
	private World world;

	public TestGame() {
		super("Test game");
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		int camTileX = (int) cameraX / TILE_SIZE;
		int camTileY = (int) cameraY / TILE_SIZE;

		int camOffsetX = (int) ((camTileX - (cameraX / TILE_SIZE)) * TILE_SIZE);
		int camOffsetY = (int) ((camTileY - (cameraY / TILE_SIZE)) * TILE_SIZE);

		g.setColor(Color.gray);
		g.fillRect(0, 0, container.getWidth(), container.getHeight());
		g.setColor(Color.white);

		map.render(camOffsetX, camOffsetY,
				camTileX - leftOffsetInTiles - 1,
				camTileY - topOffsetInTiles - 1,
				widthInTiles + 3, heightInTiles + 3,
				backLayer, false);

		g.drawString(String.format("Camera coords: (%.2f; %.2f)",
				cameraX, cameraY), 20, 20);
		
		float x = ball.getX();
		float y = ball.getY();
		
		g.drawString(String.format("Ball coords: (%.2f, %.2f)", x, y), 20, 60);
		
		g.translate(-cameraX, -cameraY);
		g.fillOval(x, y, 32, 32);
		g.resetTransform();
		
		map.render(camOffsetX, camOffsetY,
				camTileX - leftOffsetInTiles - 1,
				camTileY - topOffsetInTiles - 1,
				widthInTiles + 3, heightInTiles + 3,
				overLayer, false);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		world = new World();
		
		// Load tilemap
		map = new TiledMap("data/testmap.tmx");

		backLayer = map.getLayerIndex("back");
		overLayer = map.getLayerIndex("over");
		metaLayer = map.getLayerIndex("meta");

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

		// caculate some layout values for rendering the tilemap. How many tiles
		// do we need to render to fill the screen in each dimension and how far
		// is it from the centre of the screen
		widthInTiles = container.getWidth() / TILE_SIZE;
		heightInTiles = container.getHeight() / TILE_SIZE;
		leftOffsetInTiles = widthInTiles / 2;
		topOffsetInTiles = heightInTiles / 2;

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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void separated(CollisionEvent event) {
		// TODO Auto-generated method stub
		
	}
}
