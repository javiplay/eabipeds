package org.geneura.javiplay.bipeds.tests;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.geneura.javiplay.bipeds.simulators.EABipedViewer;

import es.ugr.osgiliath.algorithms.AlgorithmParameters;
import es.ugr.osgiliath.evolutionary.individual.Individual;
import es.ugr.osgiliath.util.impl.HashMapParameters;

public class BipedTest {
	
	
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

		EABipedViewer viewer = new EABipedViewer();
		
		AlgorithmParameters params = LoadParameters("bipedparameters.properties");
		
		viewer.setAlgorithmParameters(params);
		
		
		
		
		ArrayList<Individual> individuals = new ArrayList<Individual>();
		
		for (int i=0; i< 100; i++) {
			Individual ind = viewer.randomIndividual();
			individuals.add(ind);
		}
				
		viewer.setIndividualList(individuals);
		viewer.start(false, 1);
	}

}
