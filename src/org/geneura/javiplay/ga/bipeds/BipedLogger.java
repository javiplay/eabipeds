package org.geneura.javiplay.ga.bipeds;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jbox2d.common.Vec2;

public class BipedLogger {
	
	BipedFitnessConfig fitnessConf;
	
	BufferedWriter body_angles_file;
	BufferedWriter joint_pos_file;
	BufferedWriter actions_angles_file;
	BufferedWriter file;
	
	public BipedLogger(BipedFitnessConfig fitnessConf) {
		this.fitnessConf = fitnessConf;
		try {
			body_angles_file = new BufferedWriter(new FileWriter(
					new File("./octave/body_angles"), false));
			actions_angles_file = new BufferedWriter(new FileWriter(
					new File("./octave/action_angles"), false));	
			joint_pos_file = new BufferedWriter(new FileWriter(
					new File("./octave/joint_pos"), false));
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
		
	}


	private boolean write_finished = false;

	public void writeBodyAngles() {
		if (!write_finished) {
			try {				
				body_angles_file.write(" " + fitnessConf.getMotors().get(0).getBodyA().getAngle()
						+ " " + fitnessConf.getMotors().get(0).getBodyB().getAngle());
				body_angles_file.newLine();
				joint_pos_file = new BufferedWriter(new FileWriter(
						new File("./octave/joint_pos"), false));
			} catch (IOException e) {
				
				e.printStackTrace();
			}
		}

	}
		
	public void writeActionAngles() {
		if (!write_finished) {
			try {
			int geneCount = fitnessConf.getCurrentGeneCount(); 
			if ( geneCount > 1 ) {
				if (fitnessConf.getGene(geneCount-1).actions.get(0) != fitnessConf.getGene(geneCount).actions.get(0)) {
					actions_angles_file.write(" "
							+ fitnessConf.getMotors().get(0).getBodyA().getAngle() + " "
							+ fitnessConf.getMotors().get(0).getBodyB().getAngle());
				actions_angles_file.newLine();
				}
			}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	
	}
			
	public void writeJointPosition() {
		if (!write_finished) {
			try {
								
				joint_pos_file.write(" " + fitnessConf.getJointPosition().x + " " + fitnessConf.getJointPosition().y);
				joint_pos_file.newLine();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
	}

	public void finish() {

		try {
			body_angles_file.close();
			joint_pos_file.close();
			actions_angles_file.close();
			write_finished = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
