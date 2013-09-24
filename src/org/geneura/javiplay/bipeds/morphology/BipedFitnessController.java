package org.geneura.javiplay.bipeds.morphology;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.BipedGene;
import org.geneura.javiplay.bipeds.logging.BipedLogger;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedFitnessController {

	private ArrayList<Gene> genes;
	private BipedPositionFitness positionFitness;
	private BipedLogger logger;

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

	private int currentGene;
	private int accDuration;
	private int individualCount;
	private int stepCount;
	private double fitness;
	public boolean showFitness;
	

	private ArrayList<RevoluteJoint> motors;
	private int next_action_step;
	private int cycle;
	public Vec2 velocity;

	private int stanceA;
	private int stanceB;

	public BipedFitnessController(Individual ind, int cycles) {
	
		
		genes = ((ListGenome) ind.getGenome()).getGeneList();
		positionFitness = new BipedPositionFitness(cycles);
		cycle = 0;

	}

	public MotorActions getCurrentAction() {
		return ((BipedGene) genes.get(currentGene)).getActions().get(0);
	}

	public int getCurrentDuration() {
		return ((BipedGene) genes.get(currentGene)).getDuration();
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

	public float getCurrentSpeed() {
		return ((BipedGene) genes.get(currentGene)).getSpeed();
	}

	public boolean step(TestbedSettings settings) {

		stepCount++;

		Vec2 motor_pos = new Vec2();
		getMotors().get(0).getAnchorA(motor_pos);

		velocity = motor_pos.add(positionFitness.getLastPosition().negate());

		positionFitness.addPosition(motor_pos);

		// check if the biped keeps standing up
		if (motor_pos.y < 0.5) {
			setFitness(Float.MAX_VALUE);
			logger.clear();
			return true;
		}

		if (logger!=null) {
			logger.writeBodyAngles();
		}

		// check next gene with accumulated duration (in steps) of the actions
		if (stepCount >= next_action_step) {

			if ((currentGene + 1) * (cycle + 1) == genes.size()
					* positionFitness.getCycles()) {

				// the fitness
				float score = positionFitness.toRight();
				setFitness(score);
				if (showFitness)
					System.out.println("Fitness live:" + getFitness());

				
				return true;

			}

			if (stepCount != 1)
				currentGene++;

			if (currentGene == genes.size()) {
				cycle++;
				currentGene = 0;
			}

	
			// accumulate the action duration
			accDuration += ((BipedGene) genes.get(currentGene)).getDuration();
			next_action_step = accDuration
					/ (1000 / settings.getSetting(TestbedSettings.Hz).value);

			// execute the action
			processGene((BipedGene) genes.get(currentGene));


		}
		return false;
	}

	public void reset() {
		currentGene = 0;
		stepCount = 0;
		accDuration = 0;
		individualCount = 0;
		next_action_step = 0;
		cycle = 0;
		positionFitness.reset();

	}

	public Vec2 getJointPosition() {
		Vec2 pos = new Vec2();
		getMotors().get(0).getAnchorA(pos);
		return pos;

	}

	public ArrayList<RevoluteJoint> getMotors() {

		return motors;
	}

	public int getStepCount() {
		return stepCount;
	}

	public int getCurrentGeneCount() {

		return currentGene;
	}

	public int getIndividualCount() {

		return individualCount;
	}

	public void processGene(BipedGene g) {

		for (int i = 0; i < getMotors().size(); i++) {
			RevoluteJoint joint = getMotors().get(i);
			float speed = g.getSpeed();

			switch (g.getActions().get(i)) {
			case FREE:
				joint.enableMotor(false);
				break;
			case LEFT:
				joint.setMotorSpeed(-speed);
				joint.enableMotor(true);
				break;
			case RIGHT:
				joint.setMotorSpeed(speed);
				joint.enableMotor(true);
				break;
			case STOP:
				joint.setMotorSpeed(0);
				joint.enableMotor(true);
				break;
			}

		}
	}

	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}

	public float getAngleA() {
		return getMotors().get(0).getBodyA().getAngle();

	}

	public float getAngleB() {
		return getMotors().get(0).getBodyB().getAngle();
	}

	public double getFitness() {
		return fitness;
	}

	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

	public void setIndividual(Individual ind) {
		genes = ((ListGenome) ind.getGenome()).getGeneList();
	}

}
