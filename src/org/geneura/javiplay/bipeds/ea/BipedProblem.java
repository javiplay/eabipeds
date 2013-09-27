package org.geneura.javiplay.bipeds.ea;



import org.geneura.javiplay.bipeds.simulators.Simulator;

import es.ugr.osgiliath.problem.InputData;
import es.ugr.osgiliath.problem.Problem;
import es.ugr.osgiliath.problem.ProblemParameters;

public class BipedProblem implements Problem{
	

	
	private Simulator simulator;

	/**
	 * @return the simulator
	 */
	public Simulator getSimulator() {
		return simulator;
	}

	/**
	 * @param simulator the simulator to set
	 */
	public void setSimulator(Simulator simulator) {
		this.simulator = simulator;
	}

	@Override
	public void setInputData(InputData data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setProblemParameters(ProblemParameters problemParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void unsetProblemParameters(ProblemParameters problemParams) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ProblemParameters getParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InputData getInputData() {
		// TODO Auto-generated method stub
		return null;
	}
	


}
