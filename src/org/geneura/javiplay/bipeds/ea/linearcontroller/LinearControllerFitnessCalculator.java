package org.geneura.javiplay.bipeds.ea.linearcontroller;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.MotorActions;
import org.geneura.javiplay.bipeds.simulators.FitnessStepCalculator;
import org.geneura.javiplay.bipeds.simulators.Simulator;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.matrix.Matrix;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleGene;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class LinearControllerFitnessCalculator extends OsgiliathService implements FitnessCalculator, FitnessStepCalculator{

	Individual individual;
	private double fitness;
	FeedforwardNetwork network;
	Simulator sim;
	int simulationSteps;
	private BipedDataAudit dataAudit; 
	double speed;
	
	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public boolean fitnessStep() {
		

		// check if the biped keeps standing up
		/*if (dataAudit.getPositions().get(0).y < 0.8) {
			setFitness(0);
			return true;
		}*/

		double outputs[] = network.computeOutputs(getInputs());
		processOutputs(outputs);

		// check the time has expired
		if (sim.getStepCount() >= simulationSteps) {
			// set the fitness
			setFitness(sim.getBipedDataAudit().getMaxDistanceFromReference().get(0).x);			
			return true;
		}
		
		return false;
	}

	private void processOutputs(double[] outputs) {
		
		    float s = (float )speed;
		
		
			RevoluteJoint joint = sim.getJoints().get(0);
			MotorActions action = MotorActions.FREE;
			
			if (outputs[0]<=0.5 && outputs[1]<=0.5) {
				action = MotorActions.FREE;
			}
			if (outputs[0]>0.5 && outputs[1]<=0.5) {
				action = MotorActions.LEFT;
			}
			if (outputs[0]<=0.5 && outputs[1]>0.5) {
				action = MotorActions.RIGHT;
			}
			if (outputs[0]>0.5 && outputs[1]>0.5) {
				action = MotorActions.STOP;
			}
			
			switch (action) {
			case FREE:
				joint.enableMotor(false);
				break;
			case LEFT:
				joint.setMotorSpeed(-s);
				joint.enableMotor(true);
				break;
			case RIGHT:
				joint.setMotorSpeed(s);
				joint.enableMotor(true);
				break;
			case STOP:
				joint.setMotorSpeed(0);
				joint.enableMotor(true);
				break;
			}
		
		
	}

	private double[] getInputs() {
		
		double inputs[] = new double[8];
		inputs[0] = Math.sin(sim.getJoints().get(0).getBodyA().getAngle());
		inputs[1] = Math.cos(sim.getJoints().get(0).getBodyA().getAngle());
		inputs[2] = Math.sin(sim.getJoints().get(0).getBodyA().getAngularVelocity());
		
		inputs[3] = Math.sin(sim.getJoints().get(0).getBodyB().getAngle());
		inputs[4] = Math.cos(sim.getJoints().get(0).getBodyB().getAngle());
		inputs[5] = Math.sin(sim.getJoints().get(0).getBodyB().getAngularVelocity());
		
		inputs[6] = dataAudit.getStanceA();
		inputs[7] = dataAudit.getStanceB();
		
		return inputs;
	}

	@Override
	public void fitnessStepReset() {
		
		sim = ((BipedProblem)getProblem()).getSimulator();
		dataAudit = sim.getBipedDataAudit();
		// in ms
		int timeToTry = (Integer) getAlgorithmParameters().getParameter(UtilParams.TIME_TO_TRY);
		int motors =  (Integer) getAlgorithmParameters().getParameter(UtilParams.MOTORS);
		simulationSteps = timeToTry / (1000 / sim.getSettings().getSetting(TestbedSettings.Hz).value);
		
		speed = (Double) getAlgorithmParameters().getParameter(UtilParams.MAX_SPEED);
		network = new FeedforwardNetwork();

		// INPUTS
		// 2 inputs from each angle (cos(a), sin(a)) 
		// 1 inputs from each angular velocity
		// 2 binary inputs from contact

		network.addLayer(new FeedforwardLayer(8));
		
		// OUTPUTS
		// 2 bits per motor
		network.addLayer(new FeedforwardLayer(2*motors));
		setWeights();
		
	}

	
	void setWeights() {
		ArrayList<Gene> genes= ((ListGenome) individual.getGenome()).getGeneList();		
		int genecount = 0;
		Matrix m = network.getInputLayer().getMatrix();	
	
		for (int i=0;i<m.getRows();i++) {
			for (int j=0;j<m.getCols();j++) {
				double value = ((DoubleGene) genes.get(genecount)).getValue();
				m.set(i, j, value);
				genecount++;
			}
		}		
		
	}
	
	@Override
	public Fitness calculateFitness(Individual ind) {
		this.individual = ind;
		sim = ((BipedProblem)getProblem()).getSimulator();
		sim.simulatorReset();
		fitnessStepReset();			
		
		while (!fitnessProcessed());
				
		return new DoubleFitness(getFitness(), true );
	}

	public boolean fitnessProcessed() {
		return sim.fitnessProcessed();
	}
	
	
	@Override
	public ArrayList<Fitness> calculateFitnessForAll(ArrayList<Individual> inds) {
		ArrayList<Fitness> fitList = new ArrayList<Fitness>(inds.size()); 
		for (Individual ind: inds) {
			fitList.add(calculateFitness(ind));
		}
		return fitList;
	}

}
