package org.geneura.javiplay.bipeds.ea.controller;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.MotorActions;

import es.ugr.osgiliath.algorithms.Algorithm;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

/**
 * The linear controller gene consist of all the weights needed for the linear controller
 */
public class ControllerGene implements Gene, Cloneable {

	private static final long serialVersionUID = 6170200659507270341L;
	
	
	
	private ArrayList<Double> weights;
	
	/**
	 * @return the weights
	 */
	public ArrayList<Double> getWeights() {
		return weights;
	}

	/**
	 * @param weights the weights to set
	 */
	public void setWeights(ArrayList<Double> weights) {
		this.weights = weights;
	}

	public ControllerGene(ArrayList<Double> weights) {
		setWeights(weights);
	}

	public Object clone() {		
		ArrayList<Double> clonedWeights = new ArrayList<Double>(weights.size());		
		for (int k=0; k < weights.size(); k++) {
			double clonedWeight = weights.get(k);
			clonedWeights.add(clonedWeight);
		}						
		return new ControllerGene(clonedWeights);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Weights: ["+ getWeights()+"]" ;
	}
	
	
	
	

		

}
