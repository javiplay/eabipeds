package org.geneura.javiplay.bipeds.simulators;

import java.text.DecimalFormat;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.geneura.javiplay.ctrnn.CTRNN;
import org.jbox2d.common.Color3f;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.PrismaticJoint;

import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.util.impl.HashMapParameters;

public class CTRNNPendulumSimulator extends TestbedTest {

	CTRNN network;
	TestbedSettings settings = new TestbedSettings();
	Joint joint1;
	private BipedDataAudit simData;
	private HashMapParameters params;
	private Joint joint0;
	private boolean networkEnabled;

	/**
	 * @return the params
	 */
	public HashMapParameters getParams() {
		return params;
	}

	/**
	 * @param params
	 *            the params to set
	 */
	public void setParams(HashMapParameters params) {
		this.params = params;
	}

	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		RevoluteJoint j0 = (RevoluteJoint) joint0;
		RevoluteJoint j1 = (RevoluteJoint) joint1;

		double[] input = { j1.getJointAngle() };
		network.setInput(input);
		
		if (networkEnabled) {
			network.step();

			double[] out = network.getOuput();
			float angle0Error = (float) (((RevoluteJoint) joint0)
					.getJointAngle() - out[0]);
//			
//			float angle1Error = (float) (((RevoluteJoint) joint1)
//					.getJointAngle() - out[1]);
			float gain = 2f;

			((RevoluteJoint) joint0).setMotorSpeed(-gain * angle0Error);
//			((RevoluteJoint) joint1).setMotorSpeed(-gain * angle1Error);

			Vec2 pos;
			DecimalFormat df = new DecimalFormat("#.##");
			
			pos = new Vec2(-1, (float) (1 + out[0] * 0.1));
			getDebugDraw().drawPoint(pos, 5, Color3f.WHITE);
			getDebugDraw().drawString(
					getDebugDraw().getWorldToScreen(pos).x + 10,
					getDebugDraw().getWorldToScreen(pos).y,
					"Out 0:" + df.format(out[0]), Color3f.RED);

//			pos = new Vec2(-1.2f, (float) (1 + out[1] * 0.1));
//			getDebugDraw().drawPoint(pos, 5, Color3f.WHITE);
//			getDebugDraw().drawString(
//					getDebugDraw().getWorldToScreen(pos).x + 10,
//					getDebugDraw().getWorldToScreen(pos).y, 
//					"Out 1:" + df.format(out[1]), Color3f.RED);

		}

	}

	@Override
	public String getTestName() {
		return "Interactive";
	}

	@Override
	public void initTest(boolean arg0) {

		setCamera(new Vec2(0f, 1.5f), 250f);
		setTitle(getTestName());
		BipedMorphology initConfig = new BipedMorphology(getWorld());
		joint1 = initConfig.getJoints().get(1);
		joint0 = initConfig.getJoints().get(0);
		simData = new BipedDataAudit(getWorld(), params);
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = 1f / hz;
		System.out.println("timeStep: " + timeStep);
		
		network = new CTRNN(1, timeStep);
		
		double[] y0 = { ((RevoluteJoint) joint1).getBodyA().getAngle() };
		// double[] y0 = {((RevoluteJoint) joint1).getJointSpeed()};
		network.random(y0);
		
		networkEnabled = false;

	}

	@Override
	public void keyPressed(char argKeyChar, int argKeyCode) {

		switch (argKeyChar) {
		case 's':
			networkEnabled = true;
			((RevoluteJoint) joint0).enableMotor(true);
			//((RevoluteJoint) joint1).enableMotor(true);
			break;
		case 'f':
			networkEnabled = true;
			((RevoluteJoint) joint0).enableMotor(false);
			//((RevoluteJoint) joint1).enableMotor(true);
			break;
			

		}

	}

	@Override
	public void reset() {

		super.reset();

	}

}
