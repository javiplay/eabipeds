package org.geneura.javiplay.bipeds.simulators;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.logging.BipedLogger;
import org.geneura.javiplay.bipeds.morphology.BehaviorFitnessController;
import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.individual.Individual;

public class TestbedBehaviorSimulator extends TestbedTest implements Simulator{

	private BehaviorFitnessController fitnessController;
	private ArrayList<Individual> individuals;
	private int current_ind = 0;
	
	private boolean restart = true;
	private boolean loadFromFile= false;
	private BipedLogger logger = null;
		
	

	public TestbedBehaviorSimulator(ArrayList<Individual> inds, int cycles) {
		super();
		setFitnessController(new BehaviorFitnessController(inds.get(0)));
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
		
		BehaviorFitnessController c = getFitnessController();
		
		boolean finished = false;

		if (!settings.pause) {
			finished = c.step(settings);
		}
		addTextLine("Joint action: " + c.getCurrentAction()
				+ ", duration: " + c.getCurrentDuration() + "ms"
				+ ", speed: " + c.getCurrentSpeed());
		
		addTextLine("Joint: (X=" + c.getJointPosition().x + ", Y=" + c.getJointPosition().y);
		addTextLine("Contacts: (A=" + c.getStanceA() + ", B=" + c.getStanceB() + ")");
		
		addTextLine("Simulation Steps: " + c.getStepCount());
		addTextLine("Gene: " + c.getCurrentGeneCount());
		addTextLine("Individual: " + current_ind);

		if (finished) reset();

	}

	@Override
	public void initTest(boolean arg0) {

		setTitle(getTestName());

		if (restart) {
			BipedMorphology initConfig = new BipedMorphology(getWorld());
			getFitnessController().setMotors(initConfig.getMotors());
			restart = false;
		} 
		
		if (loadFromFile){
			
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
		restart = true;
		current_ind = (current_ind + 1) % individuals.size();		
		getFitnessController().setIndividual(individuals.get(current_ind));

	}

	/**
	 * @return the fitnessController
	 */
	public BehaviorFitnessController getFitnessController() {
		return fitnessController;
	}

	/**
	 * @param fitnessController the fitnessController to set
	 */
	public void setFitnessController(BehaviorFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}


	
	@Override
	public ArrayList<RevoluteJoint> getJoints() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BipedDataAudit getBipedDataAudit() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFitnessStepCalculator(
			FitnessStepCalculator fitnessStepCalculator) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FitnessStepCalculator getFitnessStepCalculator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean fitnessStep(Individual ind) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean fitnessStep() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void simulatorStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TestbedSettings getSettings() {
		// TODO Auto-generated method stub
		return null;
	}

}
