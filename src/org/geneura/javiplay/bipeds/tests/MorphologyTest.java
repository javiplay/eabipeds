package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.simulators.InteractiveBipedSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class MorphologyTest {
	
	
	static AlgorithmParameters LoadParameters(String filename) {
		AlgorithmParameters params = new HashMapParameters();
		
		Properties defaultProps = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(filename);
			defaultProps.load(in);
			in.close();
		} catch (Exception e) {			
			e.printStackTrace();
		}
					
		params.setup(defaultProps);
		return params;
		
	}
	
	public static void main(String[] args) {

		
		// PHYSICS SIMULATION
		TestbedModel model = new TestbedModel();
		model.addCategory("My Tests"); // add a category
		
		InteractiveBipedSimulator simulator = new InteractiveBipedSimulator();
			
		model.addTest(simulator);
		
		TestbedPanel panel = new TestPanelJ2D(model);
		
		JFrame testbed = new TestbedFrame(model, panel); 
		
		testbed.setVisible(true);
		
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			
		
	}
	
	
	 

}
