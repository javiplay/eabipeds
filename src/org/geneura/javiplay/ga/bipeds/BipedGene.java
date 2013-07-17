package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;

import es.ugr.osgiliath.evolutionary.individual.Gene;

public class BipedGene implements Gene, Cloneable {

	ArrayList<MotorActions> actions;
	int duration;

	public BipedGene(ArrayList<MotorActions> actions, int duration) {
		this.actions = actions;
		this.duration = duration;
	}

	public Object clone() {
		
		ArrayList<MotorActions> actions_clone = new ArrayList<MotorActions>(actions.size());
		
		for (int k=0; k < actions.size(); k++) {
			MotorActions action_clone = actions.get(k);
			actions_clone.add(action_clone);
		}				
		
		return new BipedGene(actions_clone, duration);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BipedGene [actions=" + actions + ", duration=" + duration + "]";
	}
	
		

}
