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
package org.geneura.javiplay.bipeds.simulators;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.swing.JFrame;

import org.geneura.javiplay.bipeds.ea.BipedGene;
import org.geneura.javiplay.bipeds.ea.BipedParameters;
import org.geneura.javiplay.bipeds.morphology.MotorActions;
import org.geneura.javiplay.bipeds.tests.BipedTest;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.BasicIndividual;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class EABipedViewer extends OsgiliathService {

	private Individual individual;
	private ArrayList<Individual> individuals;

	public void start(boolean init, int cycles) {

		// PHYSICS SIMULATION
		TestbedModel model = new TestbedModel();
		model.addCategory("My Super Tests"); // add a category
				
		
		BipedSimulator simulator = new BipedSimulator(individuals, cycles);
		
			
		model.addTest(simulator);
		
		TestbedPanel panel = new TestPanelJ2D(model);
		JFrame testbed = new TestbedFrame(model, panel); 
		
		testbed.setVisible(true);
		testbed.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}

	public Individual randomIndividual() {

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

		EABipedViewer viewer = new EABipedViewer();
		
		AlgorithmParameters params = BipedTest.LoadParameters("bipedparameters.properties");
		viewer.setAlgorithmParameters(params);
		
		ArrayList<Individual> individuals = new ArrayList<Individual>();
		Individual ind = viewer.randomIndividual();
		individuals.add(ind);
		individuals.add(ind);
		
		viewer.setIndividualList(individuals);
		viewer.start(false, 1);

	}

	public void setIndividual(Individual ind) {
		this.individual = ind;

	}
	
	public void setIndividualList(ArrayList<Individual> inds) {
		this.individuals = inds;
	}

}
