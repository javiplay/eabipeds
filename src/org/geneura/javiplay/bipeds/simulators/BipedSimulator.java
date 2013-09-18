package org.geneura.javiplay.bipeds.simulators;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedFitnessController;
import org.geneura.javiplay.bipeds.morphology.CompassLike;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedSimulator extends TestbedTest {

	private BipedFitnessController fitnessController;
	private ArrayList<Individual> individuals;
	private int current_ind = 0;
	private boolean firstRun = true;
	
	public BipedSimulator(Individual ind, int cycles) {
		super();
		setFitnessController(new BipedFitnessController(ind, cycles));
		getFitnessController().showFitness = true;
	}

	public BipedSimulator(ArrayList<Individual> inds, int cycles) {
		super();
		setFitnessController(new BipedFitnessController(inds.get(0), cycles));
		getFitnessController().showFitness = true;
		individuals = inds;

	}

	@Override
	public String getTestName() {
		return "worldstate";
	}

	@Override
	public void step(TestbedSettings settings) {

		// WORLD STEP
		super.step(settings);

		boolean finished = getFitnessController().step(settings);

		addTextLine("Joint action: " + getFitnessController().getCurrentAction()
				+ ", duration: " + getFitnessController().getCurrentDuration() + "ms");
		addTextLine("Joint position X: " + getFitnessController().getJointPosition().x);
		addTextLine("Joint position Y: " + getFitnessController().getJointPosition().y);
		addTextLine("STEPS: " + getFitnessController().getStepCount());
		addTextLine("Gene: " + getFitnessController().getCurrentGeneCount());
		addTextLine("Individual: " + current_ind);

		if (finished) fitnessReset();

	}

	@Override
	public void initTest(boolean arg0) {

		setTitle(getTestName());

		if (firstRun) {
			CompassLike initConfig = new CompassLike(getWorld());
			getFitnessController().setMotors(initConfig.getMotors());
			firstRun = false;
		} else {

			load();
			ArrayList<RevoluteJoint> motors = new ArrayList<RevoluteJoint>();
			for (Joint j = m_world.getJointList(); j != null; j = j.getNext()) {
				motors.add((RevoluteJoint) j);
			}
			getFitnessController().setMotors(motors);

		}

	}
	
	public void fitnessReset() {
		
		getFitnessController().reset();
		current_ind = (current_ind + 1) % individuals.size();
		// if (current_ind == 0) firstRun = true;

		getFitnessController().setIndividual(individuals.get(current_ind));
	}

	@Override
	public void reset() {

		super.reset();

		

		System.out.println(getFitnessController().getStepCount());

		getFitnessController().reset();
		current_ind = (current_ind + 1) % individuals.size();
		if (current_ind == 0) firstRun = true;

		getFitnessController().setIndividual(individuals.get(current_ind));

	}

	/**
	 * @return the fitnessController
	 */
	public BipedFitnessController getFitnessController() {
		return fitnessController;
	}

	/**
	 * @param fitnessController the fitnessController to set
	 */
	public void setFitnessController(BipedFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}

}
