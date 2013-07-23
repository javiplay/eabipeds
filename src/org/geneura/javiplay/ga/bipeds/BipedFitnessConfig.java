package org.geneura.javiplay.ga.bipeds;

import java.io.IOException;
import java.util.ArrayList;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.testbed.framework.TestbedSettings;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedFitnessConfig {

	private Individual individual;
	private int current_gene;
	private int acc_duration;
	private int individualCount;
	private int stepCount;
	private double fitness;
	public boolean showFitness;

	private ArrayList<RevoluteJoint> motors;
	Vec2 target_pos = new Vec2(2.2f, 1.2f);
	BipedLogger log;

	public BipedFitnessConfig(Individual ind) {
		this.individual = ind;
		log = new BipedLogger(this);
	}
	

		

	public String getCurrentAction() {
			
		return ((BipedGene) ((ListGenome) individual.getGenome()).getGeneList()
				.get(current_gene)).actions.get(0).toString();
	
	}

	public int getCurrentDuration() {

		return ((BipedGene) ((ListGenome) individual.getGenome()).getGeneList()
				.get(current_gene)).duration;
	}

	public boolean step(boolean logging, TestbedSettings settings) {
		stepCount++;

		Vec2 motor_pos = new Vec2();
		getMotors().get(0).getAnchorA(motor_pos);

		float distance = (float) Math.sqrt(Math.pow(
				(motor_pos.x - target_pos.x), 2)
				+ Math.pow((motor_pos.y - target_pos.y), 2));
		
		setFitness(distance);

		ArrayList<Gene> genes = ((ListGenome) individual.getGenome())
				.getGeneList();

		if (current_gene < genes.size()) {

			if (logging) {
				log.writeBodyAngles();
				log.writeJointPosition();
				log.writeActionAngles();
			}

			// check next gene with accumulated duration (in steps) of the
			// actions
			if (stepCount > acc_duration
					/ (1000 / settings.getSetting(TestbedSettings.Hz).value)) {
				processGene((BipedGene) genes.get(current_gene));
				acc_duration += ((BipedGene) genes.get(current_gene)).duration;
				current_gene++;
				if (current_gene == genes.size()) {
					// fitness = distance;
					if (showFitness) 
						System.out.println("Fitness live:" + getFitness());
					if (logging) {
						log.finish();
					}
					return true;
					

					//reset();
				}

			}
		}
		return false;
	}

	void reset() {
		current_gene = 0;
		stepCount = 0;
		setFitness(Float.MAX_VALUE);
		acc_duration = 0;
		individualCount = 0;

	}

	Vec2 getJointPosition() {
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

		return current_gene;
	}

	public int getIndividualCount() {

		return individualCount;
	}

	public void processGene(BipedGene g) {

		for (int i = 0; i < getMotors().size(); i++) {
			RevoluteJoint joint = getMotors().get(i);
			float speed = BipedMotorActions.MAX_SPEED;

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

	public BipedGene getGene(int geneCount) {

		return (BipedGene) ((ListGenome) individual.getGenome()).getGeneList()
				.get(geneCount);
	}

	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}




	public double getFitness() {
		return fitness;
	}




	public void setFitness(double fitness) {
		this.fitness = fitness;
	}

}
