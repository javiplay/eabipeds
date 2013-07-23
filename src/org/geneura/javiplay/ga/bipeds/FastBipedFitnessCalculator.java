package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedFitnessCalculator extends OsgiliathService implements FitnessCalculator {

	FastBipedSimulator simulator;
	
	public FastBipedFitnessCalculator(FastBipedSimulator simulator) {
		this.simulator = simulator;	
	}
	
	
	
	@Override
	public Fitness calculateFitness(Individual ind) {
		
	
		double score = simulator.reset(ind);
		return new DoubleFitness( score , false);
	}


	@Override
	public ArrayList<Fitness> calculateFitnessForAll(ArrayList<Individual> inds) {
		ArrayList<Fitness> fitList = new ArrayList<Fitness>(inds.size()); 
		for (Individual ind: inds) {
			fitList.add(calculateFitness(ind));
		}
		return fitList;
	}



}
