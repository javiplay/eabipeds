package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.simulators.CTRNNPendulumSimulator;
import org.geneura.javiplay.bipeds.simulators.InteractiveBipedSimulator;
import org.geneura.javiplay.bipeds.simulators.SimpleCompassWavesSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class WavesTests {
	
	
	public static void main(String[] args) {

		
		// PHYSICS SIMULATION
		
		TestbedModel model = new TestbedModel();
		model.addCategory("Waves"); // add a category
		
		SimpleCompassWavesSimulator simulator = new SimpleCompassWavesSimulator();
		
		HashMapParameters params = UtilParams.LoadParamsFromFile("wavetests.properties");
		
		simulator.setParams(params);
		
		model.addTest(simulator);
		
		TestbedPanel panel = new TestPanelJ2D(model);
		
		JFrame testbed = new TestbedFrame(model, panel); 
		
		testbed.setVisible(true);
		
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
			
		
	}
	
	
	 

}
