package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;

import org.jbox2d.common.Vec2;
import org.jbox2d.testbed.framework.TestbedFrame;
import org.jbox2d.testbed.framework.TestbedModel;
import org.jbox2d.testbed.framework.TestbedPanel;
import org.jbox2d.testbed.framework.TestbedSetting;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;
import org.jbox2d.testbed.framework.TestbedSetting.SettingType;
import org.jbox2d.testbed.framework.j2d.TestPanelJ2D;

import es.ugr.osgiliath.OsgiliathService;
import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.basiccomponents.individuals.DoubleFitness;
import es.ugr.osgiliath.evolutionary.elements.FitnessCalculator;
import es.ugr.osgiliath.evolutionary.individual.Fitness;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class FastBipedFitnessCalculator extends OsgiliathService implements FitnessCalculator {

	FastBipedSimulator simulator;
	
	public FastBipedFitnessCalculator(FastBipedSimulator simulator) {
		this.simulator = simulator;	
		this.simulator.bipedFitness = this;
	}
	
	
	
	@Override
	public synchronized Fitness calculateFitness(Individual ind) {
		
		double score = 0;
		double threshold = (Double) getAlgorithmParameters().getParameter(BipedParameters.HEIGHT_THRESHOLD);
		
		ListGenome genome = (ListGenome) ind.getGenome();
		ArrayList<Gene> gl = genome.getGeneList();		
		
				
		
		score = simulator.reset(gl);
		return new DoubleFitness( score , true);
	}


	@Override
	public ArrayList<Fitness> calculateFitnessForAll(ArrayList<Individual> inds) {
		ArrayList<Fitness> fitList = new ArrayList<Fitness>(inds.size()); 
		for (Individual ind: inds) {
			fitList.add(calculateFitness(ind));
		}
		return fitList;
	}


	public synchronized void wakeUp() {
		notifyAll();
		
	}	


}
