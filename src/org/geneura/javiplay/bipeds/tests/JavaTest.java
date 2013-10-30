package org.geneura.javiplay.bipeds.tests;

import java.util.Date;
import java.util.Iterator;

import javax.swing.JFrame;

import org.geneura.javiplay.ctrnn.CTRNN;
import org.jbox2d.common.Vec2;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.matrix.Matrix;
import org.math.plot.*;


public class JavaTest {
	public static double XOR_INPUT[] = {  0.0, 0.0 , 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0  };

	
 public static  void main(String[] args) {
	 /*
		final FeedforwardNetwork network = new FeedforwardNetwork();
		network.addLayer(new FeedforwardLayer(10));
		network.addLayer(new FeedforwardLayer(2));
		network.reset();
		
		FeedforwardLayer fl =  network.getInputLayer();
		Matrix m = fl.getMatrix();
		System.out.println("matrix size = [" + m.getRows()+", " +m.getCols()+"]");
		
		
		Matrix m2 = new Matrix(11, 2);
		m2.clear();
		m2.set(0, 0, 2);
		fl.setMatrix(m2);
		
		System.out.println("Neural Network Results:");
		double actual[] = network.computeOutputs(XOR_INPUT);
		System.out.println("actual=" + actual[0]);
		
		
		
		System.out.println("matrix size = [" + m.getRows()+", " +m.getCols()+"]");
		for (int i=0;i<m.getRows();i++) {
			for(int j=0;j<m.getCols();j++){
				System.out.print(m.get(i, j)+" ");				
			}
			System.out.println();
		}
		double val = 1/(1+Math.exp(-(m.get(0, 0) + m.get(1, 0) + m.get(2, 0)) ));
		System.out.println("val " +val);
		
		Vec2 v = new Vec2(-0.0f, -1.0f);
		System.out.println("Vector length:" +v.length());
		
		System.out.println("angle:" + normalizeAngle(1.9*Math.PI));
		*/
	 
	
	 
	 // Testing CTRNN, simple neuron
	 //
	 //test1();
	 //test2();
	 //test3();
	 test4();
	 
	 
		
		
 }



private static void test2() {
	double timeStep = 0.1;
	 CTRNN network = new CTRNN(1, timeStep);
	 
	 double[][] weight = {{-20}};
	 double[] input = {40};
	 double[] y0 = {0};
	 double[] tau = {1};
	 network.setState(y0);
	 network.setWeight(weight);
	 network.setTau(tau);
	 
	 network.setInput(input);
	 
	 
	 
	 double simulationTime = 10;
	 int steps = (int) (simulationTime/timeStep);
	 
	 
	 
	 
	 Plot2DPanel plot = new Plot2DPanel();
	 
	 int points = 10;
	 double[] X = new double [points+1];
	 double[] Y = new double [points+1];

	 double min = -20;
	 double max = 40;
	 
	 for (int j=0; j<=points; j++) {
		 
		 network.setState(y0);
		 
		 input[0] = min + ((float)j/points)*(max-min);
		 
		 network.setInput(input);
		 
		 X[j] = input[0];
		 
		 int i=0;	 
		 double t = 0;
		 double[] out = {0};
		 while (t<simulationTime) {
	
			 t+=timeStep;
			 
			 
			 network.step();
			 out = network.getOuput();
			 
			 i++;
		 
		 }
		 
		 Y[j] = out[0];
		 
	 }
	 
	 plot.addLinePlot("my plot", X, Y);
	 
	 JFrame frame = new JFrame("testing");
	 frame.setSize(600, 600);
	 frame.setContentPane(plot);
	 frame.setVisible(true);
}

 
private static void test1() {
	double timeStep = 0.1;
	 CTRNN network = new CTRNN(1, timeStep);
	 
	 double[][] weight = {{-20}};
	 double[] input = {-10};
	 double[] y0 = {-100};
	 double[] tau = {1};
	 network.setState(y0);
	 network.setWeight(weight);
	 network.setTau(tau);
	 
	 network.setInput(input);
	 
	 
	 
	 double simulationTime = 10;
	 int steps = (int) (simulationTime/timeStep);
	 
	 
	 double[] X = new double [steps+1];
	 double[] Y = new double [steps+1];
	 
	 
	 Plot2DPanel plot = new Plot2DPanel();
	 
	 for (int j=0; j<=10; j++) {
		 
		 y0[0] = (j-5)*10;	 
		 network.setState(y0);
		 
		 int i=0;	 
		 double t = 0;
		 while (t<simulationTime) {
	
			 t+=timeStep;
			 X[i] = t;
			 
			 network.step();
			 double[] out = network.getOuput();
			 Y[i] = out[0];
			 i++;
		 
		 }
		 plot.addLinePlot("my plot", X, Y);
	 }
	 
	 
	 JFrame frame = new JFrame("testing");
	 frame.setSize(600, 600);
	 frame.setContentPane(plot);
	 frame.setVisible(true);
}
 
 
 public static double normalizeAngle(double a) {
     return a - (2*Math.PI) * Math.floor(a / (2*Math.PI));
 }



private static void test3() {
	double timeStep = 0.1;
	 CTRNN network = new CTRNN(1, timeStep);
	 
	 double[][] weight = {{20}};
	 double[] input = {40};
	 double[] y0 = {0};
	 double[] tau = {1};
	 network.setState(y0);
	 network.setWeight(weight);
	 network.setTau(tau);
	 
	 network.setInput(input);
	 
	 
	 
	 double simulationTime = 10;
	 int steps = (int) (simulationTime/timeStep);
	 
	 
	 
	 
	 Plot2DPanel plot = new Plot2DPanel();
	 
	 int points = 50;
	 double[] X = new double [points+1];
	 double[] Y = new double [points+1];

	 double min = -30;
	 double max = 10;
	 
	 for (int j=0; j<=points; j++) {
		 
		 network.setState(y0);
		 
		 input[0] = min + ((float)j/points)*(max-min);
		 
		 network.setInput(input);
		 
		 X[j] = input[0];
		 
		 int i=0;	 
		 double t = 0;
		 double[] out = {0};
		 while (t<simulationTime) {
	
			 t+=timeStep;
			 
			 
			 network.step();
			 out = network.getOuput();
			 
			 i++;
		 
		 }
		 
		 Y[j] = out[0];
		 
	 }
	 
	 plot.addLinePlot("my plot", X, Y);
	 
	 JFrame frame = new JFrame("testing");
	 frame.setSize(600, 600);
	 frame.setContentPane(plot);
	 frame.setVisible(true);
}



private static void test4() {
	double timeStep = 0.1;
	 CTRNN network = new CTRNN(1, timeStep);
	 
	 double[][] weight = {{20}};
	 double[] input = {-10};
	 double[] y0 = {0};
	 double[] tau = {1};
	 double[] bias = {-2};
	 
	 network.setState(y0);
	 network.setWeight(weight);
	 network.setTau(tau);
	 network.setBias(bias);
	 
	 
	 network.setInput(input);
	 
	 
	 
	 double simulationTime = 10;
	 int steps = (int) (simulationTime/timeStep);
	 
	 
	 double[] X = new double [steps+1];
	 double[] Y = new double [steps+1];
	 
	 
	 Plot2DPanel plot = new Plot2DPanel();
	 
	 int subplots = 30;
	 int min = -15;
	 int max = 15;
	 
	 
	 for (int j=0; j<subplots; j++) {
		 
		 y0[0] = min + ((float)j/subplots) * (max-min);	 
		 network.setState(y0);
		 
		 int i=0;	 
		 double t = 0;
		 while (t<simulationTime) {
	
			 t+=timeStep;
			 X[i] = t;
			 
			 network.step();
			 double[] out = network.getOuput();
			 Y[i] = out[0];
			 i++;
		 
		 }
		 plot.addLinePlot("my plot", X, Y);
	 }
	 
	 
	 JFrame frame = new JFrame("testing");
	 frame.setSize(600, 600);
	 frame.setContentPane(plot);
	 frame.setVisible(true);
	 
}
 
 
 
}
