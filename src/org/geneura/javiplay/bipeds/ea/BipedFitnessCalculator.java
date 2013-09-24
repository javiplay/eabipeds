package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.simulators.FastBipedSimulator;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedFitnessCalculator extends OsgiliathService implements FitnessCalculator {

	FastBipedSimulator simulator;
	public int indNumber;
	int cycles;
	
	public BipedFitnessCalculator(FastBipedSimulator simulator, AlgorithmParameters params) {
		this.simulator = simulator;
		setAlgorithmParameters(params);
		
		this.cycles = (Integer) getAlgorithmParameters().getParameter(BipedParameters.GENOME_CYCLES);
	}
	
	
	
	@Override
	public Fitness calculateFitness(Individual ind) {
		
		double score = simulator.reset(ind, cycles, indNumber);
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
