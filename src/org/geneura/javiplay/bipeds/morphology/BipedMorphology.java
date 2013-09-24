package org.geneura.javiplay.bipeds.morphology;

import java.util.ArrayList;

import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.World;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class BipedMorphology {

	private ArrayList<RevoluteJoint> motors;
	

	public void ground(World w) {
		// Ground
		BodyDef bdGround = new BodyDef();
		
		
		Body ground = w.createBody(bdGround);
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-1000.0f, 0.0f), new Vec2(1000.0f, -0.0f));
		ground.createFixture(shape, 0.0f);
		

	}
	
	public void guide(World w) {
		
		//Shape
		PolygonShape guideShape = new PolygonShape();
		guideShape.setAsEdge(new Vec2(-1000.0f, 0.6f), new Vec2(1000.0f, 0.6f));
		//Fixture
		FixtureDef fixDefGuide = new FixtureDef();
		fixDefGuide.shape = guideShape;
		fixDefGuide.filter.groupIndex = -1;
		//Body
		BodyDef bodyDefGuide = new BodyDef();
		bodyDefGuide.type = BodyType.STATIC;
		Body bodyGuide = w.createBody(bodyDefGuide);
		bodyGuide.createFixture(fixDefGuide);
		
	}

	
	public void compassLike(World w) {
		
		// Parameters		
		float footRadius = 0.05f;
		float legRadius = 0.05f;
		float bodyDensity = 1.0f;
		
		// Leg A
		// -----

		// Shapes
		CircleShape footShapeA = new CircleShape();
		footShapeA.m_radius = footRadius;

		PolygonShape legShapeA = new PolygonShape();
		legShapeA.setAsBox(legRadius, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootA = new FixtureDef();
		fdFootA.shape = footShapeA;
		fdFootA.density = bodyDensity;
		fdFootA.friction = 1.0f;
		fdFootA.filter.groupIndex = -1;
		

		FixtureDef fdLegA = new FixtureDef();
		fdLegA.shape = legShapeA;
		fdLegA.density = bodyDensity;
		fdLegA.filter.groupIndex = -1;

		// The leg body
		BodyDef bdA = new BodyDef();
		bdA.type = BodyType.DYNAMIC;
		bdA.position.set(0.0f, 0.05f);
		Body bA = w.createBody(bdA);
		bA.createFixture(fdLegA);
		bA.createFixture(fdFootA);
		
		// Leg B
		// -----

		// Shapes
		CircleShape footShapeB = new CircleShape();
		footShapeB.m_radius = footRadius;

		PolygonShape legShapeB = new PolygonShape();
		legShapeB.setAsBox(legRadius, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootB = new FixtureDef();
		fdFootB.shape = footShapeB;
		fdFootB.density = bodyDensity;
		fdFootB.friction = 1.0f;
		fdFootB.filter.groupIndex = -1;

		FixtureDef fdLegB = new FixtureDef();
		fdLegB.shape = legShapeB;
		fdLegB.density = bodyDensity;
		fdLegB.filter.groupIndex = -1;
		// The leg body
		BodyDef bdB = new BodyDef();
		bdB.type = BodyType.DYNAMIC;
		bdB.position.set(0.0f, 0.05f);
		Body bB = w.createBody(bdB);
		bB.createFixture(fdFootB);
		bB.createFixture(fdLegB);

		// Joint
		// -----
		RevoluteJointDef rjdLegs = new RevoluteJointDef();
		rjdLegs.initialize(bA, bB, new Vec2(0.0f, 0.85f));

		rjdLegs.motorSpeed = -2.0f * MathUtils.PI;

		rjdLegs.maxMotorTorque = 10000.0f;
		rjdLegs.enableMotor = false;

		rjdLegs.lowerAngle = -0.75f * MathUtils.PI;
		rjdLegs.upperAngle = 0.75f * MathUtils.PI;
		rjdLegs.enableLimit = false;

		// rjdLegs.collideConnected = true;
		RevoluteJoint m_joint = (RevoluteJoint) w.createJoint(rjdLegs);
		setMotors(new ArrayList<RevoluteJoint>(1));
		getMotors().add(m_joint);
		

	}
	
	
	
public void compassLikeWithHip(World w) {
		
		// Parameters		
		float footRadius = 0.05f;
		float hipRadius = 0.05f;
		
		float legRadius = 0.05f;
		float bodyDensity = 1.0f;
		
		// Leg A
		// -----

		// Shapes
		CircleShape footShapeA = new CircleShape();
		footShapeA.m_radius = footRadius;

		PolygonShape legShapeA = new PolygonShape();
		legShapeA.setAsBox(legRadius, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootA = new FixtureDef();
		fdFootA.shape = footShapeA;
		fdFootA.density = bodyDensity;
		fdFootA.friction = 1.0f;
		fdFootA.filter.groupIndex = -1;
		

		FixtureDef fdLegA = new FixtureDef();
		fdLegA.shape = legShapeA;
		fdLegA.density = bodyDensity;
		fdLegA.filter.groupIndex = -1;

		// The leg body
		BodyDef bdA = new BodyDef();
		bdA.type = BodyType.DYNAMIC;
		bdA.position.set(0.0f, 0.05f);
		Body bA = w.createBody(bdA);
		bA.createFixture(fdLegA);
		bA.createFixture(fdFootA);
		
		// Leg B
		// -----

		// Shapes
		CircleShape footShapeB = new CircleShape();
		footShapeB.m_radius = footRadius;

		PolygonShape legShapeB = new PolygonShape();
		legShapeB.setAsBox(legRadius, 0.4f, new Vec2(0.0f, 0.4f), 0.0f);

		// Fixtures
		FixtureDef fdFootB = new FixtureDef();
		fdFootB.shape = footShapeB;
		fdFootB.density = bodyDensity;
		fdFootB.friction = 1.0f;
		fdFootB.filter.groupIndex = -1;

		FixtureDef fdLegB = new FixtureDef();
		fdLegB.shape = legShapeB;
		fdLegB.density = bodyDensity;
		fdLegB.filter.groupIndex = -1;
		// The leg body
		BodyDef bdB = new BodyDef();
		bdB.type = BodyType.DYNAMIC;
		bdB.position.set(0.0f, 0.05f);
		Body bB = w.createBody(bdB);
		bB.createFixture(fdFootB);
		bB.createFixture(fdLegB);

		// Joint
		// -----
		RevoluteJointDef rjdLegs = new RevoluteJointDef();
		rjdLegs.initialize(bA, bB, new Vec2(0.0f, 0.85f));

		rjdLegs.motorSpeed = -2.0f * MathUtils.PI;

		rjdLegs.maxMotorTorque = 10000.0f;
		rjdLegs.enableMotor = false;

		rjdLegs.lowerAngle = -0.75f * MathUtils.PI;
		rjdLegs.upperAngle = 0.75f * MathUtils.PI;
		rjdLegs.enableLimit = false;

		// rjdLegs.collideConnected = true;
		RevoluteJoint m_joint = (RevoluteJoint) w.createJoint(rjdLegs);
		setMotors(new ArrayList<RevoluteJoint>(1));
		getMotors().add(m_joint);
		
		// HIP
		// Shape
		CircleShape hipShape = new CircleShape();
		hipShape.m_radius = hipRadius;
		// Fixture
		FixtureDef fixDefHip = new FixtureDef();
		fixDefHip.shape = hipShape;
		fixDefHip.density = bodyDensity;
		fixDefHip.friction = 1.0f;
		// The hip body
		BodyDef bodyDefHip = new BodyDef();
		bodyDefHip.type = BodyType.DYNAMIC;
		bodyDefHip.position.set(0.0f, 0.85f);
		Body bodyHip = w.createBody(bodyDefHip);
		
		bodyHip.createFixture(fixDefHip);
				
		// Hip joints
		
		// -----
		RevoluteJointDef rjdHipA = new RevoluteJointDef();
		rjdHipA.initialize(bodyHip, bA, new Vec2(0.0f, 0.85f));
		w.createJoint(rjdHipA);
		RevoluteJointDef rjdHipB = new RevoluteJointDef();
		rjdHipB.initialize(bodyHip, bB, new Vec2(0.0f, 0.85f));
		w.createJoint(rjdHipB);
		
	}
	
	

	public void threeLinks(World w) {
		
		// parameters
		float footRadius = 0.05f;
		float footRadius_B = 0.05f;
		
		float legRadiusX = 0.05f;
				
		
		float legRadiusY = 0.2f;
		float legRadiusY_B = 0.19f;

		Vec2 legPosition = new Vec2(0.0f, 0.05f);
		Vec2 legPosition_B = new Vec2(0.0f, 0.09f);
		
		float bodyDensity = 1.0f;
		
		PolygonShape upperLegAShape = new PolygonShape();
		upperLegAShape.setAsBox(legRadiusX, legRadiusY, new Vec2(0.0f, -legRadiusY), 0.0f);
		
		
	
		// Fixture
		FixtureDef fixDefUpperLegA = new FixtureDef();
		fixDefUpperLegA.shape = upperLegAShape;
		fixDefUpperLegA.density = bodyDensity;
		fixDefUpperLegA.friction = 1.0f;
		fixDefUpperLegA.filter.groupIndex = -1;
		
		
		// body
		BodyDef bodyDefUpperLegA = new BodyDef();
		bodyDefUpperLegA.type = BodyType.DYNAMIC;
		bodyDefUpperLegA.position.set(0.0f, 0.85f);
		Body bodyUpperLegA = w.createBody(bodyDefUpperLegA);
		bodyUpperLegA.createFixture(fixDefUpperLegA);

		// Leg A
		// -----
		// Shapes
		CircleShape footShapeA = new CircleShape();
		footShapeA.m_radius = footRadius;

		PolygonShape lowerLegShapeA = new PolygonShape();
		lowerLegShapeA.setAsBox(legRadiusX, legRadiusY, new Vec2(0.0f, legRadiusY), 0.0f);

		// Fixtures
		FixtureDef fixDefFootA = new FixtureDef();
		fixDefFootA.shape = footShapeA;
		fixDefFootA.density = bodyDensity;
		fixDefFootA.friction = 1.0f;
		fixDefFootA.filter.groupIndex = -1;

		FixtureDef fixDefLowerLegA = new FixtureDef();
		fixDefLowerLegA.shape = lowerLegShapeA;
		fixDefLowerLegA.density = bodyDensity;
		fixDefLowerLegA.filter.groupIndex = -1;

		// The leg body
		BodyDef bodyDefLowerLegA = new BodyDef();
		bodyDefLowerLegA.type = BodyType.DYNAMIC;
		bodyDefLowerLegA.position.set(legPosition);
		Body bodyLowerLegA = w.createBody(bodyDefLowerLegA);
		bodyLowerLegA.createFixture(fixDefFootA);
		bodyLowerLegA.createFixture(fixDefLowerLegA);

		// Leg B
		// -----

		// Shapes
		CircleShape footShapeB = new CircleShape();
		footShapeB.m_radius = footRadius_B;

		PolygonShape legShapeB = new PolygonShape();
		legShapeB.setAsBox(legRadiusX, legRadiusY_B*2, new Vec2(0.0f, legRadiusY_B*2), 0.0f);

		// Fixtures
		FixtureDef fixDefFootB = new FixtureDef();
		fixDefFootB.shape = footShapeB;
		fixDefFootB.density = bodyDensity;
		fixDefFootB.friction = 1.0f;
		fixDefFootB.filter.groupIndex = -1;

		FixtureDef fixDefLegB = new FixtureDef();
		fixDefLegB.shape = legShapeB;
		fixDefLegB.density = bodyDensity;
		fixDefLegB.filter.groupIndex = -1;

		// The leg body
		BodyDef bodyDefLegB = new BodyDef();
		bodyDefLegB.type = BodyType.DYNAMIC;
		bodyDefLegB.position.set(legPosition_B);
		Body bodyLegB = w.createBody(bodyDefLegB);
		bodyLegB.createFixture(fixDefFootB);
		bodyLegB.createFixture(fixDefLegB);

		
		// Joints
		// -----
		RevoluteJointDef jointDefUpperLegAWithLegB = new RevoluteJointDef();
		jointDefUpperLegAWithLegB.initialize(bodyUpperLegA, bodyLegB, new Vec2(0.0f, 0.85f));
		

		jointDefUpperLegAWithLegB.motorSpeed = MathUtils.PI;

		jointDefUpperLegAWithLegB.maxMotorTorque = 1000.0f;
		jointDefUpperLegAWithLegB.enableMotor = false;

		jointDefUpperLegAWithLegB.lowerAngle = -0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.upperAngle = 0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.enableLimit = false;

		jointDefUpperLegAWithLegB.collideConnected = true;
		RevoluteJoint hipJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLegB);
		setMotors(new ArrayList<RevoluteJoint>(1));
		getMotors().add(hipJoint);


		// -----------
		RevoluteJointDef jointDefUpperLegAWithLowerLegA = new RevoluteJointDef();
		jointDefUpperLegAWithLowerLegA.initialize(bodyUpperLegA, bodyLowerLegA, new Vec2(0.0f, 0.45f));


		jointDefUpperLegAWithLowerLegA.motorSpeed = MathUtils.PI;

		jointDefUpperLegAWithLowerLegA.maxMotorTorque = 10000.0f;
		jointDefUpperLegAWithLowerLegA.enableMotor = false;

		jointDefUpperLegAWithLowerLegA.lowerAngle = -0.25f * MathUtils.PI;
		jointDefUpperLegAWithLowerLegA.upperAngle = 0.25f * MathUtils.PI;
		jointDefUpperLegAWithLowerLegA.enableLimit = true;
		
		jointDefUpperLegAWithLowerLegA.collideConnected = false;
		RevoluteJoint kneeJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLowerLegA);

		setMotors(new ArrayList<RevoluteJoint>());
		getMotors().add(hipJoint);
		getMotors().add(kneeJoint);
	}

	public BipedMorphology(World w) {

		ground(w);
		//guide(w);
		//compassLikeWithHip(w);
		compassLike(w);
		//threeLinks(w);

	}

	public ArrayList<RevoluteJoint> getMotors() {
		return motors;
	}

	public void setMotors(ArrayList<RevoluteJoint> motors) {
		this.motors = motors;
	}
}
