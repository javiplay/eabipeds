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
import java.util.ArrayList;
import java.util.Properties;

import org.geneura.javiplay.bipeds.ea.BipedInitializer;
import org.geneura.javiplay.bipeds.ea.BipedMutation;
import org.geneura.javiplay.bipeds.ea.BipedParameters;
import org.geneura.javiplay.bipeds.ea.BipedProblem;
import org.geneura.javiplay.bipeds.ea.FastBipedFitnessCalculator;
import org.geneura.javiplay.bipeds.simulators.EABipedViewer;
import org.geneura.javiplay.bipeds.simulators.FastBipedSimulator;

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
import es.ugr.osgiliath.problem.Problem;
import es.ugr.osgiliath.problem.ProblemParameters;
import es.ugr.osgiliath.util.impl.BasicLogger;
import es.ugr.osgiliath.util.impl.HashMapParameters;
import es.ugr.osgiliath.utils.Stopwatch;

public class EABipedsAlgorithmLauncher {
		
		public static void launchAlgorithm(){
			
			
			// PHYSICS SIMULATION
			FastBipedSimulator simulator = new FastBipedSimulator();
			 
				
			
			// Evolutionary Algorithm
			Stopwatch sw = new Stopwatch();
			sw.start();
			
			//Algorithm and parameters
			EvolutionaryAlgorithm algo = new EvolutionaryAlgorithm();
			
			AlgorithmParameters params = new HashMapParameters();
			
			Properties defaultProps = new Properties();
			FileInputStream in;
			try {
				in = new FileInputStream(						
						"bipedparameters.properties");
				defaultProps.load(in);
				in.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
						
			params.setup(defaultProps);
			
			
			Problem problem = new BipedProblem();
			ProblemParameters problemParams = new HashMapParameters();
			problem.setProblemParameters(problemParams);
			
			
			
			
			//FITNESS CALCULATOR
			FitnessCalculator fitnessCalculator = new FastBipedFitnessCalculator(simulator, params);
			

			
			//Population and Initializer
			Population pop = new ListPopulation();	
			
			Initializer init = new BipedInitializer();
			((BipedInitializer) init).setAlgorithmParameters(params);
			((BipedInitializer) init).setProblem(problem);
			((BipedInitializer) init).setFitnessCalculator(fitnessCalculator);
			
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
			((BasicOrderRecombinator) recombinator).setFitnessCalculator(fitnessCalculator);
			((BasicOrderRecombinator) recombinator).setProblem(problem);
			((BasicOrderRecombinator) recombinator).setAlgorithmParameters(params);
			((BasicOrderRecombinator) recombinator).setCrossover(crossover);
			algo.setRecombinator(recombinator);
			
			
			//MUTATION
			BipedMutation mutation = new BipedMutation();
			mutation.setAlgorithmParameters(params);
			
			//MUTATOR
			Mutator mutator = new BasicOrderMutator();
			((BasicOrderMutator) mutator).setFitnessCalculator(fitnessCalculator);
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
			//((NWorstIndividualsReplacer) replacer).setFitnessCalculator(fitnessCalculator);
			algo.setReplacer(replacer);
			
			algo.setLogger(new BasicLogger());
			//problem.getParameters().setup(null);
			sw.stop();
			String time = sw.toString();
			sw.start();
			
			algo.start();
			
			System.out.println("["+algo.getObtainedSolution()+"]");
			Individual solution1 = (Individual) algo.getObtainedSolution();
			sw.stop();
			time=time+":"+sw.toString();
			System.out.println(time);
			System.out.println("SOL: " + solution1.getFitness());
			

			
			System.out.println("SOL AGAIN: " + fitnessCalculator.calculateFitness(solution1));
			System.out.println(simulator.fitnessController.getStepCount());
			/*
			simulator.save();
			
			simulator.initLoaded = true;

			
			stopCriterion.reset();
			algo.start();
			Individual solution2 = (Individual) algo.getObtainedSolution();
			
			System.out.println("["+algo.getObtainedSolution()+"]");
			
			System.out.println("SOL AGAIN: " + fitnessCalculator.calculateFitness(solution2));
			System.out.println(simulator.fitnessConf.getStepCount());
			*/
			EABipedViewer viewer = new EABipedViewer();
			ArrayList<Individual> indlist = new  ArrayList<Individual>();
			indlist.add(solution1);
			//indlist.add(solution2);
			
			
			viewer.setIndividualList(indlist);
			
			viewer.start(false, (Integer) params.getParameter(BipedParameters.GENOME_CYCLES));
			
		}
		
		public static void main(String[] args) {
			
			launchAlgorithm();
			//System.out.println("EXIT");
			//System.exit(0);
			
		}

}
