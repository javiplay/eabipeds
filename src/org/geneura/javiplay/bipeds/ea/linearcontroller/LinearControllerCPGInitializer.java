package org.geneura.javiplay.bipeds.ea.linearcontroller;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.simulators.Simulator;



import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleGene;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.evolutionary.individual.Initializer;

public class LinearControllerCPGInitializer extends OsgiliathService implements Initializer {
	
	FitnessCalculator fc;

	@Override
	public ArrayList<Individual> initializeIndividuals(int size) {
		ArrayList<Individual> list = new ArrayList<Individual>(size);
		Simulator sim = ((BipedProblem) getProblem()).getSimulator();
		sim.simulatorReset();
		int genomeSize = (Integer) getAlgorithmParameters().getParameter(UtilParams.GENOME_SIZE);
		
		for (int i=0; i < size; i++) {
			 
			BasicIndividual individual = new BasicIndividual();
			ListGenome genome = new ListGenome();
			ArrayList<Gene> genes = new ArrayList<Gene>(genomeSize);
			for (int j=0; j < genomeSize ; j++ ) {
				
				java.util.Random rand = new java.util.Random();
				// GENE CONFIG
				double weight = minWeightValue + rand.nextFloat()*(maxWeightValue-minWeightValue);
				DoubleGene gene = new DoubleGene(weight);				
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
