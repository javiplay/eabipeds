package org.geneura.javiplay.bipeds.ea.behavior;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.MotorActions;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.evolutionary.individual.Initializer;

public class BehaviorInitializer extends OsgiliathService implements Initializer {

	FitnessCalculator fc;
	
	@Override
	public ArrayList<Individual> initializeIndividuals(int size) {
		
		ArrayList<Individual> list = new ArrayList<Individual>(size);
		int genomeSize = (Integer) getAlgorithmParameters().getParameter(BehaviorParameters.GENOME_SIZE);
		int maxDuration = (Integer) getAlgorithmParameters().getParameter(BehaviorParameters.MAX_ACTION_DURATION);
		int minDuration = (Integer) getAlgorithmParameters().getParameter(BehaviorParameters.MIN_ACTION_DURATION);
		double maxSpeed = (Double) getAlgorithmParameters().getParameter(BehaviorParameters.MAX_SPEED);
		double minSpeed = (Double) getAlgorithmParameters().getParameter(BehaviorParameters.MIN_SPEED);
		boolean calculateFitness = (Boolean) getAlgorithmParameters().getParameter(BehaviorParameters.FITNESS_AT_INIT);
		
		int motors = (Integer) getAlgorithmParameters().getParameter(BehaviorParameters.MOTORS);
		
		for (int i=0; i < size; i++) {
			 
			BasicIndividual individual = new BasicIndividual();
			
			ListGenome genome = new ListGenome();
			ArrayList<Gene> genes = new ArrayList<Gene>(genomeSize);
			for (int j=0; j < genomeSize ; j++ ) {
				
				java.util.Random rand = new java.util.Random();
				// GENE CONFIG
				
				ArrayList<MotorActions> actions = new ArrayList<MotorActions>(motors);				
				for (int k=0; k < motors; k++) {
					int action = rand.nextInt(MotorActions.values().length);
					actions.add(MotorActions.values()[action]);
				}				
				
				int duration = minDuration;
				if ((maxDuration-minDuration)>0) {
					duration += rand.nextInt(maxDuration-minDuration);
				}
				
				float speed = (float) (minSpeed + rand.nextFloat()*(float)(maxSpeed-minSpeed));
				
				BehaviorGene gene = new BehaviorGene(actions, duration, speed);				
				genes.add(gene);
			}
			genome.setGenes(genes);
			
			individual.setGenome(genome);
			if (calculateFitness) {
				individual.setFitness(fc.calculateFitness(individual));
			}
			
			list.add(individual);
			
			
			
		}		
		return list;
	}

	public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
		fc = fitnessCalculator;		
	}
	
}
