package org.geneura.javiplay.bipeds.simulators;


import es.ugr.osgiliath.evolutionary.individual.Individual;

public interface FitnessStepCalculator {
	void setFitnessStepCalculator(FitnessStepCalculator fitnessStepCalculator);
	FitnessStepCalculator getFitnessStepCalculator();

	boolean fitnessStep();

}
