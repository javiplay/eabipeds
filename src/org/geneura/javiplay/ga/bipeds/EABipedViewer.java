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
package org.geneura.javiplay.ga.bipeds;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedSetting;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.TestbedSetting.SettingType;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.osgiliath.evolutionary.basicimplementations.combinators.BasicOrderRecombinator;
import es.osgiliath.evolutionary.basicimplementations.mutators.BasicOrderMutator;
import es.osgiliath.evolutionary.basicimplementations.populations.ListPopulation;
import es.osgiliath.evolutionary.basicimplementations.selectors.DeterministicTournamentSelection;
import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.basiccomponents.operators.TPXListCrossover;
import es.ugr.osgiliath.evolutionary.basicimplementations.stopcriterions.NGenerationsStopCriterion;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.EvolutionaryAlgorithm;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.elements.Mutator;
import es.ugr.osgiliath.evolutionary.elements.ParentSelector;
import es.ugr.osgiliath.evolutionary.elements.Population;
import es.ugr.osgiliath.evolutionary.elements.Recombinator;
import es.ugr.osgiliath.evolutionary.elements.Replacer;
import es.ugr.osgiliath.evolutionary.elements.StopCriterion;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.evolutionary.individual.Initializer;

import es.ugr.osgiliath.problem.Problem;
import es.ugr.osgiliath.problem.ProblemParameters;

import es.ugr.osgiliath.util.impl.BasicLogger;
import es.ugr.osgiliath.util.impl.HashMapParameters;
import es.ugr.osgiliath.utils.Stopwatch;

public class EABipedViewer extends OsgiliathService {

	private Individual individual;

	public void start() {

		// PHYSICS SIMULATION
		TestbedModel model = new TestbedModel();
		model.addCategory("My Super Tests"); // add a category
		
		
		BipedSimulator simulator = new BipedSimulator();
		simulator.reset(((ListGenome)individual.getGenome()).getGeneList());
		
		model.addTest(simulator);
		// add our custom setting "My Range Setting", with a default value of
		// 10, between 0 and 20

		TestbedPanel panel = new TestPanelJ2D(model); // create our testbed
														// panel

		JFrame testbed = new TestbedFrame(model, panel); // put both into our
															// testbed frame

		// etc
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		
		
		AlgorithmParameters params = new HashMapParameters();

		Properties defaultProps = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(
					"/home/javier/workspace/EABipeds/bipedparameters.properties");
			defaultProps.load(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		params.setup(defaultProps);

		

		
		this.setAlgorithmParameters(params);

		
		
		

	}

	public Individual loadIndividual() {

		int genome_size = (Integer) getAlgorithmParameters().getParameter(
				BipedParameters.GENOME_SIZE);
		int max_possible_duration = (Integer) getAlgorithmParameters()
				.getParameter(BipedParameters.MAX_ACTION_DURATION);
		int motors = (Integer) getAlgorithmParameters().getParameter(
				BipedParameters.MOTORS);

		BasicIndividual individual = new BasicIndividual();

		ListGenome genome = new ListGenome();
		ArrayList<Gene> genes = new ArrayList<Gene>(genome_size);
		for (int j = 0; j < genome_size; j++) {

			java.util.Random rand = new java.util.Random();
			// GENE CONFIG

			ArrayList<MotorActions> actions = new ArrayList<MotorActions>(
					motors);
			for (int k = 0; k < motors; k++) {
				int action = rand.nextInt(MotorActions.values().length);
				actions.add(MotorActions.values()[action]);
			}

			int duration = rand.nextInt(max_possible_duration);

			BipedGene gene = new BipedGene(actions, duration);
			genes.add(gene);
		}
		genome.setGenes(genes);

		individual.setGenome(genome);

		return individual;
	}

	public static void main(String[] args) {

		EABipedViewer ea = new EABipedViewer();
		ea.start();

	}

	public void setIndividual(Individual ind) {
		this.individual = ind;

	}

}
