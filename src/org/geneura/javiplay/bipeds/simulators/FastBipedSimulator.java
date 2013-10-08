package org.geneura.javiplay.bipeds.simulators;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class FastBipedSimulator implements Simulator {

	private FitnessStepCalculator fitnessStepCalculator;
	TestbedSettings settings = new TestbedSettings(); // default settings
	private BipedDataAudit simData;
	private int stepCount;
	protected World world;
	private HashMapParameters params;
	private boolean firstReset = true;

	@Override
	public boolean fitnessProcessed() {
		return fitnessStep();
	}

	@Override
	public boolean fitnessStep() {
		simulatorStep();
		return fitnessStepCalculator.fitnessStep();

	}

	@Override
	public void fitnessStepReset() {
		// TODO Auto-generated method stub

	}

	@Override
	public BipedDataAudit getBipedDataAudit() {
		return simData;
	}

	public FitnessStepCalculator getFitnessStepCalculator() {
		return fitnessStepCalculator;
	}

	/**
	 * @return the settings
	 */
	public TestbedSettings getSettings() {
		return settings;
	}

	@Override
	public int getStepCount() {

		return stepCount;
	}

	public void initTest() {
		new BipedMorphology(world);
		simData = new BipedDataAudit(world, params);
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

	public void setFitnessStepCalculator(
			FitnessStepCalculator fitnessStepCalculator) {
		this.fitnessStepCalculator = fitnessStepCalculator;

	}

	/**
	 * @param settings
	 *            the settings to set
	 */
	public void setSettings(TestbedSettings settings) {
		this.settings = settings;
	}

	public void simulatorReset() {
		stepCount = 0;
		//if (firstReset)  {
			Vec2 gravity = new Vec2(0, -10f);
			world = new World(gravity, true);			
			initTest();
			//firstReset = false;
		//}
	}

	public void simulatorStep() {

		// WORLD STEP
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = hz > 0f ? 1f / hz : 0;
		world.step(timeStep,
				settings.getSetting(TestbedSettings.VelocityIterations).value,
				settings.getSetting(TestbedSettings.PositionIterations).value);

		simData.save();

		stepCount++;

	}

}
