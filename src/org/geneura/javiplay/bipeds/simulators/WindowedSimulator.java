package org.geneura.javiplay.bipeds.simulators;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.util.impl.HashMapParameters;

public class WindowedSimulator extends TestbedTest implements Simulator {
	
	private boolean fitnessHasFinished = false;
	
	private BipedDataAudit simData;
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
	
	
	private FitnessStepCalculator fitnessStepCalculator;

	private HashMapParameters params;
	

	
	@Override
	public String getTestName() {
		return "worldstate";
	}


	@Override
	public void initTest(boolean arg0) {

		BipedMorphology.asThreeLinksOneKnee(m_world);
		simData = new BipedDataAudit(m_world, params);
		simData.reset();		
	}
	
	

	/**
	 * @return the params
	 */
	public HashMapParameters getParams() {
		return params;
	}

	/**
	 * @param params the params to set
	 */
	public void setParams(HashMapParameters params) {
		this.params = params;
	}

	@Override
	public BipedDataAudit getBipedDataAudit() {		
		return simData;
	}
	
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		simData.save();
		
		if (fitnessStepCalculator != null) {
			fitnessHasFinished = fitnessStepCalculator.fitnessStep();
		}
		
		//addTextLine("Joint speed: " + ((RevoluteJoint) joints.get(0)).getJointSpeed());
		
		addTextLine("Joint: (X=" + simData.getPositions().get(0).x + ", Y=" + simData.getPositions().get(0).y);
		addTextLine("Contacts: (A=" + simData.getFootContactA() + ", B=" + simData.getFootContactB() + ")");
		
		addTextLine("Simulation Steps: " + getStepCount());
		addTextLine("Energy: "+simData.getTotalEnergy());
		
		
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
