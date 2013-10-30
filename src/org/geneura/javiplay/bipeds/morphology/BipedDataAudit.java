package org.geneura.javiplay.bipeds.morphology;

import java.util.ArrayList;

import org.geneura.javiplay.bipeds.ea.UtilParams;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.contacts.ContactEdge;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;

import es.ugr.osgiliath.util.impl.HashMapParameters;

public class BipedDataAudit {

	private int footContactA;

	private int footContactB;
	private ArrayList<Vec2> lastPositions;
	ArrayList<Vec2> maxDistanceFromReference = new ArrayList<Vec2>();

	ArrayList<Vec2> minDistancesToTarget = new ArrayList<Vec2>();
	ArrayList<Vec2> positions;

	Vec2 reference = new Vec2(0.0f, 0.0f);

	Vec2 target = new Vec2(20.0f, 1.0f);

	Vec2 centerOfGravity = new Vec2();

	ArrayList<Vec2> velocities;

	World world;

	private HashMapParameters params;

	public BipedDataAudit(World world, HashMapParameters params) {
		this.params = params;
		this.world = world;
		reset();
	}

	public ArrayList<Body> getBodies() {

		Body b = world.getBodyList();
		ArrayList<Body> bodies = new ArrayList<Body>();

		while (b != null) {
			if (b.getType() == BodyType.DYNAMIC) {
				bodies.add(b);
			}
			b = b.getNext();
		}
		return bodies;
	}

	public int getFootContactA() {
		return footContactA;
	}

	public int getFootContactB() {
		return footContactB;
	}

	public ArrayList<Joint> getJoints() {

		Joint j = world.getJointList();
		ArrayList<Joint> joints = new ArrayList<Joint>();

		while (j != null) {
			if (RevoluteJoint.class.isInstance(j)) {
				joints.add(j);
			}
			j = j.getNext();

		}
		return joints;
	}

	public ArrayList<Vec2> getJointsPos() {
		ArrayList<Vec2> jointsPos = new ArrayList<Vec2>();
		for (Joint motor : getJoints()) {
			Vec2 pos = new Vec2();
			motor.getAnchorA(pos);
			jointsPos.add(pos);
		}
		return jointsPos;
	}

	public ArrayList<Vec2> getLastPositions() {
		return lastPositions;
	}

	/**
	 * @return the maxDistanceFromReference
	 */
	public ArrayList<Vec2> getMaxDistanceFromReference() {
		return maxDistanceFromReference;
	}

	/**
	 * @return the minDistancesToTarget
	 */
	public ArrayList<Vec2> getMinDistancesToTarget() {
		return minDistancesToTarget;
	}

	public ArrayList<Vec2> getPositions() {
		return positions;
	}

	double getPotentialEnergy(Body b) {
		double m = b.getMass();
		double g = world.getGravity().length();
		Vec2 p = b.getWorldCenter();

		double h = p.y;

		double E = m * g * h;
		return E;
	}

	double getKineticEnergy(Body b) {
		double m = b.getMass();
		Vec2 v = b.getLinearVelocityFromLocalPoint(b.getLocalCenter());
		double w = b.getAngularVelocity();
		double I = b.getInertia();
		double E = 0.5 * m * v.lengthSquared() + 0.5 * I * w * w;
		return E;
	}

	public double getTotalEnergy() {
		// calculate potential energy of bodies
		double potentialEnergy = 0;
		double kineticEnergy = 0;
		for (Body b : getBodies()) {
			potentialEnergy += getPotentialEnergy(b);
			kineticEnergy += getKineticEnergy(b);
		}
		return potentialEnergy + kineticEnergy;
	}

	public double getTotalPotentialEnergy() {
		// calculate potential energy of bodies
		double potentialEnergy = 0;

		for (Body b : getBodies()) {
			potentialEnergy += getPotentialEnergy(b);

		}
		return potentialEnergy;
	}

	public double getTotalKineticEnergy() {
		// calculate potential energy of bodies

		double kineticEnergy = 0;
		for (Body b : getBodies()) {
			kineticEnergy += getKineticEnergy(b);
		}
		return kineticEnergy;
	}

	public void reset() {
		int size = world.getJointCount();

		minDistancesToTarget = new ArrayList<Vec2>();
		maxDistanceFromReference = new ArrayList<Vec2>(size);
		positions = new ArrayList<Vec2>(size);
		lastPositions = new ArrayList<Vec2>(size);
		for (int i = 0; i < size; i++) {
			minDistancesToTarget
					.add(new Vec2(Float.MAX_VALUE, Float.MAX_VALUE));
			maxDistanceFromReference.add(new Vec2(0f, 0f));
			positions.add(new Vec2(0f, 0f));
			lastPositions.add(new Vec2(0f, 0f));
		}
	}

	public void save() {

		// world.getJointList();
		// savePositions(getJointsPos());
		// saveContacts(getJoints());

	}

	public Vec2 getCenterOfGravity() {
		Body b = world.getBodyList();
		float massSum = 0;
		Vec2 center = new Vec2();
		while (b != null) {
			if (b.getType() == BodyType.DYNAMIC) {
				Vec2 pos = b.getWorldCenter();
				float mass = b.getMass();
				center.x += mass * pos.x;
				center.y += mass * pos.y;
				massSum += mass;
				b = b.getNext();
			}
		}
		center.x /= massSum;
		center.y /= massSum;

		return center;
	}

	private void saveContacts(ArrayList<Joint> joints) {
		footContactA = 0;
		ContactEdge c = joints.get(0).getBodyA().getContactList();
		while (c != null) {
			if (c.contact.isTouching()) {
				footContactA = 1;
			}
			c = c.next;
		}

		footContactB = 0;
		c = joints.get(0).getBodyB().getContactList();
		while (c != null) {
			if (c.contact.isTouching()) {
				footContactB = 1;
			}
			c = c.next;
		}
	}

	private void savePositions(ArrayList<Vec2> jointsPos) {

		setLastPositions(positions);
		positions = jointsPos;

		int i = 0;
		for (Vec2 jointPos : jointsPos) {
			Vec2 distanceToTarget = target.add(jointPos.negate());
			Vec2 distanceFromReference = jointPos.add(reference.negate());
			minDistancesToTarget.set(i,
					Vec2.min(minDistancesToTarget.get(i), distanceToTarget));
			maxDistanceFromReference.set(i, Vec2.max(
					maxDistanceFromReference.get(i), distanceFromReference));
			i++;
		}
	}

	public void setLastPositions(ArrayList<Vec2> lastJointsPosition) {
		this.lastPositions = lastJointsPosition;
	}

	public void runSinusoidalCPG(double[] parameters, double time) {
		
		
		int jointCount = world.getJointCount();
		double gain = 2;
		if (parameters.length != jointCount * 4) {
			throw new Error("ERROR: number of parameters to the CPG");

		}
		Joint j = world.getJointList(); // joints

		int i = 0; // parameter index

		while (j != null) {

			double ampli = parameters[i++];
			double frequ = parameters[i++];
			double phase = parameters[i++];
			double offset = parameters[i++];

			double angle = ampli * Math.sin(2 * Math.PI * frequ * time + phase)
					+ offset;
			double error = ((RevoluteJoint) j).getJointAngle() - angle;
			((RevoluteJoint) j).setMotorSpeed((float) (-gain * error));

			j = j.getNext();
		}
	}

}
