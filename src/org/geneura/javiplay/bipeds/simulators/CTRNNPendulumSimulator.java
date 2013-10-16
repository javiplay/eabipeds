package org.geneura.javiplay.bipeds.simulators;

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
	Joint joint;
	private BipedDataAudit simData;
	private HashMapParameters params;
	
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
	public synchronized void step(TestbedSettings settings) {
		super.step(settings);
		RevoluteJoint j = (RevoluteJoint) joint;
		
		double[] input = { j.getJointAngle(), j.getJointSpeed() };
		network.setInput(input);
		network.step();
		
		
		
		double[] out = network.getOuput();
	
		
		float value = (float) out[0];
		
		//j.enableMotor(true);
		//j.setMotorSpeed(value);
		
		//addTextLine("Energy:" + simData.getTotalEnergy());
		addTextLine("Cam Pos:" + getCachedCameraPos());
		addTextLine("Scale:" + getCachedCameraScale());
		Color3f color =  new Color3f(1,1,1);
		Color3f textColor =  new Color3f(1,0,0);
		Vec2 pos = new Vec2(-1, 1 + j.getJointSpeed()*0.1f);
		getDebugDraw().drawPoint(pos, 5, color);
		getDebugDraw().drawString(getDebugDraw().getWorldToScreen(pos).x+10, getDebugDraw().getWorldToScreen(pos).y, "Value:" + value , textColor);
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
		joint = initConfig.getJoints().get(0);
		simData = new BipedDataAudit(getWorld(), params);	
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = 1f / hz;
		System.out.println("timeStep: " +timeStep);
		network = new CTRNN(2, timeStep);
		double[] y0 = {((RevoluteJoint) joint).getJointSpeed(), 0.1};
		network.reset(y0);

	}


	@Override
	  public void keyPressed(char argKeyChar, int argKeyCode) {
		float speed = 4;
		
		if (PrismaticJoint.class.isInstance(joint)) {
			 switch (argKeyChar) {
		      case 's':
		    	  ((PrismaticJoint)joint).enableMotor(false);
		        break;
		      case 'a':
		    	  ((PrismaticJoint)joint).setMotorSpeed(-speed);
		    	  ((PrismaticJoint)joint).enableMotor(true);
		        break;
		      case 'd':
		    	  ((PrismaticJoint)joint).setMotorSpeed(speed);
		    	  ((PrismaticJoint)joint).enableMotor(true);
		        
		        break;
		      case 'w':
		    	  ((PrismaticJoint)joint).setMotorSpeed(0);
		    	  ((PrismaticJoint)joint).enableMotor(true);
		        
		        break;
		    }
		}
		if (RevoluteJoint.class.isInstance(joint)) {
			 switch (argKeyChar) {
		      case 's':
		    	  ((RevoluteJoint)joint).enableMotor(false);
		        break;
		      case 'a':
		    	  ((RevoluteJoint)joint).setMotorSpeed(-speed);
		    	  ((RevoluteJoint)joint).enableMotor(true);
		        break;
		      case 'd':
		    	  ((RevoluteJoint)joint).setMotorSpeed(speed);
		    	  ((RevoluteJoint)joint).enableMotor(true);
		        
		        break;
		      case 'w':
		    	  ((RevoluteJoint)joint).setMotorSpeed(0);
		    	  ((RevoluteJoint)joint).enableMotor(true);		        
		        break;
		    }
		}
	
	   
	  }


	@Override
	public void reset() {

		super.reset();

		

	}

}
