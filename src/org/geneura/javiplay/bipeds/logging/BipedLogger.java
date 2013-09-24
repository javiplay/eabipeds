package org.geneura.javiplay.bipeds.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import org.geneura.javiplay.bipeds.morphology.BipedFitnessController;
import org.jbox2d.common.Vec2;

public class BipedLogger {

	BipedFitnessController fitnessController;

	/**
	 * @return the fitnessController
	 */
	public BipedFitnessController getFitnessController() {
		return fitnessController;
	}

	/**
	 * @param fitnessController
	 *            the fitnessController to set
	 */
	public void setFitnessController(BipedFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}

	BufferedWriter body_angles_file;

	BufferedWriter file;

	int number;
	public BipedLogger(BipedFitnessController fitnessController) {
		this.fitnessController = fitnessController;
	}

	public void open(int n) {
		this.number = n;

		try {
			body_angles_file = new BufferedWriter(new FileWriter(new File(
					"./octave/body_angles_" + n), false));
			
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

	public void clear() {
		
		close();
		open(number);		
		
	}

}
