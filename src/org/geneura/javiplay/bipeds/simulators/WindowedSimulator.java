package org.geneura.javiplay.bipeds.simulators;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

public class WindowedSimulator extends TestbedTest implements Simulator {
	
	private boolean fitnessHasFinished = false;
	
	private BipedDataAudit dataAudit = new BipedDataAudit();
	TestbedSettings settings = new TestbedSettings();
	/**
	 * @return the settings
	 */
	public TestbedSettings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(TestbedSettings settings) {
		this.settings = settings;
	}
	protected World world;
	private ArrayList<RevoluteJoint> motors;
	private FitnessStepCalculator fitnessStepCalculator;
	
	/**
	 * @return the motors
	 */
	public ArrayList<RevoluteJoint> getMotors() {
		return motors;
	}

	/**
	 * @param motors the motors to set
	 */
	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}
	
	
	@Override
	public String getTestName() {
		return "worldstate";
	}


	@Override
	public void initTest(boolean arg0) {

		BipedMorphology initConfig = new BipedMorphology(m_world);
		setMotors(initConfig.getMotors());
		dataAudit.reset(initConfig.getMotors().size());
		
	}
	
	
	@Override
	public ArrayList<RevoluteJoint> getJoints() {
		
		return motors;
	}

	@Override
	public BipedDataAudit getBipedDataAudit() {
		
		return dataAudit;
	}
	
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		dataAudit.save(motors);
		
		if (fitnessStepCalculator != null) {
			fitnessHasFinished = fitnessStepCalculator.fitnessStep();
		}
		
		addTextLine("Joint speed: " + motors.get(0).getJointSpeed());
		
		addTextLine("Joint: (X=" + dataAudit.getPositions().get(0).x + ", Y=" + dataAudit.getPositions().get(0).y);
		addTextLine("Contacts: (A=" + dataAudit.getStanceA() + ", B=" + dataAudit.getStanceB() + ")");
		
		addTextLine("Simulation Steps: " + getStepCount());
		
		
	}



	
	public FitnessStepCalculator getFitnessStepCalculator() {
		
		return fitnessStepCalculator;
	}

	@Override
	public boolean fitnessStep() {
		
		return true;
		
	}


	public void setFitnessStepCalculator(
			FitnessStepCalculator fitnessStepCalculator) {
		this.fitnessStepCalculator = fitnessStepCalculator;
		
	}


	@Override	
	public void reset() {
		
		super.reset();
		fitnessStepCalculator.fitnessStepReset();
		
		
	}

	@Override
	public void fitnessStepReset() {
		
		
	}

	@Override
	public void simulatorReset() {
		reset();		
	}

	@Override
	public boolean fitnessProcessed() {
		
		return fitnessHasFinished;
	}

	

}
