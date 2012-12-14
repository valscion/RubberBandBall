package com.vesalaakso.rbb.tests;
import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.*;

public class GridTest extends BasicGame {

	private Image cursorImg;
	private int[][] grid;
	private int gridWidth, gridHeight;
	
	private int mouseX, mouseY;
	private int cursorX, cursorY;
	private boolean mouseButtonsPressed[] = { false, false };
	
	private int change = -1;
	
	public GridTest() {
		super("Simple grid test");
	}
	
	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		
		Color colorDark = new Color(64, 64, 64);
		Color color1 = new Color(0, 64, 0);
		Color color2 = new Color(0, 0, 64);
		
		g.setColor(colorDark);
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				if (grid[y][x] > 0) {
					if (grid[y][x] == 1) {
						g.setColor(color1);
					}
					else if (grid[y][x] == 2) {
						g.setColor(color2);
					}
					g.fillRect(x * 32, y * 32, 32, 32);
				}
				
				g.setColor(colorDark);
				g.drawLine(x * 32, 0, x * 32, gridHeight * 32);
				g.drawLine(0, y * 32, gridWidth * 32, y * 32);
			}
		}
		
		g.drawLine(gridWidth * 32, 0, gridWidth * 32, gridHeight * 32);
		g.drawLine(0, gridHeight * 32, gridWidth * 32, gridHeight * 32);
		
		cursorImg.drawCentered(cursorX, cursorY);
		
		g.setColor(new Color(255, 255, 255));
		g.drawString(String.format("Image coord: (%d, %d)", cursorX, cursorY), 10, 60);
		g.drawString(String.format("Change: %d", change), 10, 80);
	}

	@Override
	public void init(GameContainer container) throws SlickException {
		cursorImg = new Image("data/cbmedia/cursor.png");
		
		gridWidth = container.getWidth() / 32;
		gridHeight = container.getHeight() / 32;
		grid = new int[gridHeight][gridWidth];
		
		for (int y = 0; y < gridHeight; y++) {
			for (int x = 0; x < gridWidth; x++) {
				grid[y][x] = 0;
			}
		}
		
		// Hide mouse
		try {
			Cursor emptyCursor = new Cursor(1, 1, 0, 0, 1, BufferUtils.createIntBuffer(1), null);
			Mouse.setNativeCursor(emptyCursor);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
		if (change != -1) {
			int gridX = mouseX / 32;
			int gridY = mouseY / 32;
			if (gridX >= gridWidth || gridX < 0 || gridY >= gridWidth || gridY < 0) {
				System.out.printf("Weird mouse coordinates: (%d, %d).%n", gridX, gridY);
				return;
			}
			
			grid[gridY][gridX] = change;
		}
		
	}

	public static void main(String[] args) throws SlickException {
		AppGameContainer app = new AppGameContainer(new GridTest());
		app.setDisplayMode(640, 480, false);
		app.start();
	}
	
	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		updateMouseCoordinates(newx, newy);
	}
	
	@Override
	public void mouseDragged(int oldx, int oldy, int newx, int newy) {
		updateMouseCoordinates(newx, newy);
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		if (button >= 0 && button < mouseButtonsPressed.length) {
			if (!mouseButtonsPressed[button]) {
				// Wasn't down before
				mouseButtonsPressed[button] = true;
				int gridX = mouseX / 32;
				int gridY = mouseY / 32;
				if (grid[gridY][gridX] > 0) {
					// Erase if not empty
					change = 0;
				}
				else {
					change = button + 1;
				}
			}
		}
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		if (button >= 0 && button < mouseButtonsPressed.length) {
			mouseButtonsPressed[button] = false;
			// Stop changing
			change = -1;
		}
	}
	
	private void updateMouseCoordinates(int x, int y) {
		mouseX = x;
		mouseY = y;
		
		cursorX = ((int) (mouseX / 32)) * 32 + 16;
		cursorY = ((int) (mouseY / 32)) * 32 + 16;
	}
}
