package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.morphology.MotorSpeed;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedFitnessController {

	private ArrayList<Gene> genes;
	private BipedPositionFitness fit;
	private int currentGene;
	private int accDuration;
	private int individualCount;
	private int stepCount;
	private double fitness;
	public boolean showFitness;
	public float velocityVariance = 0;
	private ArrayList<RevoluteJoint> motors;
	private int next_action_step;
	private int cycle;


	public BipedFitnessController(Individual ind, int cycles) {
		genes = ((ListGenome) ind.getGenome()).getGeneList();		

		fit = new BipedPositionFitness(cycles);
		cycle = 0;

	}

	public String getCurrentAction() {		
		return ((BipedGene) genes.get(currentGene)).actions.get(0).toString();
	}

	
	public int getCurrentDuration() {
		return ((BipedGene) genes.get(currentGene)).duration;
	}

	public boolean step(TestbedSettings settings) {
		stepCount++;

		Vec2 motor_pos = new Vec2();
		getMotors().get(0).getAnchorA(motor_pos);

		fit.addPosition(motor_pos);
		/*if (motor_pos.y < 0.5) {
			setFitness(Float.MAX_VALUE);
			return true;
			
		}*/
		
		// check next gene with accumulated duration (in steps) of the actions					
		if (stepCount >= next_action_step) {
			
			accDuration += ((BipedGene) genes.get(currentGene)).duration;
			
			next_action_step = accDuration
					/ (1000 / settings.getSetting(TestbedSettings.Hz).value);
			
			// execute the action
			processGene((BipedGene) genes.get(currentGene));

			// accumulate the action duration							
			currentGene++;
			if (currentGene == genes.size()) {
				cycle++;
				currentGene = 0;
			}
			
			if ((currentGene + 1)*(cycle + 1) == genes.size()*fit.cycles) {
								
				// the fitness
				float score = fit.toRight();
				setFitness(score);
				if (showFitness)
					System.out.println("Fitness live:" + getFitness());
				return true;					
				
			}		
			
			
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
		fit.reset();

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
			float speed = MotorSpeed.MAX_SPEED;

			switch (g.actions.get(i)) {
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
