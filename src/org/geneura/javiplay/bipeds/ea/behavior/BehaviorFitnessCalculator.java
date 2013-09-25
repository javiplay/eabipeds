package org.geneura.javiplay.bipeds.ea.behavior;

import java.util.ArrayList;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import org.geneura.javiplay.bipeds.ea.BipedProblem;

public class BehaviorFitnessCalculator extends OsgiliathService implements FitnessCalculator {

	public int indNumber;
	int cycles;
	
	public BehaviorFitnessCalculator() {
		this.cycles = (Integer) getAlgorithmParameters().getParameter(BehaviorParameters.GENOME_CYCLES);
	}
	
	
	
	@Override
	public Fitness calculateFitness(Individual ind) {		
		double score = ((BipedProblem) getProblem()).getSimulator().getBehaviorFitness(ind, cycles, indNumber);
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
