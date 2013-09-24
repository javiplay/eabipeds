package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;
import java.util.Random;

import org.geneura.javiplay.bipeds.morphology.MotorActions;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.elements.Mutation;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Genome;

public class BipedMutation extends OsgiliathService implements Mutation {

	@Override
	public Genome mutate(Genome genome) {

		ListGenome g = (ListGenome) genome;
		ArrayList<Gene> gl = g.getGeneList();
		Random rand = new Random();

		// Swap some genes
		int swaps = (Integer) this.getAlgorithmParameters().getParameter(
				BipedParameters.GENES_SWAPS);
		int genome_size = (Integer) this.getAlgorithmParameters().getParameter(
				BipedParameters.GENOME_SIZE);

		for (int i = 0; i < swaps; i++) {
			int indexA = rand.nextInt(genome_size);
			int indexB = rand.nextInt(genome_size);

			Gene geneA = gl.get(indexA);
			Gene geneB = gl.get(indexB);

			gl.set(indexA, geneB);
			gl.set(indexB, geneA);

		}

		// Mutate some genes
		int mutations = (Integer) this.getAlgorithmParameters().getParameter(
				BipedParameters.GENES_TO_MUTATE);

		int maxDurationChange = (Integer) this.getAlgorithmParameters()
				.getParameter(BipedParameters.MAX_DURATION_CHANGE);
		double maxSpeedChange = (Double) this.getAlgorithmParameters().getParameter(BipedParameters.MAX_SPEED_CHANGE);

		
		int minDuration = (Integer) this.getAlgorithmParameters()
				.getParameter(BipedParameters.MIN_ACTION_DURATION);
		double minSpeed = (Double) this.getAlgorithmParameters().getParameter(BipedParameters.MIN_SPEED);
		
		
		for (int i = 0; i < mutations; i++) {

			int index = rand.nextInt(genome_size);
			BipedGene gene = (BipedGene) gl.get(index);

			// mutate action, speed and duration
			// randomize all motor actions
			for (int j = 0; j < gene.getActions().size(); j++) {
					gene.getActions().set(j, MotorActions.values()[rand
							.nextInt(MotorActions.values().length)]);
			}
			
			
			int durationChange = 0;
			// change in duration 
			int durationChangeSign = (rand.nextInt(2) * 2 - 1);
			if (maxDurationChange != 0) 
				durationChange = durationChangeSign * rand.nextInt(maxDurationChange); 
			
			
			gene.setDuration(gene.getDuration() + durationChange);
			if (gene.getDuration() < minDuration)
					gene.setDuration(minDuration);
			
			// change in speed
			int speedChangeSign = (rand.nextInt(2) * 2 - 1);
			float speedChange = speedChangeSign * rand.nextFloat()*(float)maxSpeedChange;
			gene.setSpeed(gene.getSpeed() + speedChange);
			if (gene.getSpeed() < minSpeed)
					gene.setDuration(minDuration);			
			
		}

		return genome;
	}

}
