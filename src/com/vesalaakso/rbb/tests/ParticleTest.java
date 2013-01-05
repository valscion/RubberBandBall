package com.vesalaakso.rbb.tests;

import java.io.IOException;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.particles.ParticleIO;
import org.newdawn.slick.particles.ParticleSystem;
import org.newdawn.slick.particles.effects.FireEmitter;

/**
 * A particle test, copied from slick repository and modified to suit the
 * project.
 */
public class ParticleTest extends BasicGame {
	/** The particle system running everything */
	private ParticleSystem system;
	/** The particle blending mode */
	private int mode = ParticleSystem.BLEND_COMBINE;

	/**
	 * Create a new test of graphics context rendering
	 */
	public ParticleTest() {
		super("Particle Test");
	}

	/**
	 * @see org.newdawn.slick.BasicGame#init(org.newdawn.slick.GameContainer)
	 */
	@Override
	public void init(GameContainer container) throws SlickException {
		String file = "data/slick_particle_effects/explosion.xml";
		try {
			ParticleSystem.setRelativePath("data/slick_particle_effects");
			system = ParticleIO.loadConfiguredSystem(file);
		}
		catch (IOException e) {
			e.printStackTrace();
			container.exit();
			return;
		}
		
		system.setPosition(250, 250);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#render(org.newdawn.slick.GameContainer,
	 *      org.newdawn.slick.Graphics)
	 */
	@Override
	public void render(GameContainer container, Graphics g) {
		system.render();
	}

	/**
	 * @see org.newdawn.slick.BasicGame#update(org.newdawn.slick.GameContainer,
	 *      int)
	 */
	@Override
	public void update(GameContainer container, int delta) {
		system.update(delta);
	}

	/**
	 * @see org.newdawn.slick.BasicGame#keyPressed(int, char)
	 */
	@Override
	public void keyPressed(int key, char c) {
		if (key == Input.KEY_ESCAPE) {
			System.exit(0);
		}
		if (key == Input.KEY_SPACE) {
			mode =
				ParticleSystem.BLEND_ADDITIVE == mode ? ParticleSystem.BLEND_COMBINE
						: ParticleSystem.BLEND_ADDITIVE;
			system.setBlendingMode(mode);
		}
	}

	/**
	 * Entry point to our test
	 * 
	 * @param argv
	 *            The arguments passed to the test
	 */
	public static void main(String[] argv) {
		try {
			AppGameContainer container =
				new AppGameContainer(new ParticleTest());
			container.setDisplayMode(800, 600, false);
			container.start();
		}
		catch (SlickException e) {
			e.printStackTrace();
		}
	}
}
