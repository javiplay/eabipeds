package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.MotorActions;

import es.ugr.osgiliath.algorithms.Algorithm;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedGene implements Gene, Cloneable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6170200659507270341L;
	
	
	
	private ArrayList<MotorActions> actions;
	private float speed;
	private int duration;

	public BipedGene(ArrayList<MotorActions> actions, int duration, float speed) {
		this.setActions(actions);
		this.setDuration(duration);
		this.setSpeed(speed);
	}

	public Object clone() {
		
		ArrayList<MotorActions> clonedActions = new ArrayList<MotorActions>(getActions().size());
		
		for (int k=0; k < getActions().size(); k++) {
			MotorActions clonedAction = getActions().get(k);
			clonedActions.add(clonedAction);
		}				
		
		return new BipedGene(clonedActions, getDuration(), getSpeed());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BipedGene [actions=" + getActions() + ", duration=" + getDuration() + "speed=" + getSpeed() + "]" ;
	}
	
	
	
	
	

	/**
	 * @return the speed
	 */
	public float getSpeed() {
		return speed;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	/**
	 * @return the actions
	 */
	public ArrayList<MotorActions> getActions() {
		return actions;
	}

	/**
	 * @param actions the actions to set
	 */
	public void setActions(ArrayList<MotorActions> actions) {
		this.actions = actions;
	}

	/**
	 * @return the duration
	 */
	public int getDuration() {
		return duration;
	}

	/**
	 * @param duration the duration to set
	 */
	public void setDuration(int duration) {
		this.duration = duration;
	}
	
		

}
