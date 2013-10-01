package org.geneura.javiplay.bipeds.ea.linearcontroller;

import java.util.ArrayList;
import java.util.Random;

import org.geneura.javiplay.bipeds.ea.UtilParams;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleGene;
import es.ugr.osgiliath.evolutionary.elements.Mutation;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Genome;

public class LinearControllerMutation  extends OsgiliathService implements Mutation {

	@Override
	public Genome mutate(Genome genome) {
		
		double sd = (Double) getAlgorithmParameters().getParameter(UtilParams.WEIGHTS_CHANGE_SD);
		ArrayList<Gene> genes = ((ListGenome)genome).getGeneList();
		Random r = new Random();
		for (Gene g: genes) {
			DoubleGene dg = (DoubleGene) g;
			dg.setValue(dg.getValue()+r.nextGaussian()*sd);
		}
		return genome;
	}

}
