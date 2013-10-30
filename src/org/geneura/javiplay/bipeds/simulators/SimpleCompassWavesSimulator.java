package org.geneura.javiplay.bipeds.simulators;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

public class SimpleCompassWavesSimulator extends TestbedTest {

	TestbedSettings settings = new TestbedSettings();
	private BipedDataAudit simData;
	private HashMapParameters params;
	RevoluteJoint hipJoint;
	private float timeStep;
	private float time;
	private float desiredAngle;
	int prevA, prevB;
	int turn;
	private float timeBase;
	private float amplitude1;
	private float amplitude2;
	private float phase;
	private PrintWriter writer;
	private float prevJointX;
	private float prevDeltaX;
	

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

		
		
		
		writer.println(
				simData.getBodies().get(0).getAngle() + " " + 
				simData.getBodies().get(1).getAngle() + " " +  
				simData.getJointsPos().get(0).x + " " +
				simData.getJointsPos().get(0).y + " " +
				hipJoint.getJointAngle() + " " +
				hipJoint.getJointSpeed()) ; 
		
		// Visualization
		Vec2 pos;
		DecimalFormat df = new DecimalFormat("#.##");
		pos = new Vec2(-1, (float) (1 + desiredAngle * 0.1));
		getDebugDraw().drawPoint(pos, 5, Color3f.WHITE);
		getDebugDraw().drawString(getDebugDraw().getWorldToScreen(pos).x + 10,
				getDebugDraw().getWorldToScreen(pos).y,
				"Out 0:" + df.format(1 + desiredAngle), Color3f.RED);

		addTextLine("Contacts, A: " + simData.getFootContactA() + ", B: "
				+ simData.getFootContactB());
		addTextLine("Angles, A: " + hipJoint.getBodyA().getAngle() + ", B: "
				+ hipJoint.getBodyB().getAngle());
		addTextLine("Target: " + desiredAngle);
		addTextLine("Time: "+ (time-timeBase));
		addTextLine("Turn: "+ turn);

		super.step(settings);
		simData.save();
		
		float curX = simData.getJointsPos().get(0).x;
		
		float deltaX = curX-prevJointX;
		
	
		time += timeStep;
		desiredAngle = (float) Math.abs((0.5*Math.sin(10*time) + Math.PI/6));
		
//		if (Math.signum(prevDeltaX) != Math.signum(deltaX)) {
//			turn = (turn+1) %2;
//			timeBase = time;
//		}
	
		
	
			
/*
		if ((prevB==0) && (simData.getFootContactB()==1)) {
			turn = 0;
			timeBase = time;
		}
		if ((prevA==0) && (simData.getFootContactA()==1)) {
			turn = 1;
			timeBase = time;
		}
	*/	
//		// function 2
//		if (turn==1) {
//			//hipJoint.enableMotor(true);
//			desiredAngle = func1(time-timeBase);
//
//		} else {
//			//hipJoint.enableMotor(false);
//			desiredAngle = func2(time-timeBase);
//
//		}

		

		
		// function 3
//		
//		if ((simData.getFootContactA() == 1)
//				&& (simData.getFootContactB() == 1)) {
//
//			desiredAngle = 1;//hipJoint.getJointAngle() - 1f;
//
//		}

		// P control
		float angleError = hipJoint.getJointAngle() - desiredAngle;
		float gain = 3f;
		hipJoint.setMotorSpeed(-gain * angleError);
		
		prevA = simData.getFootContactA();
		prevB = simData.getFootContactB();
		prevJointX = simData.getJointsPos().get(0).x;
		prevDeltaX = deltaX;

	}

//	private float func2(float angle) {
//		float amplitude2 = 0.5f;
//		float frequency2 = 30f;
//		float phase2 = 0;
//		return (float) (amplitude2 * Math.sin(frequency2 * angle + phase2));
//	}
//
//	private float func1(float angle) {
//		float amplitude1 = 0.5f;
//		float frequency1 = 30f;
//		float phase1 = (float) (Math.PI/4f);
//		return (float) (amplitude1 * Math.sin(frequency1 * angle + phase1));
//	}
//
	
	private float func2(float angle) {
		
		float frequency2 = 100f;
		
		return (float) (amplitude2 * Math.exp(-frequency2*Math.pow(angle - phase, 2)));
	}

	private float func1(float angle) {
		
		float frequency1 = 100f;
		
		return (float) (amplitude1 * Math.exp(-frequency1 * Math.pow(angle - phase, 2)));
	}

	@Override
	public String getTestName() {
		return "Interactive";
	}

	@Override
	public void initTest(boolean arg0) {

		setCamera(new Vec2(0f, 1f), 250f);
		setTitle(getTestName());
		hipJoint = BipedMorphology.asCompassLike(getWorld());

		simData = new BipedDataAudit(getWorld(), params);

		float hz = settings.getSetting(TestbedSettings.Hz).value;
		timeStep = 1f / hz;
		time = 0;

		
		try {
			writer = new PrintWriter("waves1","UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		amplitude1 = 0.7f;
		amplitude2 = 1.0f;
		phase = 0.3f;
	}

	@Override
	public void keyPressed(char argKeyChar, int argKeyCode) {

		switch (argKeyChar) {
		case 's':
			amplitude1 = 0.6f;
			amplitude2 = 1.2f;
			phase = 0.25f;
			break;
		case 'f':
			amplitude1 = 1.0f;
			amplitude2 = 0.7f;
			break;
		case 'g':
			amplitude1 = 0.6f;
			amplitude2 = 1.2f;
			phase = 0.35f;
			break;
		case 'h':
			 writer.flush();
	    	 writer.close();
	    	 break;

		}

	}

	@Override
	public void reset() {

		super.reset();

	}

}
