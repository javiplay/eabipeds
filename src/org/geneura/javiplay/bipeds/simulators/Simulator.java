package org.geneura.javiplay.bipeds.simulators;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;

import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;



public interface Simulator extends FitnessStepCalculator{
	

	void simulatorReset();
	int getStepCount();
	ArrayList<RevoluteJoint> getJoints();
	BipedDataAudit getBipedDataAudit();
	TestbedSettings getSettings();

}
