package org.geneura.javiplay.bipeds.simulators;


import es.ugr.osgiliath.evolutionary.individual.Individual;

public interface FitnessStepCalculator {

	boolean fitnessStep();
	public boolean fitnessProcessed();
	void fitnessStepReset();

}
