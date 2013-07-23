package org.geneura.javiplay.ga.bipeds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedSimulator extends TestbedTest {
	
	BipedFitnessConfig fitnessConf;

	public BipedSimulator(Individual ind) {
		super();
		fitnessConf = new BipedFitnessConfig(ind);
		fitnessConf.showFitness = true;
	}
	
	@Override
	public String getTestName() {
		return "Two Legged Robot";
	}

	@Override
	public void step(TestbedSettings settings) {
		
		// WORLD STEP
		super.step(settings);
	
		
		boolean finished = fitnessConf.step(true, settings);
		if (finished) {
			reset();
		}
		
		
		addTextLine("Joint action: " + fitnessConf.getCurrentAction()
				+ ", duration: " + fitnessConf.getCurrentDuration() + "ms");
		addTextLine("Joint position X: " + fitnessConf.getJointPosition().x);
		addTextLine("Joint position Y: " + fitnessConf.getJointPosition().y);		
		addTextLine("STEPS: " + fitnessConf.getStepCount());
		addTextLine("Gene: " + fitnessConf.getCurrentGeneCount());
		addTextLine("Individual: " + fitnessConf.getIndividualCount());

	}

	@Override
	public void initTest(boolean arg0) {
				
		setTitle(getTestName());		
		BipedInitConfig initConfig = new BipedInitConfig(getWorld());		
		fitnessConf.setMotors(initConfig.getMotors());
	
		
	}

	@Override
	public void reset() {

		super.reset();
		fitnessConf.reset();

	}


	

}
