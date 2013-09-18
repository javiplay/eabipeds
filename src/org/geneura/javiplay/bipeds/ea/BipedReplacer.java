package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;

import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.elements.Population;
import es.ugr.osgiliath.evolutionary.elements.Replacer;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedReplacer implements Replacer {
	
	FitnessCalculator fc;

	@Override
	public void select(Population pop, ArrayList<Individual> parents,
			ArrayList<Individual> offspring,
			ArrayList<Individual> mutatedOffspring) {
		// TODO Auto-generated method stub
		
		for(Individual inda: offspring){
			System.out.println(inda.getFitness()+" VS "+this.fc.calculateFitness(inda));
		}
		
		for(Individual popi: pop.getAllIndividuals()){
			System.out.println(popi.getFitness()+" POP "+this.fc.calculateFitness(popi));
			if(popi.getFitness().compareTo(fc.calculateFitness(popi))!=0) System.out.println(">>>>>>>>>>>>>>>>>>>>");;
		}

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub

	}

	public void setFitnessCalculator(FitnessCalculator fitnessCalculator) {
		fc = fitnessCalculator;
		
	}

}
