package org.geneura.javiplay.bipeds.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.geneura.javiplay.bipeds.morphology.BehaviorFitnessController;
import org.jbox2d.common.Vec2;

public class BipedLogger {

	BehaviorFitnessController fitnessController;

	/**
	 * @return the fitnessController
	 */
	public BehaviorFitnessController getFitnessController() {
		return fitnessController;
	}

	/**
	 * @param fitnessController
	 *            the fitnessController to set
	 */
	public void setFitnessController(BehaviorFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}

	BufferedWriter body_angles_file;

	BufferedWriter file;

	
	public BipedLogger(BehaviorFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}

	public void open() {
		

		try {
			body_angles_file = new BufferedWriter(new FileWriter(new File(
					"./octave/body_angles"), false));
			
		} catch (IOException e) {

			e.printStackTrace();
		}

	}



	public void writeBodyAngles() {
		try {
			body_angles_file.write(" "
					+ fitnessController.getMotors().get(0).getBodyA()
							.getAngle()
					+ " "
					+ fitnessController.getMotors().get(0).getBodyB()
							.getAngle()
					+ " "
					+ fitnessController.getMotors().get(0).getBodyA()
							.getAngularVelocity()
					+ " "
					+ fitnessController.getMotors().get(0).getBodyB()
							.getAngularVelocity() + " "
					+ fitnessController.getStanceA() + " "
					+ fitnessController.getStanceB() + " "
					+ fitnessController.getCurrentAction().getValue() + " "
					+ fitnessController.velocity.x);
			body_angles_file.newLine();

		} catch (IOException e) {

			e.printStackTrace();
		}

	}


	public void close() {

		try {
			body_angles_file.flush();
			body_angles_file.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	
}
