package com.vesalaakso.rbb.tests;

import org.newdawn.fizzy.Circle;
import org.newdawn.fizzy.CollisionEvent;
import org.newdawn.fizzy.DynamicBody;
import org.newdawn.fizzy.Rectangle;
import org.newdawn.fizzy.StaticBody;
import org.newdawn.fizzy.World;
import org.newdawn.fizzy.WorldListener;
import org.newdawn.slick.*;
import org.newdawn.slick.tiled.TiledMap;

public class TestGame extends BasicGame implements WorldListener {

	private static final int TILE_SIZE = 32;
	private static float CAMERA_SPEED = 0.3f;
	private float cameraX, cameraY;
	
	private DynamicBody<Circle> ball;

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
		float r = ((Circle) ball.getShape()).getRadius();
		
		g.drawString(String.format("Ball coords: (%.2f, %.2f)", x, y), 20, 60);
		
		g.translate(-cameraX, -cameraY);
		g.fillOval(x - r, y - r, 32, 32);
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
		
		//world = new World(9.81f);
		world = new World(0.0f);

		hitData = new boolean[map.getWidth()][map.getHeight()];
		for (int x = 0; x < map.getWidth(); x++) {
			for (int y = 0; y < map.getHeight(); y++) {
				int tileID = map.getTileId(x, y, metaLayer);
				String value =
						map.getTileProperty(tileID, "collidable", "false");
				if ("true".equals(value)) {
					hitData[x][y] = true;
					
					Rectangle rect = new Rectangle(TILE_SIZE, TILE_SIZE);
					world.add(new StaticBody<Rectangle>(rect,
							x * TILE_SIZE, y * TILE_SIZE));
				}
			}
		}
		
		Circle ballCircle = new Circle(16f);
		ball = new DynamicBody<Circle>(ballCircle, 100f, 100f);
		world.add(ball);
		world.addListener(this);

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
		
		if (container.getInput().isKeyDown(Input.KEY_W)) {
			ball.applyForce(0, -1000f);
		}
		if (container.getInput().isKeyDown(Input.KEY_S)) {
			ball.applyForce(0, 1000f);
		}
		if (container.getInput().isKeyDown(Input.KEY_A)) {
			ball.applyForce(-500f, 0);
		}
		if (container.getInput().isKeyDown(Input.KEY_D)) {
			ball.applyForce(500f, 0);
		}
		
		float targetX = ball.getX() - container.getWidth() / 2;
		float targetY = ball.getY() - container.getHeight() / 2;
		
		// oldV + (newV - oldV) / smoothness);
		cameraX = cameraX + (targetX - cameraX) / 5.5f;
		cameraY = cameraY + (targetY - cameraY) / 5.5f;
		
		world.update(1f/20);
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new TestGame());

		app.setDisplayMode(800, 600, false);
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
