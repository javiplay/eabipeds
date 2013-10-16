package org.geneura.javiplay.bipeds.ea.behavior;

import java.util.ArrayList;
import java.util.Random;

import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.morphology.MotorActions;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.elements.Mutation;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Genome;

public class BehaviorMutation extends OsgiliathService implements Mutation {

	@Override
	public Genome mutate(Genome genome) {

		ListGenome g = (ListGenome) genome;
		ArrayList<Gene> gl = g.getGeneList();
		Random rand = new Random();

		// Swap some genes
		int swaps = (Integer) this.getAlgorithmParameters().getParameter(
				UtilParams.GENES_SWAPS);
		
		int genome_size = (Integer) this.getAlgorithmParameters().getParameter(
				UtilParams.GENOME_SIZE);

		for (int i = 0; i < swaps; i++) {
			int indexA = rand.nextInt(genome_size);
			int indexB = rand.nextInt(genome_size);

			Gene geneA = gl.get(indexA);
			Gene geneB = gl.get(indexB);

			gl.set(indexA, geneB);
			gl.set(indexB, geneA);

		}

		
		int maxDurationChange = (Integer) this.getAlgorithmParameters()
				.getParameter(UtilParams.MAX_DURATION_CHANGE);
		
		
		int minDuration = (Integer) this.getAlgorithmParameters()
				.getParameter(UtilParams.MIN_ACTION_DURATION);
		
		
		for (int i = 0; i < genome_size; i++) {

			int index = rand.nextInt(genome_size);
			BehaviorGene gene = (BehaviorGene) gl.get(index);

			// mutate action, and duration
			
			// randomize all motor actions
			for (int j = 0; j < gene.getActions().size(); j++) {
					gene.getActions().set(j, MotorActions.values()[rand
							.nextInt(MotorActions.values().length)]);
			}
			
			
			double durationChange = (rand.nextGaussian()*maxDurationChange); 
			
			
			gene.setDuration((int) (gene.getDuration() + durationChange));
			if (gene.getDuration() < minDuration)
					gene.setDuration(minDuration);
			
			// change in speed
			/*
			int speedChangeSign = (rand.nextInt(2) * 2 - 1);
			float speedChange = speedChangeSign * rand.nextFloat()*(float)maxSpeedChange;
			gene.setSpeed(gene.getSpeed() + speedChange);
			if (gene.getSpeed() < minSpeed)
					gene.setDuration(minDuration);			
			*/
		}

		return genome;
	}

}
