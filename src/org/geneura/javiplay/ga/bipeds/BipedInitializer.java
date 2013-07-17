package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Genome;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.evolutionary.individual.Initializer;
import es.ugr.osgiliath.problem.Problem;
import es.ugr.osgiliath.utils.Random;

public class BipedInitializer extends OsgiliathService implements Initializer {

	FitnessCalculator fc;
	
	@Override
	public ArrayList<Individual> initializeIndividuals(int size) {
		
		ArrayList<Individual> list = new ArrayList<Individual>(size);
		int genome_size = (Integer) getAlgorithmParameters().getParameter(BipedParameters.GENOME_SIZE);
		int max_possible_duration = (Integer) getAlgorithmParameters().getParameter(BipedParameters.MAX_ACTION_DURATION);
		int motors = (Integer) getAlgorithmParameters().getParameter(BipedParameters.MOTORS);
		
		for (int i=0; i < size; i++) {
			 
			BasicIndividual individual = new BasicIndividual();
			
			ListGenome genome = new ListGenome();
			ArrayList<Gene> genes = new ArrayList<Gene>(genome_size);
			for (int j=0; j < genome_size ; j++ ) {
				
				java.util.Random rand = new java.util.Random();
				// GENE CONFIG
				
				ArrayList<MotorActions> actions = new ArrayList<MotorActions>(motors);				
				for (int k=0; k < motors; k++) {
					int action = rand.nextInt(MotorActions.values().length);
					actions.add(MotorActions.values()[action]);
				}				
				
				int duration = rand.nextInt(max_possible_duration);
				
				BipedGene gene = new BipedGene(actions, duration);				
				genes.add(gene);
			}
			genome.setGenes(genes);
			
			individual.setGenome(genome);
			individual.setFitness(fc.calculateFitness(individual));
			
			list.add(individual);
			
			
			
		}		
		return list;
	}

	public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
		fc = fitnessCalculator;		
	}
	
}
