package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.BipedGene;
import org.geneura.javiplay.bipeds.ea.BipedInitializer;
import org.geneura.javiplay.bipeds.ea.BipedParameters;
import org.geneura.javiplay.bipeds.simulators.TestbedBipedSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class RandomIndividualsTest {

	public static AlgorithmParameters LoadParameters(String filename) {
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

		AlgorithmParameters params = LoadParameters("random_behaviour.properties");

		ArrayList<Individual> individuals = new ArrayList<Individual>();
		BipedInitializer bi = new BipedInitializer();
		bi.setAlgorithmParameters(params);

		
		for (int i = 0; i < 1; i++) {
			individuals = bi.initializeIndividuals(1);			
		}

		// PHYSICS SIMULATION
		TestbedModel model = new TestbedModel();
		model.addCategory("Random"); // add a category

		TestbedBipedSimulator simulator2 = new TestbedBipedSimulator(
				individuals,
				(Integer) params.getParameter(BipedParameters.GENOME_CYCLES));

		model.addTest(simulator2);

		TestbedPanel panel = new TestPanelJ2D(model);
		JFrame testbed = new TestbedFrame(model, panel);

		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

}
