package org.geneura.javiplay.bipeds.simulators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.geneura.javiplay.bipeds.logging.BipedLogger;
import org.geneura.javiplay.bipeds.morphology.BipedFitnessController;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.serialization.JbDeserializer;
import org.jbox2d.serialization.JbSerializer;
import org.jbox2d.serialization.SerializationResult;
import org.jbox2d.serialization.UnsupportedObjectException;
import org.jbox2d.serialization.pb.PbDeserializer;
import org.jbox2d.serialization.pb.PbSerializer;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedSimulator {

	protected World m_world;
	TestbedSettings settings = new TestbedSettings();
	public BipedFitnessController fitnessController;
	boolean initLoaded;
	private JbSerializer serializer = new PbSerializer();
	private JbDeserializer deserializer  = new PbDeserializer();
	BipedLogger logger = null;

	
	/**
	 * @return the logger
	 */
	public BipedLogger getLogger() {
		return logger;
	}

	/**
	 * @param logger the logger to set
	 */
	public void setLogger(BipedLogger logger) {
		this.logger = logger;
	}

	

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

		return fitnessController.step(settings);

	}

	public void initTest(boolean deserialized) {
		
		if (deserialized) {
			_load();
			ArrayList<RevoluteJoint> motors = new ArrayList<RevoluteJoint>();
			for (Joint j = m_world.getJointList(); j != null; j = j.getNext()) {
				motors.add((RevoluteJoint) j);
			}			
			fitnessController.setMotors(motors);
			
		} else {
			
			BipedMorphology initConfig = new BipedMorphology(m_world);
			fitnessController.setMotors(initConfig.getMotors());
		}

	}

	public double reset(Individual ind, int cycles, int n) {

		Vec2 gravity = new Vec2(0, -10f);
		m_world = new World(gravity, true);

		fitnessController = new BipedFitnessController(ind, cycles);		
		fitnessController.setLogger(logger);		
		logger.setFitnessController(fitnessController);
		logger.open(n);

		initTest(initLoaded);

		while (!step());
		
		logger.close();

		return fitnessController.getFitness();
	}

}
