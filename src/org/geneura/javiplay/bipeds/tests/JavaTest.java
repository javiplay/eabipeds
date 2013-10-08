package org.geneura.javiplay.bipeds.tests;

import java.util.Date;
import java.util.Iterator;

import org.jbox2d.common.Vec2;

import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardLayer;
import com.heatonresearch.book.introneuralnet.neural.feedforward.FeedforwardNetwork;
import com.heatonresearch.book.introneuralnet.neural.matrix.Matrix;

public class JavaTest {
	public static double XOR_INPUT[] = {  0.0, 0.0 , 1.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0, 1.0  };

 public static  void main(String[] args) {
	 
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
		
		 
		
		
 }
 
 
 public static double normalizeAngle(double a) {
     return a - (2*Math.PI) * Math.floor(a / (2*Math.PI));
 }
 
 
 
}
