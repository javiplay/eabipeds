package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.behavior.BehaviorFitnessCalculator;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorGene;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorInitializer;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorParameters;
import org.geneura.javiplay.bipeds.logging.BipedLogger;
import org.geneura.javiplay.bipeds.simulators.FastBipedSimulator;
import org.geneura.javiplay.bipeds.simulators.TestbedBehaviorSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class FastRandomIndividualsTest {

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
		BehaviorInitializer bi = new BehaviorInitializer();
		bi.setAlgorithmParameters(params);
		ArrayList<Individual> inds = bi.initializeIndividuals(10);

		FastBipedSimulator simulator2 = new FastBipedSimulator();		
		BipedLogger logger = new BipedLogger(simulator2.fitnessController);
		simulator2.setLogger(logger);
		
		BehaviorFitnessCalculator fitnessCalculator = new BehaviorFitnessCalculator();
		
		for (Individual ind: inds) {
			fitnessCalculator.indNumber++;
			fitnessCalculator.calculateFitness(ind);
		}

	}

}
