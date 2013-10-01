package org.geneura.javiplay.bipeds.morphology;

import java.util.ArrayList;

import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.RevoluteJoint;

public class BipedDataAudit {

	ArrayList<Vec2> positions = new ArrayList<Vec2>();
	private ArrayList<Vec2> lastPositions = new ArrayList<Vec2>();
	ArrayList<Vec2> velocities = new ArrayList<Vec2>();
	
	Vec2 target = new Vec2(20.0f, 1.0f);
	Vec2 reference = new Vec2(0.0f, 0.0f);
	
	private int stanceA;
	/**
	 * @return the stanceA
	 */
	public int getStanceA() {
		return stanceA;
	}

	/**
	 * @param stanceA the stanceA to set
	 */
	public void setStanceA(int stanceA) {
		this.stanceA = stanceA;
	}

	private int stanceB;
	
	

	
	
	/**
	 * @return the stanceB
	 */
	public int getStanceB() {
		return stanceB;
	}

	/**
	 * @param stanceB the stanceB to set
	 */
	public void setStanceB(int stanceB) {
		this.stanceB = stanceB;
	}

	ArrayList<Vec2> minDistancesToTarget = new ArrayList<Vec2>();
	/**
	 * @return the minDistancesToTarget
	 */
	public ArrayList<Vec2> getMinDistancesToTarget() {
		return minDistancesToTarget;
	}

	ArrayList<Vec2> maxDistanceFromReference = new ArrayList<Vec2>();
	
	/**
	 * @return the maxDistanceFromReference
	 */
	public ArrayList<Vec2> getMaxDistanceFromReference() {
		return maxDistanceFromReference;
	}

	public ArrayList<Vec2> getLastPositions() {
		return lastPositions;
	}

	public void setLastPositions(ArrayList<Vec2> lastJointsPosition) {
		this.lastPositions = lastJointsPosition;
	}

	
	
	float toSpeedCycling() {
		
		return 0;
		
	}


	private void savePositions(ArrayList<Vec2> jointsPos) {
		
		setLastPositions(positions);
		positions = jointsPos;
		
		int i=0;
		for (Vec2 jointPos: jointsPos) {
			Vec2 distanceToTarget = target.add(jointPos.negate());
			Vec2 distanceFromReference = jointPos.add(reference.negate());
			minDistancesToTarget.set(i, Vec2.min(minDistancesToTarget.get(i), distanceToTarget));
			maxDistanceFromReference.set(i, Vec2.max(maxDistanceFromReference.get(i), distanceFromReference)); 
			i++;
		}
	}


	public void reset(int size) {
		
		minDistancesToTarget = new ArrayList<Vec2>();
		maxDistanceFromReference = new ArrayList<Vec2>(size);
		positions = new ArrayList<Vec2>(size);
		lastPositions = new ArrayList<Vec2>(size);
		for (int i=0;i<size;i++) {
			minDistancesToTarget.add(new Vec2(Float.MAX_VALUE, Float.MAX_VALUE));
			maxDistanceFromReference.add(new Vec2(0f,0f));
			positions.add(new Vec2(0f,0f));
			lastPositions.add(new Vec2(0f,0f));							
		}		
	}

	public void save(ArrayList<RevoluteJoint> motors) {
		ArrayList<Vec2>positions = new ArrayList<Vec2>();
		for (RevoluteJoint joint: motors) {
			Vec2 p = new Vec2();
			joint.getAnchorA(p);
			positions.add(p);	
		}
		savePositions(positions);
		
		saveContacts(motors);
		
	}

	private void saveContacts(ArrayList<RevoluteJoint> motors) {
		stanceA = 0;
		ContactEdge c = motors.get(0).getBodyB().getContactList();
		while (c != null) {
			if (c.contact.isTouching()) {
				stanceA = 1;
			}
			c = c.next;
		}
		
		stanceB = 0;
		c = motors.get(1).getBodyB().getContactList();
		while (c != null) {
			if (c.contact.isTouching()) {
				stanceB = 1;
			}
			c = c.next;
		}
	}

	public ArrayList<Vec2> getPositions() {
		return positions;		
	}
	

}
