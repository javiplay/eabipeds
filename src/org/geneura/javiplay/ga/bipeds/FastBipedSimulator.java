package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedSimulator {

	protected World m_world;
	TestbedSettings settings = new TestbedSettings();
	BipedFitnessConfig fitnessConf;

	public boolean step() {
		

		// WORLD STEP
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = hz > 0f ? 1f / hz : 0;
		m_world.step(timeStep,
				settings.getSetting(TestbedSettings.VelocityIterations).value,
				settings.getSetting(TestbedSettings.PositionIterations).value);
		
		return fitnessConf.step(false, settings);
		
		
	}

	public void initTest() {
		
		BipedInitConfig initConfig = new BipedInitConfig(m_world);
		fitnessConf.setMotors(initConfig.getMotors());
		
		
	}


	public double reset(Individual ind) {
		
		
		Vec2 gravity = new Vec2(0, -10f);
		m_world = new World(gravity, true);
		
		fitnessConf = new BipedFitnessConfig(ind);
		
		initTest();

		while (!step());
		
		
		return fitnessConf.getFitness();
	}

	

	

}
