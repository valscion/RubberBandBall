package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleSystem;

import com.vesalaakso.rbb.model.ParticleManager;

/**
 * A <code>Painter</code> responsible for drawing all the particle systems.
 * 
 * @author Vesa Laakso
 */
public class ParticleSystemPainter implements Painter {

	/** The ParticleManager associated with this Painter. */
	private ParticleManager particleManager;

	/**
	 * Constructs a new <code>ParticleSystemPainter</code> and associates it
	 * with the given <code>ParticleManager</code>.
	 * 
	 * @param particleManager
	 *            the <code>ParticleManager</code> to be drawn.
	 */
	public ParticleSystemPainter(ParticleManager particleManager) {
		this.particleManager = particleManager;
	}

	/**
	 * @see com.vesalaakso.rbb.view.Painter#isDrawnToWorldCoordinates()
	 * 
	 * @return <code>true</code>, as particles are indeed drawn to world
	 *         coordinates.
	 */
	@Override
	public boolean isDrawnToWorldCoordinates() {
		return true;
	}

	@Override
	public void paint(Graphics g) {
		// Get all of the ParticleSystems there are.
		List<ParticleSystem> systems = particleManager.getSystems();

		// Loop through them all and render them.
		for (ParticleSystem ps : systems) {
			ps.render();
		}
	}

}
