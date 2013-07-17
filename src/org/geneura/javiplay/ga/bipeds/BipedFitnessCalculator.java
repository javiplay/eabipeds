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

public class BipedFitnessCalculator extends OsgiliathService implements FitnessCalculator {

	public BipedSimulator simulator;
	
	public BipedFitnessCalculator() {
		
	}
	
	
	public Fitness calculateFitness_OLD(Individual ind) {
		
		simulator.reset();
		double score = 0;
		double threshold = (Double) getAlgorithmParameters().getParameter(BipedParameters.HEIGHT_THRESHOLD);
		
		ListGenome genome = (ListGenome) ind.getGenome();
		ArrayList<Gene> gl = genome.getGeneList();		
		
		for (Gene g: gl)
		{
			simulator.processGene((BipedGene) g);
			try {
				Thread.sleep(((BipedGene) g).duration);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if (simulator.getMotorPosition().y < threshold) 
				break;
		}
		score = simulator.getMotorPosition().x;
		return new DoubleFitness( score , true);
	}

	@Override
	public synchronized Fitness calculateFitness(Individual ind) {
		
		double score = 0;
		double threshold = (Double) getAlgorithmParameters().getParameter(BipedParameters.HEIGHT_THRESHOLD);
		
		ListGenome genome = (ListGenome) ind.getGenome();
		ArrayList<Gene> gl = genome.getGeneList();		
		
		simulator.reset(gl);
		// get the time needed to run the individual
		
		int total_time_for_individual = 0;
		for (Gene g: gl)
		{
			total_time_for_individual  += ((BipedGene)g).duration;
		}
		
		int hertzs = simulator.getModel().getSettings().getSetting(TestbedSettings.Hz).value;
		float ticks_to_finish = total_time_for_individual / (1000 / hertzs);
				
		int ticks = simulator.stepCount;
		while (ticks < ticks_to_finish) {				
			try {
				wait();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ticks = simulator.stepCount;
		}
		
		
		score = simulator.getMotorPosition().x;
		System.out.println("Fitness: " + simulator.fitness);
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


	public synchronized void waitSimulator() {
		try {
			wait();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}	


}
