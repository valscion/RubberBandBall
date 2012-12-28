package com.vesalaakso.rbb.game;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import com.vesalaakso.rbb.game.exceptions.MapException;

/**
 * The game.
 */
public class RubberBandBall extends BasicGame {

	/** The current tile map */
	private TileMap map;
	
	/** Constructs a new game. */
	public RubberBandBall() {
		super("Rubber band ball");
	}
	
	@Override
	public void init(GameContainer container) throws SlickException {
		try {
			map = new TileMap(3);
			map.init();
		}
		catch (MapException e) {
			Log.error(e.getMessage(), e);
			container.exit();
			return;
		}
	}

	@Override
	public void update(GameContainer container, int delta)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(GameContainer container, Graphics g)
			throws SlickException {
		// TODO Auto-generated method stub
		
	}

}
