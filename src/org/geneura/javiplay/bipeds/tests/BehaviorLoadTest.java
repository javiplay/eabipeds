package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorFitnessCalculator;
import org.geneura.javiplay.bipeds.ea.linearcontroller.LinearControllerFitnessCalculator;
import org.geneura.javiplay.bipeds.simulators.WindowedSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.problem.ProblemParameters;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class BehaviorLoadTest {



	public static void main(String[] args) {
		
		ObjectInputStream ois;
		Individual solutionLoaded = null;
		
		try {
			ois = new ObjectInputStream(new FileInputStream("behavior.solution"));
			solutionLoaded = (Individual) ois.readObject();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// PHYSICS SIMULATION
		TestbedModel model = new TestbedModel();
		model.addCategory("Loaded Linear"); // add a category

		WindowedSimulator winSimulator = new WindowedSimulator();
		
		model.addTest(winSimulator);
		
		TestbedPanel panel = new TestPanelJ2D(model);
		JFrame testbed = new TestbedFrame(model, panel);

		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		
		HashMapParameters params = UtilParams.LoadParamsFromFile("behaviorparameters.properties");		
		BipedProblem problem = new BipedProblem();
		problem.setProblemParameters(params);
		problem.setSimulator(winSimulator);		
		
		BehaviorFitnessCalculator fc = new BehaviorFitnessCalculator();
		fc.setProblem(problem);
		winSimulator.setFitnessStepCalculator(fc);
		fc.calculateFitness(solutionLoaded);		
		

	}

}
