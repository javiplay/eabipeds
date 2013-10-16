package org.geneura.javiplay.ctrnn;

import java.util.Random;

public class CTRNN {
	int n;

	double[] dy;
	double[] y;
	double[] bias;
	double[] input;
	double[] tau;
	double[][] weight;
	double timeStep;

	public CTRNN(int n, double timeStep) {
		this.n = n;
		dy = new double[n];
		y = new double[n];
		bias = new double[n];
		input = new double[n];
		tau = new double[n];
		weight = new double[n][n];
		this.timeStep = timeStep;
	}
	
	
	public void setInput(double[] input) {
		this.input = input.clone();
	}

	
	public void reset(double[] y0) {
		
				
		Random rand = new Random();
		for (int i=0; i<n; i++) {
			tau[i] = 0.017 + rand.nextDouble()*2; // 0.016 < tau <= 1
			y[i] = y0[i];
		}
		
		for (int i=0; i<n; i++) {
			for (int j=0; j<n; j++) {
				weight[j][i] = rand.nextDouble()*20-10; // -10 <= w <= 10
			}	
		}
		
	}

	public void step() {
		for (int i = 0; i < n; i++) {
			double sum = 0;
			for (int j = 0; j < n; j++) {
				sum += weight[j][i] * logistic(y[j] + bias[j]);
			}
			dy[i] = (sum - y[i]) / tau[i] + input[i];
			y[i] += timeStep * dy[i];
		}
	}

	public double logistic(double x) {
		return 1 / (1 + (Math.exp(-x)));
	}
	
	public double[] getOuput() {
		return y.clone();
	}
}
