package org.geneura.javiplay.bipeds.ea;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;

public class BipedPositionFitness {

	ArrayList<Vec2> positions = new ArrayList<Vec2>();
	Vec2 target = new Vec2(20.0f, 1.0f);
	
	float min_distance = Float.MAX_VALUE;
	float max_distance_achieved = 0;
	
	int cycles = 10;
	
	public BipedPositionFitness(int cycles) {
		this.cycles = cycles;
		// TODO Auto-generated constructor stub
	}
	
	float toPoint() {
		
		if (positions.get(positions.size()-1).y < 0.7f) {
			return min_distance*10;
		}
						
		return min_distance;
	}
	
	float toRight() {
									
		return - max_distance_achieved;
	}
	
	float toSpeedCycling() {
		
		return 0;
		
	}


	public void addPosition(Vec2 motor_pos) {
		// add to array
		positions.add(motor_pos);
		
		// store minimum distance to target
		Vec2 distance_vec = target.add(motor_pos.negate()); 
		min_distance = Math.min(min_distance, distance_vec.length());
		
		// store maximum distance achieved
		max_distance_achieved = Math.max(motor_pos.x, max_distance_achieved); 
		
		
	}


	public void reset() {
		min_distance = Float.MAX_VALUE;
		max_distance_achieved = 0;
		positions = new ArrayList<Vec2>();		
	}
	
	

}
