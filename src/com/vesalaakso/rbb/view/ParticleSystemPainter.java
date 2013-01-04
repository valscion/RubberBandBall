package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleSystem;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.ParticleSystemContainer;

/**
 * A <code>Painter</code> responsible for drawing all the particle systems.
 * 
 * @author Vesa Laakso
 */
public class ParticleSystemPainter implements Painter {

	/** The particle systems to paint are here. */
	private ParticleSystemContainer pSystemContainer;

	/**
	 * Constructs a new <code>ParticleSystemPainter</code> and associates it
	 * with the given <code>ParticleSystemContainer</code>.
	 * 
	 * @param psc
	 *            the ParticleSystemContainer which handles the saving and
	 *            destroying of particle systems.
	 */
	public ParticleSystemPainter(ParticleSystemContainer psc) {
		this.pSystemContainer = psc;
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
	public void paint(Graphics g, Camera cam) {
		// Get all of the ParticleSystems there are.
		List<ParticleSystem> systems = pSystemContainer.getSystems();
		
		// Loop through them all and render them.
		for (ParticleSystem ps : systems) {
			ps.render();
		}
	}

}
