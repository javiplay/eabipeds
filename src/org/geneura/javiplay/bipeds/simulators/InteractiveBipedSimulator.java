package org.geneura.javiplay.bipeds.simulators;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.geneura.javiplay.bipeds.morphology.MotorSpeed;
import org.jbox2d.common.MathUtils;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.individual.Individual;

public class InteractiveBipedSimulator extends TestbedTest {

	

	RevoluteJoint motor;

	@Override
	public String getTestName() {
		return "interactive";
	}


	@Override
	public void initTest(boolean arg0) {

		setTitle(getTestName());
		BipedMorphology initConfig = new BipedMorphology(getWorld());
		motor = initConfig.getMotors().get(0);

		

	}


	@Override
	  public void keyPressed(char argKeyChar, int argKeyCode) {
		float speed = 1;
	
	    switch (argKeyChar) {
	      case 's':
	    	  motor.enableMotor(false);
	        break;
	      case 'a':
	    	  motor.setMotorSpeed(-speed);
				motor.enableMotor(true);
	        break;
	      case 'd':
	    	  motor.setMotorSpeed(speed);
				motor.enableMotor(true);
	        
	        break;
	      case 'w':
	    	  motor.setMotorSpeed(0);
				motor.enableMotor(true);
	        
	        break;
	    }
	  }


	@Override
	public void reset() {

		super.reset();

		

	}

}
