package org.geneura.javiplay.bipeds.ea.behavior;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;

import org.geneura.javiplay.bipeds.simulators.FitnessStepCalculator;
import org.geneura.javiplay.bipeds.simulators.Simulator;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BehaviorFitnessCalculator extends OsgiliathService implements FitnessCalculator, FitnessStepCalculator {

	private Individual individual;
	private int currentGene;
	private double fitness;
	public boolean showFitness;
	private int accDuration;
	private int next_action_step;
	Simulator sim;
	
	

	
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}
	


	
	
	
	public void processGene(BehaviorGene g) {
		
		

		for (int i = 0; i < sim.getJoints().size(); i++) {
			RevoluteJoint joint = sim.getJoints().get(i);
			float speed = g.getSpeed();

			switch (g.getActions().get(i)) {
			case FREE:
				joint.enableMotor(false);
				break;
			case LEFT:
				joint.setMotorSpeed(-speed);
				joint.enableMotor(true);
				break;
			case RIGHT:
				joint.setMotorSpeed(speed);
				joint.enableMotor(true);
				break;
			case STOP:
				joint.setMotorSpeed(0);
				joint.enableMotor(true);
				break;
			}

		}
	}
	
	
	@Override
	public Fitness calculateFitness(Individual ind) {
		
		this.individual = ind;
		Simulator sim = ((BipedProblem)getProblem()).getSimulator();
		sim.simulatorReset();
		fitnessStepReset();
		
		while (!fitnessProcessed());
		
		
		return new DoubleFitness(getFitness(), true );
		
	}



	@Override
	public ArrayList<Fitness> calculateFitnessForAll(ArrayList<Individual> inds) {
		ArrayList<Fitness> fitList = new ArrayList<Fitness>(inds.size()); 
		for (Individual ind: inds) {
			fitList.add(calculateFitness(ind));
		}
		return fitList;
	}

	@Override
	public boolean fitnessStep() {
				
		ArrayList<Gene> genes= ((ListGenome) individual.getGenome()).getGeneList();
		int genesListSize = genes.size();

		// check if the biped keeps standing up
		if (sim.getBipedDataAudit().getPositions().get(0).y < 0.5) {
			setFitness(0);
			return true;
		}

				// check next gene with accumulated duration (in steps) of the actions
		if (sim.getStepCount() >= next_action_step) {
			if (sim.getStepCount() != 1)
				currentGene++;

			if (currentGene == genesListSize) {
				// the fitness
				setFitness(sim.getBipedDataAudit().getMaxDistanceFromReference().get(0).x);
				if (showFitness) System.out.println("Fitness live:" + getFitness());
				
				currentGene = 0;
				return true;
			}

			// accumulate the action duration
			accDuration += ((BehaviorGene) genes.get(currentGene)).getDuration();
			next_action_step = accDuration
					/ (1000 / sim.getSettings().getSetting(TestbedSettings.Hz).value);

			// execute the action
			processGene((BehaviorGene) genes.get(currentGene));
		}
		
		return false;
	}




	@Override
	public void fitnessStepReset() {
		currentGene = 0;
		fitness = 0;		
		accDuration = 0;
		next_action_step = 0;
		sim = ((BipedProblem)getProblem()).getSimulator();
	}

	@Override
	public boolean fitnessProcessed() {				
		return ((BipedProblem)getProblem()).getSimulator().fitnessProcessed();
	}


	

	


}
