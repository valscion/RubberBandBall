package com.vesalaakso.rbb.view;

import java.util.List;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.particles.ParticleSystem;

import com.vesalaakso.rbb.model.EffectManager;
import com.vesalaakso.rbb.model.ResourceManager;

/**
 * A <code>Painter</code> responsible for drawing all the particle systems.
 * 
 * @author Vesa Laakso
 */
public class ParticleSystemPainter implements Painter {

	/** The EffectManager associated with this Painter. */
	private EffectManager effectManager;

	/**
	 * Constructs a new <code>ParticleSystemPainter</code> and associates it
	 * with the given <code>EffectManager</code>.
	 * 
	 * @param effectManager
	 *            the <code>EffectManager</code> whose particles will be drawn.
	 */
	public ParticleSystemPainter(EffectManager effectManager) {
		this.effectManager = effectManager;
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

	/**
	 * @see com.vesalaakso.rbb.view.Painter#paint(Graphics, ResourceManager)
	 */
	@Override
	public void paint(Graphics g, ResourceManager resManager) {
		// Get all of the ParticleSystems there are.
		List<ParticleSystem> systems = effectManager.getParticleSystems();

		// Loop through them all and render them.
		for (ParticleSystem ps : systems) {
			ps.render();
		}
	}

}
