package com.vesalaakso.rbb.controller;

import org.newdawn.fizzy.Vector;
import org.newdawn.slick.Input;

import com.vesalaakso.rbb.RubberBandBall;
import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.Player;
import com.vesalaakso.rbb.model.TileMap;
import com.vesalaakso.rbb.model.TileMapContainer;
import com.vesalaakso.rbb.util.Utils;

/**
 * This class handles the controlling of {@link Camera}.
 */
public class CameraController extends MouseAdapter implements Updateable,
		Resetable {

	/** If camera enters this border area from the screen, it will move. */
	private static final int BORDER_MAX_DIST = 50;

	/** This is the closest to the border mouse can get to gain full speed */
	private static final int BORDER_MIN_DIST = 10;

	/**
	 * The speed how fast the camera will be moved when mouse enters the edges
	 * of screen.
	 */
	private static final float CAMERA_MOVE_SPEED = 15.0f;

	/** The speed of which the camera will be moved in x-axis. */
	private float cameraMoveX = 0.0f;

	/** The speed of which the camera will be moved in y-axis. */
	private float cameraMoveY = 0.0f;

	/** The scale to try to move the camera to */
	private float cameraTargetScale = 1.0f;

	/** The Player the camera will follow. */
	private Player player;

	/** The map container to query the current map from for limiting */
	private TileMapContainer mapContainer;

	/**
	 * Constructs a new CameraController and associates it with the given
	 * <code>Player</code> and <code>TileMapContainer</code>.
	 * 
	 * @param player
	 *            the <code>Player</code> this controller will make the Camera
	 *            follow.
	 * @param mapContainer
	 *            the map container to query the current map from for limiting
	 */
	public CameraController(Player player, TileMapContainer mapContainer) {
		this.player = player;
		this.mapContainer = mapContainer;
	}

	@Override
	public void mouseMoved(int oldx, int oldy, int newx, int newy) {
		int scrW = RubberBandBall.SCREEN_WIDTH;
		int scrH = RubberBandBall.SCREEN_HEIGHT;

		int xDist = 0, yDist = 0;

		if (newx - BORDER_MAX_DIST < 0) {
			xDist = newx;
		}
		else if (newx + BORDER_MAX_DIST > scrW) {
			xDist = newx - scrW;
		}
		if (newy - BORDER_MAX_DIST < 0) {
			yDist = newy;
		}
		else if (newy + BORDER_MAX_DIST > scrH) {
			yDist = newy - scrH;
		}
		moveCamera(xDist, yDist);
	}

	@Override
	public void mouseWheelMoved(int change) {
		if (change < 0) {
			cameraTargetScale *= .95f;
			if (cameraTargetScale < Camera.MIN_SCALING) {
				cameraTargetScale = Camera.MIN_SCALING;
			}
		}
		else {
			cameraTargetScale *= 1.05f;
		}

		// When target scale is near 1, set it to 1 to ensure nice graphics.
		if (Math.abs(cameraTargetScale - 1.0f) < 0.02f) {
			cameraTargetScale = 1.0f;
		}

		// Don't allow the target scale to go break camera limits
		if (cameraTargetScale > Camera.MAX_SCALING) {
			cameraTargetScale = Camera.MAX_SCALING;
		}
		else if (cameraTargetScale < Camera.MIN_SCALING) {
			cameraTargetScale = Camera.MIN_SCALING;
		}
	}
	
	@Override
	public void mousePressed(int button, int x, int y) {
		// When the wheel was pressed, reset target scale to 1.
		if (button == Input.MOUSE_MIDDLE_BUTTON) {
			cameraTargetScale = 1.0f;
		}
	}

	/**
	 * Handles moving camera based on the given values
	 * 
	 * @param xDist
	 *            mouse x-distance from screen edge
	 * @param yDist
	 *            mouse y-distance from screen edge
	 */
	private void moveCamera(int xDist, int yDist) {
		if (xDist < 0) {
			xDist = Utils.clamp(-xDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveX = (1.0f / xDist) * CAMERA_MOVE_SPEED;
		}
		else if (xDist > 0) {
			xDist = Utils.clamp(xDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveX = -(1.0f / xDist) * CAMERA_MOVE_SPEED;
		}
		else {
			cameraMoveX = 0;
		}
		if (yDist < 0) {
			yDist = Utils.clamp(-yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = (1.0f / yDist) * CAMERA_MOVE_SPEED;
		}
		else if (yDist > 0) {
			yDist = Utils.clamp(yDist, BORDER_MIN_DIST, BORDER_MAX_DIST);
			cameraMoveY = -(1.0f / yDist) * CAMERA_MOVE_SPEED;
		}
		else {
			cameraMoveY = 0;
		}
	}

	/** Updates the camera position and scaling as wanted. */
	private void updateCamera(int delta) {
		Camera cam = Camera.get();

		// If scaling target differs enough from the current scale, scale.
		float camScale = cam.getScaling();
		if (Math.abs(camScale - cameraTargetScale) > 1e-5f) {
			float newScale = Utils.curveValue(camScale, cameraTargetScale, 20);
			cam.setScaling(newScale);
		}

		// If the player is in a state that he is ready to be launched or hasn't
		// been positioned for start yet, move the camera by the speed specified
		// with mouse position
		if (player.isReadyForLaunch() || !player.isStartPositioned()) {
			cam.translate(cameraMoveX * delta, cameraMoveY * delta);
		}
		else {
			// Glue the camera to the player if one is not ready for launch but
			// has a start position set.
			Vector oldP = new Vector(cam.getX(), cam.getY());
			Vector targetP = new Vector(player.getX(), player.getY());
			Vector curved = Utils.curvePoints(oldP, targetP, 20);

			cam.setPosition(curved.x, curved.y);
		}
	}

	/** Limits the camera position and scaling */
	private void limitCamera() {
		Camera cam = Camera.get();
		TileMap map = mapContainer.getMap();

		// Limit camera position based on map height.
		int mapWidth = map.getWidthInTiles() * TileMap.TILE_SIZE;
		int mapHeight = map.getHeightInTiles() * TileMap.TILE_SIZE;

		// Position to set the camera to
		float camX = cam.getX();
		float camY = cam.getY();

		// Current viewport width and height halves in pixels
		float halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
		float halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());

		// Limit scaling
		if (halfScrW * 2 > mapWidth) {
			float newScale = ((float) RubberBandBall.SCREEN_WIDTH / mapWidth);
			cam.setScaling(newScale);
			cameraTargetScale = newScale;
			halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
			halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());
		}
		if (halfScrH * 2 > mapHeight) {
			float newScale = ((float) RubberBandBall.SCREEN_HEIGHT / mapHeight);
			cam.setScaling(newScale);
			cameraTargetScale = newScale;
			halfScrW = RubberBandBall.SCREEN_WIDTH / (2 * cam.getScaling());
			halfScrH = RubberBandBall.SCREEN_HEIGHT / (2 * cam.getScaling());
		}

		if (camX < halfScrW) {
			// Over the left
			camX = halfScrW;
		}
		else if (camX > mapWidth - halfScrW) {
			// Over the right
			camX = mapWidth - halfScrW;
		}
		if (camY < halfScrH) {
			// Over the top
			camY = halfScrH;
		}
		else if (camY > mapHeight - halfScrH) {
			// Over the bottom
			camY = mapHeight - halfScrH;
		}

		cam.setPosition(camX, camY);
	}

	/**
	 * @see Updateable#update(int)
	 */
	@Override
	public void update(int delta) {
		updateCamera(delta);
		limitCamera();
	}

	@Override
	public void reset() {
		cameraMoveX = 0;
		cameraMoveY = 0;
		cameraTargetScale = 1;
		limitCamera();
	}

}
