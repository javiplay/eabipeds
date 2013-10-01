/*
 * IntelligentRandomManager.java
 * 
 * Copyright (c) 2013, Pablo Garcia-Sanchez. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 * 
 * Contributors:
 * Daniel Calandria
 * Ana Belen Pelegrina
 */
package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorFitnessCalculator;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorInitializer;
import org.geneura.javiplay.bipeds.ea.behavior.BehaviorMutation;
import org.geneura.javiplay.bipeds.simulators.FastBipedSimulator;
import org.geneura.javiplay.bipeds.simulators.FitnessStepCalculator;
import org.geneura.javiplay.bipeds.simulators.WindowedSimulator;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.osgiliath.evolutionary.basicimplementations.combinators.BasicOrderRecombinator;
import es.osgiliath.evolutionary.basicimplementations.mutators.BasicOrderMutator;
import es.osgiliath.evolutionary.basicimplementations.populations.ListPopulation;
import es.osgiliath.evolutionary.basicimplementations.replacers.NWorstIndividualsReplacer;
import es.osgiliath.evolutionary.basicimplementations.selectors.DeterministicTournamentSelection;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.EvolutionaryAlgorithm;
import es.ugr.osgiliath.evolutionary.basiccomponents.operators.TPXListCrossover;
import es.ugr.osgiliath.evolutionary.basicimplementations.stopcriterions.NGenerationsStopCriterion;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.elements.Mutator;
import es.ugr.osgiliath.evolutionary.elements.ParentSelector;
import es.ugr.osgiliath.evolutionary.elements.Population;
import es.ugr.osgiliath.evolutionary.elements.Replacer;
import es.ugr.osgiliath.evolutionary.elements.StopCriterion;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.evolutionary.individual.Initializer;
import es.ugr.osgiliath.problem.ProblemParameters;
import es.ugr.osgiliath.util.impl.BasicLogger;
import es.ugr.osgiliath.util.impl.HashMapParameters;
import es.ugr.osgiliath.utils.Stopwatch;

public class BehaviorTest {
		
		public static void launchAlgorithm(){
			
			
			// PHYSICS SIMULATION
			FastBipedSimulator simulator = new FastBipedSimulator();

			Stopwatch sw = new Stopwatch();
			sw.start();
			
			EvolutionaryAlgorithm algo = new EvolutionaryAlgorithm();
			HashMapParameters params = UtilParams.LoadParamsFromFile("behaviorparameters.properties");
	
			
			BipedProblem problem = new BipedProblem();
			problem.setProblemParameters(params);
			problem.setSimulator(simulator);
			
			
			//FITNESS CALCULATOR
			FitnessCalculator behaviorFitnessCalculator = (FitnessCalculator) new BehaviorFitnessCalculator();
			((BehaviorFitnessCalculator)behaviorFitnessCalculator).setProblem(problem);
			((BehaviorFitnessCalculator)behaviorFitnessCalculator).setAlgorithmParameters(params);
			simulator.setFitnessStepCalculator((FitnessStepCalculator) behaviorFitnessCalculator);

			
			//Population and Initializer
			Population pop = new ListPopulation();	
			
			Initializer init = new BehaviorInitializer();
			((BehaviorInitializer) init).setAlgorithmParameters(params);
			((BehaviorInitializer) init).setProblem(problem);
			((BehaviorInitializer) init).setFitnessCalculator(behaviorFitnessCalculator);
			
			((ListPopulation) pop).setInitializer(init);
			((ListPopulation) pop).setAlgorithmParameters(params);
			((ListPopulation) pop).setProblem(problem);		
			algo.setPopulation(pop);
			
			
			//PARENT SELECTOR
			ParentSelector parentSelector = new DeterministicTournamentSelection();
			
			((DeterministicTournamentSelection)parentSelector).setAlgorithmParameters(params);
			((DeterministicTournamentSelection)parentSelector).setProblem(problem);
			algo.setParentSelector(parentSelector);
			
			//CROSSOVER
			TPXListCrossover crossover = new TPXListCrossover();
				
			//RECOMBINATOR
			BasicOrderRecombinator recombinator = new BasicOrderRecombinator();
			((BasicOrderRecombinator) recombinator).setFitnessCalculator(behaviorFitnessCalculator);
			((BasicOrderRecombinator) recombinator).setProblem(problem);
			((BasicOrderRecombinator) recombinator).setAlgorithmParameters(params);
			((BasicOrderRecombinator) recombinator).setCrossover(crossover);
			algo.setRecombinator(recombinator);
			
			
			//MUTATION
			BehaviorMutation mutation = new BehaviorMutation();
			mutation.setAlgorithmParameters(params);
			
			//MUTATOR
			Mutator mutator = new BasicOrderMutator();
			((BasicOrderMutator) mutator).setFitnessCalculator(behaviorFitnessCalculator);
			((BasicOrderMutator) mutator).setAlgorithmParameters(params);
			((BasicOrderMutator) mutator).setMutation(mutation);
			algo.setMutator(mutator);
			
			//STOP CRITERION
			StopCriterion stopCriterion = new NGenerationsStopCriterion();
			((NGenerationsStopCriterion) stopCriterion).setAlgorithmParameters(params);
			((NGenerationsStopCriterion) stopCriterion).setProblem(problem);
			algo.setStopCriterion(stopCriterion);
			
			//REPLACER
			Replacer replacer = new NWorstIndividualsReplacer();			
			algo.setReplacer(replacer);
			
			algo.setLogger(new BasicLogger());

			sw.stop();
			String time = sw.toString();
			sw.start();
			
			// START
			algo.start();
			
			System.out.println("["+algo.getObtainedSolution()+"]");
			
			Individual solution = (Individual) algo.getObtainedSolution();
			
			try {
				ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("behavior.solution"));
				oos.writeObject(solution);
				oos.close();
			} catch (FileNotFoundException e) {
				
				e.printStackTrace();
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			
			
			sw.stop();
			time=time+":"+sw.toString();
			
			System.out.println(time);
			
			System.out.println("Fitness solution in background: " + solution.getFitness());						
			
			
			// PHYSICS SIMULATION
			TestbedModel model = new TestbedModel();
			model.addCategory("Behavior Result"); // add a category
			
			WindowedSimulator winSimulator = new WindowedSimulator();			
			
			model.addTest(winSimulator);
			
			TestbedPanel panel = new TestPanelJ2D(model);
			JFrame testbed = new TestbedFrame(model, panel); 
			
			testbed.setVisible(true);
			testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			problem.setSimulator(winSimulator);
			
			winSimulator.setFitnessStepCalculator((FitnessStepCalculator) behaviorFitnessCalculator);
			
			behaviorFitnessCalculator.calculateFitness(solution);
			
			
			System.out.println("Fitness solution in testbed window: " + solution.getFitness());
			
			
			
			
			
		}
		
		
		public static void main(String[] args) {
			
			launchAlgorithm();
			System.out.println("EXIT");
			
			
		}

}
