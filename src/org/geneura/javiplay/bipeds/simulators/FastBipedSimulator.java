package org.geneura.javiplay.bipeds.simulators;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.logging.BipedLogger;
import org.geneura.javiplay.bipeds.morphology.BehaviorFitnessController;
import org.geneura.javiplay.bipeds.morphology.BipedMorphology;
import org.geneura.javiplay.bipeds.morphology.BipedDataAudit;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.serialization.JbDeserializer;
import org.jbox2d.serialization.JbSerializer;
import org.jbox2d.serialization.SerializationResult;
import org.jbox2d.serialization.UnsupportedObjectException;
import org.jbox2d.serialization.pb.PbDeserializer;
import org.jbox2d.serialization.pb.PbSerializer;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedSimulator implements Simulator{

	private BipedDataAudit dataAudit;
	private JbDeserializer deserializer  = new PbDeserializer();
	
	private FitnessStepCalculator fitnessStepCalculator;
	boolean initLoaded;

	private ArrayList<RevoluteJoint> motors;

	private JbSerializer serializer = new PbSerializer();
	TestbedSettings settings = new TestbedSettings();

	private int stanceA;

	
	private int stanceB;
	private int stepCount;
	protected World world;
	
	

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
		world = w;

		//initTest(true);
		return;
	}

	

	@Override
	public boolean fitnessStep() {
		simulatorStep();
		return fitnessStepCalculator.fitnessStep();
		
	}

	@Override
	public BipedDataAudit getBipedDataAudit() {
		
		return dataAudit;
	}
	@Override
	public FitnessStepCalculator getFitnessStepCalculator() {
		
		return fitnessStepCalculator;
	}

	@Override
	public ArrayList<RevoluteJoint> getJoints() {
		
		return motors;
	}
	
	public ArrayList<Vec2> getJointsPos() {
		ArrayList<Vec2> jointsPos=new ArrayList<Vec2>();
		for (RevoluteJoint motor: motors) {
			Vec2 pos = new Vec2();
			motor.getAnchorA(pos);
			jointsPos.add(pos);			
		}
		return jointsPos;
		
	}
	/**
	 * @return the motors
	 */
	public ArrayList<RevoluteJoint> getMotors() {
		return motors;
	}
	
	/**
	 * @return the settings
	 */
	public TestbedSettings getSettings() {
		return settings;
	}
	public int getStanceA() {
		stanceA = 0;
		ContactEdge c = motors.get(0).getBodyA().getContactList();

		while (c != null) {
			if (c.contact.isTouching()) {
				stanceA = 1;
			}
			c = c.next;
		}

		return stanceA;
	}
	
	public int getStanceB() {
		stanceB = 0;
		ContactEdge c = motors.get(0).getBodyB().getContactList();

		while (c != null) {
			if (c.contact.isTouching()) {
				stanceB = 1;
			}
			c = c.next;
		}

		return stanceB;
	}

	
	@Override
	public int getStepCount() {
		
		return stepCount;
	}

	/**
	 * @return the world
	 */
	public World getWorld() {
		return world;
	}

	public void initTest(boolean deserialized) {
		
		if (deserialized) {
			_load();
			ArrayList<RevoluteJoint> motors = new ArrayList<RevoluteJoint>();
			for (Joint j = world.getJointList(); j != null; j = j.getNext()) {
				motors.add((RevoluteJoint) j);
			}			
			//behaviorFitnessController.setMotors(motors);
			
		} else {
			
			BipedMorphology initConfig = new BipedMorphology(world);
			setMotors(initConfig.getMotors());
			dataAudit.reset(initConfig.getMotors().size());
		}

	}

	@Override
	public void simulatorReset() {
		Vec2 gravity = new Vec2(0, -10f);
		world = new World(gravity, true);
		dataAudit = new BipedDataAudit();
		stepCount = 0;
		initTest(false);
	}

	public void save() {

		SerializationResult result;
		result = serializer.serialize(world);
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

	@Override
	public void setFitnessStepCalculator(FitnessStepCalculator fitnessStepCalculator) {
		this.fitnessStepCalculator = fitnessStepCalculator;
		
	}

	/**
	 * @param motors the motors to set
	 */
	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(TestbedSettings settings) {
		this.settings = settings;
	}

	/**
	 * @param world the world to set
	 */
	public void setWorld(World world) {
		this.world = world;
	}


	public void simulatorStep() {

		// WORLD STEP
		float hz = settings.getSetting(TestbedSettings.Hz).value;
		float timeStep = hz > 0f ? 1f / hz : 0;
		world.step(timeStep,
				settings.getSetting(TestbedSettings.VelocityIterations).value,
				settings.getSetting(TestbedSettings.PositionIterations).value);
		
		dataAudit.save(motors);

		stepCount++;

	}





	


}
