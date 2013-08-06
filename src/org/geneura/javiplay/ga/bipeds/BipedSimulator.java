package org.geneura.javiplay.ga.bipeds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedController;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedSimulator extends TestbedTest {

	private BipedFitnessConfig fitnessConf;
	private ArrayList<Individual> individuals;
	private int current_ind = 0;
	private boolean firstRun = true;
	

	public BipedSimulator(Individual ind) {
		super();
		fitnessConf = new BipedFitnessConfig(ind);
		fitnessConf.showFitness = true;
	}
	
	
	public BipedSimulator(ArrayList<Individual> inds) {
		super();
		fitnessConf = new BipedFitnessConfig(inds.get(0));
		fitnessConf.showFitness = true;
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

		boolean finished = fitnessConf.step(settings);
		

		addTextLine("Joint action: " + fitnessConf.getCurrentAction()
				+ ", duration: " + fitnessConf.getCurrentDuration() + "ms");
		addTextLine("Joint position X: " + fitnessConf.getJointPosition().x);
		addTextLine("Joint position Y: " + fitnessConf.getJointPosition().y);
		addTextLine("STEPS: " + fitnessConf.getStepCount());
		addTextLine("Gene: " + fitnessConf.getCurrentGeneCount());
		addTextLine("Individual: " + current_ind);

		if (finished) {
			reset();				
		}


	}

	@Override
	public void initTest(boolean arg0) {

		setTitle(getTestName());
		

		if (firstRun ) {
		BipedInitConfig initConfig = new BipedInitConfig(getWorld());
		fitnessConf.setMotors(initConfig.getMotors());
		firstRun = false;
		} else {
		load();
		ArrayList<RevoluteJoint> motors = new ArrayList<RevoluteJoint>();
		for (Joint j = m_world.getJointList(); j != null; j = j.getNext()) {
			motors.add((RevoluteJoint) j);
		}			
		fitnessConf.setMotors(motors);
		}

	}

	@Override
	public void reset() {
		
		
		super.reset();
		
		
		//fitnessConf.setMotors(motors);
	
		System.out.println(fitnessConf.getStepCount());
		
		fitnessConf.reset();
		current_ind = (current_ind + 1) % individuals.size();		
		if (current_ind == 0)
			firstRun = true;
				
		
		fitnessConf.setIndividual(individuals.get(current_ind));

	}

}
