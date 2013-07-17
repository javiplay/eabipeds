package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
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

		int duration_change = (Integer) this.getAlgorithmParameters()
				.getParameter(BipedParameters.GENE_DURATION_CHANGE);
		int min_duration = (Integer) this.getAlgorithmParameters()
				.getParameter(BipedParameters.MIN_ACTION_DURATION);

		for (int i = 0; i < mutations; i++) {

			int index = rand.nextInt(genome_size);
			BipedGene gene = (BipedGene) gl.get(index);

			// mutate with same probability either action or duration
			if (rand.nextFloat() > 0.5) {
				// randomize all motor actions
				for (int j = 0; j < gene.actions.size(); j++) {
					gene.actions.set(j, MotorActions.values()[rand
							.nextInt(MotorActions.values().length)]);
				}
			} else {

				// change the time
				int sign = (rand.nextInt(2) * 2 - 1);
				int change = sign * rand.nextInt(duration_change);

				gene.duration += change;

				if (gene.duration < min_duration)
					gene.duration = min_duration;
			}
		}

		return genome;
	}

}
