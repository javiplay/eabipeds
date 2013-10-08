package org.geneura.javiplay.bipeds.ea.linearcontroller;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.MotorActions;
import org.geneura.javiplay.bipeds.simulators.FitnessStepCalculator;
import org.geneura.javiplay.bipeds.simulators.Simulator;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJoint;
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

public class LinearControllerFitnessCalculator extends OsgiliathService
		implements FitnessCalculator, FitnessStepCalculator {

	Individual individual;
	private double fitness;
	private double mean;
	private double var;
	private int stepsWorking;
	FeedforwardNetwork network;
	Simulator sim;
	int simulationSteps;
	double speed;

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	@Override
	public boolean fitnessStep() {
		BipedDataAudit data = sim.getBipedDataAudit();

		// check if the biped keeps standing up
		/*
		 * if (data.getPositions().get(0).y < 0.6) { setFitness(0); return true;
		 * }
		 */

		double outputs[] = network
				.computeOutputs(getInputs());
		processSpeedOutputs(outputs);

		double potEnergy = data.getTotalPotentialEnergy();

		if (potEnergy < 0.2) {
			setFitness(0);
			return true;
		}

		double energy = data.getTotalEnergy();

		mean += energy / simulationSteps;
		var += energy * energy / simulationSteps;

		// check the time has expired
		if (sim.getStepCount() >= simulationSteps) {

			// set the fitness
			var = var - mean * mean;

			// double distancePerUnit =
			// data.getMaxDistanceFromReference().get(0).x/10;
			// double workPerUnit = ((double) stepsWorking)/ (2 *
			// simulationSteps);

			// setFitness(0.5*distancePerUnit + 0.5*(1-workPerUnit));
			// setFitness(data.getMaxDistanceFromReference().get(0).x);

			// setFitness(0.5*Math.max(0,-(Math.abs(mean-1))+1) +
			// 0.5*Math.max(0, 1-var));

			setFitness(0.5 * Math.max(0, 1 - (Math.abs(mean - 0.9))) 
			// 0.2 * (1 - workPerUnit) +
			//0.5 * Math.max(0, 1 - var)
			);

			// setFitness(0.7*Math.max(0,-(Math.abs(mean-1))+1));
			return true;
		}

		return false;
	}

	private MotorActions getActionFromOutputs(double out1, double out2) {

		MotorActions action = MotorActions.FREE;

		if (out1 <= 0.5 && out2 <= 0.5) {
			action = MotorActions.FREE;
		} else if (out1 > 0.5 && out2 <= 0.5) {
			action = MotorActions.LEFT;
		} else if (out1 <= 0.5 && out2 > 0.5) {
			action = MotorActions.RIGHT;
		} else if (out1 > 0.5 && out2 > 0.5) {
			action = MotorActions.STOP;
		}
		return action;
	}

	private void processSpeedOutputs(double[] outputs) {

		int index = 0;
		int joints = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.JOINTS);
		for (int i = 0; i < joints; i++) {
			Joint joint = sim.getBipedDataAudit().getJoints().get(i);
			if (RevoluteJoint.class.isInstance(joint)) {
				RevoluteJoint j = (RevoluteJoint) joint;
				j.setMotorSpeed(2 * (float) outputs[index++]);
				j.enableMotor(true);
			}

		}

	}

	private void processOutputs(double[] outputs) {

		float s = (float) speed;
		int index = 0;
		int joints = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.JOINTS);
		for (int i = 0; i < joints; i++) {
			Joint joint = sim.getBipedDataAudit().getJoints().get(i);

			double out1 = outputs[index++];
			double out2 = outputs[index++];

			MotorActions action = getActionFromOutputs(out1, out2);
			if (RevoluteJoint.class.isInstance(joint)) {
				RevoluteJoint j = (RevoluteJoint) joint;
				switch (action) {
				case FREE:
					j.enableMotor(false);

					break;
				case LEFT:
					j.setMotorSpeed(-s);
					j.enableMotor(true);
					stepsWorking++;
					break;
				case RIGHT:
					j.setMotorSpeed(s);
					j.enableMotor(true);
					stepsWorking++;
					break;
				case STOP:
					j.setMotorSpeed(0);
					j.enableMotor(true);
					break;
				}
			}

			if (PrismaticJoint.class.isInstance(joint)) {
				PrismaticJoint j = (PrismaticJoint) joint;
				switch (action) {
				case FREE:
					j.enableMotor(false);
					break;
				case LEFT:
					j.setMotorSpeed(-s);
					j.enableMotor(true);
					break;
				case RIGHT:
					j.setMotorSpeed(s);
					j.enableMotor(true);
					break;
				case STOP:
					j.setMotorSpeed(0);
					j.enableMotor(true);
					break;
				}
			}

		}
	}

	public double normalizeAngle(double a) {
		return a - (2 * Math.PI) * Math.floor(a / (2 * Math.PI));
	}

	private double[] getInputsFromAnglesVelocitiesAndContacts() {

		ArrayList<Body> bodies = sim.getBipedDataAudit().getBodies();

		// (angles + angular velocity)*bodies + 2 feet contacts
		int inputsSize = bodies.size() * 2 + 2;

		double inputs[] = new double[inputsSize];
		int genecount = 0;
		for (Body b : bodies) {
			inputs[genecount++] = normalizeAngle(b.getAngle());
			inputs[genecount++] = b.getAngularVelocity();
		}
		inputs[genecount++] = sim.getBipedDataAudit().getFootContactA();
		inputs[genecount++] = sim.getBipedDataAudit().getFootContactB();

		return inputs;
	}

	private double[] getInputs() {

		int inputsSize = sim.getBipedDataAudit().getBodies().size() * 3 + 2;

		double inputs[] = new double[inputsSize];
		int genecount = 0;
		for (Body b : sim.getBipedDataAudit().getBodies()) {
			inputs[genecount++] = Math.sin(b.getAngle());
			inputs[genecount++] = Math.cos(b.getAngle());
			inputs[genecount++] = b.getAngularVelocity();
		}
		inputs[genecount++] = sim.getBipedDataAudit().getFootContactA();
		inputs[genecount++] = sim.getBipedDataAudit().getFootContactB();

		return inputs;
	}

	@Override
	public void fitnessStepReset() {
		mean = 0;
		var = 0;
		stepsWorking = 0;
		fitness = 0;
		sim = ((BipedProblem) getProblem()).getSimulator();

		// in ms
		int timeToTry = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.TIME_TO_TRY);
		int joints = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.JOINTS);
		int bodies = (Integer) getAlgorithmParameters().getParameter(
				UtilParams.BODIES);

		simulationSteps = timeToTry
				/ (1000 / sim.getSettings().getSetting(TestbedSettings.Hz).value);

		speed = (Double) getAlgorithmParameters().getParameter(
				UtilParams.MAX_SPEED);
		network = new FeedforwardNetwork();

		// INPUTS
		// 1 inputs from each body
		// 1 inputs from each angular velocity
		// 2 binary inputs from feet contacts

		network.addLayer(new FeedforwardLayer(3 * bodies + 2));

		// OUTPUTS
		// 2 bits per motor
		network.addLayer(new FeedforwardLayer(joints));
		setWeights();

	}

	void setWeights() {
		ArrayList<Gene> genes = ((ListGenome) individual.getGenome())
				.getGeneList();
		int genecount = 0;
		Matrix m = network.getInputLayer().getMatrix();

		for (int i = 0; i < m.getRows(); i++) {
			for (int j = 0; j < m.getCols(); j++) {
				double value = ((DoubleGene) genes.get(genecount)).getValue();
				m.set(i, j, value);
				genecount++;
			}
		}

	}

	@Override
	public Fitness calculateFitness(Individual ind) {
		this.individual = ind;
		sim = ((BipedProblem) getProblem()).getSimulator();
		sim.simulatorReset();
		fitnessStepReset();

		while (!fitnessProcessed())
			;

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
