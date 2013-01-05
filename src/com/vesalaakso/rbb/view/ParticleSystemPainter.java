package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleSystem;

import com.vesalaakso.rbb.model.Camera;
import com.vesalaakso.rbb.model.ParticleManager;

/**
 * A <code>Painter</code> responsible for drawing all the particle systems.
 * 
 * @author Vesa Laakso
 */
public class ParticleSystemPainter implements Painter {

	/**
	 * Constructs a new <code>ParticleSystemPainter</code>.
	 */
	public ParticleSystemPainter() {
		// We can use the singleton class to get the ParticleManager when
		// needed - thus we don't need to do any initialization here.
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
		// ParticleManager is a singleton class, get it's instance.
		ParticleManager pm = ParticleManager.get();

		// Get all of the ParticleSystems there are.
		List<ParticleSystem> systems = pm.getSystems();

		// Loop through them all and render them.
		for (ParticleSystem ps : systems) {
			ps.render();
		}
	}

}
