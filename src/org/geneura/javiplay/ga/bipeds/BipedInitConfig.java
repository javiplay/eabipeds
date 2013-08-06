package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class BipedInitConfig {

	private ArrayList<RevoluteJoint> motors;	
	private float bodyDensity = 1.0f;
	
	public BipedInitConfig(World w) {
		// Ground
		BodyDef bdGround = new BodyDef();
		Body ground = w.createBody(bdGround);
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-40.0f, 0.2f), new Vec2(40.0f, 0.2f));
		ground.createFixture(shape, 0.0f);

		// Leg A
		// -----

		// Shapes
		CircleShape footShapeA = new CircleShape();
		footShapeA.m_radius = 0.05f;

		PolygonShape legShapeA = new PolygonShape();
		legShapeA.setAsBox(0.05f, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootA = new FixtureDef();
		fdFootA.shape = footShapeA;
		fdFootA.density = bodyDensity;
		fdFootA.friction = 1.0f;

		FixtureDef fdLegA = new FixtureDef();
		fdLegA.shape = legShapeA;
		fdLegA.density = bodyDensity;

		// The leg body
		BodyDef bdA = new BodyDef();
		bdA.type = BodyType.DYNAMIC;
		bdA.position.set(0.0f, 0.5f);
		Body bA = w.createBody(bdA);
		bA.createFixture(fdFootA);
		bA.createFixture(fdLegA);

		// Leg B
		// -----

		// Shapes
		CircleShape footShapeB = new CircleShape();
		footShapeB.m_radius = 0.05f;

		PolygonShape legShapeB = new PolygonShape();
		legShapeB.setAsBox(0.05f, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootB = new FixtureDef();
		fdFootB.shape = footShapeB;
		fdFootB.density = bodyDensity;
		fdFootB.friction = 1.0f;

		FixtureDef fdLegB = new FixtureDef();
		fdLegB.shape = legShapeB;
		fdLegB.density = bodyDensity;

		// The leg body
		BodyDef bdB = new BodyDef();
		bdB.type = BodyType.DYNAMIC;
		bdB.position.set(0.0f, 0.5f);
		Body bB = w.createBody(bdB);
		bB.createFixture(fdFootB);
		bB.createFixture(fdLegB);

		// Hinge
		// -----
		RevoluteJointDef rjdLegs = new RevoluteJointDef();
		rjdLegs.initialize(bA, bB, new Vec2(0.0f, 1.3f));

		rjdLegs.motorSpeed = -2.0f * MathUtils.PI;
		
		rjdLegs.maxMotorTorque = 10000.0f;
		rjdLegs.enableMotor = false;

		rjdLegs.lowerAngle = -0.25f * MathUtils.PI;
		rjdLegs.upperAngle = 0.5f * MathUtils.PI;
		rjdLegs.enableLimit = false;

		// rjdLegs.collideConnected = true;
		RevoluteJoint m_joint = (RevoluteJoint) w.createJoint(rjdLegs);
		setMotors(new ArrayList<RevoluteJoint>(1));
		getMotors().add(m_joint);
	}

	public ArrayList<RevoluteJoint> getMotors() {
		return motors;
	}

	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}
}
