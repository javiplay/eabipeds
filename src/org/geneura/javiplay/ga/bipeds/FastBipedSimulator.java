package org.geneura.javiplay.ga.bipeds;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;
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
import org.jbox2d.serialization.JbDeserializer;
import org.jbox2d.serialization.JbSerializer;
import org.jbox2d.serialization.SerializationResult;
import org.jbox2d.serialization.UnsupportedObjectException;
import org.jbox2d.serialization.pb.PbDeserializer;
import org.jbox2d.serialization.pb.PbSerializer;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedSimulator {

	protected World m_world;
	TestbedSettings settings = new TestbedSettings();
	BipedFitnessConfig fitnessConf;
	boolean initLoaded;
	private JbSerializer serializer = new PbSerializer();
	private JbDeserializer deserializer  = new PbDeserializer();

	public void save() {

		SerializationResult result;
		result = serializer.serialize(m_world);
		FileOutputStream fos;
		try {
			fos = new FileOutputStream("worldstate.box2d");
			result.writeTo(fos);
		} catch (FileNotFoundException e3) {
			// TODO Auto-generated catch block
			e3.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		return;
	}

	protected void _load() {

		World w;
		try {
			FileInputStream fis = new FileInputStream("worldstate.box2d");
			w = deserializer.deserializeWorld(fis);
		} catch (FileNotFoundException e) {
			System.out.println(e.getMessage());
			return;
		} catch (UnsupportedObjectException e) {
			System.out.println(e.getMessage());
			return;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return;
		}
		m_world = w;

		//initTest(true);
		return;
	}

	public boolean step() {

		// WORLD STEP
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = hz > 0f ? 1f / hz : 0;
		m_world.step(timeStep,
				settings.getSetting(TestbedSettings.VelocityIterations).value,
				settings.getSetting(TestbedSettings.PositionIterations).value);

		return fitnessConf.step(settings);

	}

	public void initTest(boolean deserialized) {
		
		if (deserialized) {
			_load();
			ArrayList<RevoluteJoint> motors = new ArrayList<RevoluteJoint>();
			for (Joint j = m_world.getJointList(); j != null; j = j.getNext()) {
				motors.add((RevoluteJoint) j);
			}			
			fitnessConf.setMotors(motors);
			
		} else {
			
			BipedInitConfig initConfig = new BipedInitConfig(m_world);
			fitnessConf.setMotors(initConfig.getMotors());
		}

	}

	public double reset(Individual ind) {

		Vec2 gravity = new Vec2(0, -10f);
		m_world = new World(gravity, true);

		fitnessConf = new BipedFitnessConfig(ind);
		
		

		initTest(initLoaded);

		while (!step());

		return fitnessConf.getFitness();
	}

}
