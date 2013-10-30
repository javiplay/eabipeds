package org.geneura.javiplay.bipeds.simulators;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.PrismaticJoint;

import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.util.impl.HashMapParameters;

public class InteractiveBipedSimulator extends TestbedTest {

	

	PrintWriter writer;
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
		addTextLine("Energy:" + simData.getTotalEnergy());
		writer.println(simData.getBodies().get(0).getAngle() + " " + simData.getBodies().get(1).getAngle() + " " +
		simData.getFootContactA() + " " + simData.getFootContactB());
	}

	@Override
	public String getTestName() {
		return "Interactive";
	}


	@Override
	public void initTest(boolean arg0) {

		setTitle(getTestName());
		BipedMorphology initConfig = new BipedMorphology(getWorld());
		joint = initConfig.getJoints().get(0);
		simData = new BipedDataAudit(getWorld(), params);	
		
		try {
			writer = new PrintWriter("datos","UTF-8");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	  public void keyPressed(char argKeyChar, int argKeyCode) {
		float speed = 4; 
		switch (argKeyChar) {
	      case 'f':
	    	 writer.flush();
	    	 writer.close();
	        break;
		}
		
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
