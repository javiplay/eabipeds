package org.geneura.javiplay.bipeds.ea.linearcontroller;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.simulators.FitnessStepCalculator;
import org.geneura.javiplay.bipeds.simulators.Simulator;
import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class LinearControllerCPGFitnessCalculator extends OsgiliathService
		implements FitnessCalculator, FitnessStepCalculator {

	Individual individual;
	private double fitness;

	
	Simulator sim;
	int simulationSteps;
	double speed;
	private float timeStep;
	private float time;
	private double[] parameters;

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public boolean fitnessStep() {
		BipedDataAudit data = sim.getBipedDataAudit();
		data.save();
		time+=timeStep;
		
		data.runSinusoidalCPG(parameters, time);
		
		// check the time has expired
		if (sim.getStepCount() >= simulationSteps) {
			Vec2 cog = data.getCenterOfGravity();
			setFitness(cog.x);
			return true;
		}

		return false;
	}
	

	public double normalizeAngle(double a) {
		return a - (2 * Math.PI) * Math.floor(a / (2 * Math.PI));
	}

	
	
	@Override
	public void fitnessStepReset() {
		fitness = 0;
		time = 0;
		
		// in ms
		int timeToTry = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.TIME_TO_TRY);
		
		simulationSteps = timeToTry
				/ (1000 / sim.getSettings().getSetting(TestbedSettings.Hz).value);

		// in seconds
		timeStep = 1/sim.getSettings().getSetting(TestbedSettings.Hz).value; 
		parameters = new double[] {1, 10, 0, 1, 1, 10, 0, 1};
		

	}
	
	@Override
	public Fitness calculateFitness(Individual ind) {
		this.individual = ind;
		sim = ((BipedProblem) getProblem()).getSimulator();
		sim.simulatorReset();
		fitnessStepReset();

		while (!fitnessProcessed());

		return new DoubleFitness(getFitness(), true);
	}

	public boolean fitnessProcessed() {
		return sim.fitnessProcessed();
	}

	@Override
	public ArrayList<Fitness> calculateFitnessForAll(ArrayList<Individual> inds) {
		ArrayList<Fitness> fitList = new ArrayList<Fitness>(inds.size());
		for (Individual ind : inds) {
			fitList.add(calculateFitness(ind));
		}
		return fitList;
	}

}
