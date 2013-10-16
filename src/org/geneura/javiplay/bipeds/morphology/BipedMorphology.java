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
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.PrismaticJoint;
import org.jbox2d.dynamics.joints.PrismaticJointDef;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;

public class BipedMorphology {

	private ArrayList<Joint> joints;
	

	public void ground(World w) {
		// Ground
		BodyDef bdGround = new BodyDef();
		
		
		Body ground = w.createBody(bdGround);
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-1000.0f, 0.0f), new Vec2(1000.0f, -0.0f));
		ground.createFixture(shape, 0.0f).setFriction(1.0f);
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
		float footRadius = 0.02f;
		float legRadius = 0.02f;
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
		setMotors(new ArrayList<Joint>(1));
		getJoints().add(m_joint);
		

	}
	
public void simplePendulum(World w) {
	
		
		// Parameters		
		float footRadius = 0.02f;
		float legRadius = 0.02f;
		float bodyDensity = 1.0f;
		
		
		// Static box
		// 
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(0.1f, 0.1f);
		
	
		BodyDef bdWall = new BodyDef();
		bdWall.position = new Vec2(0.0f, 1.6f);
		
		Body wall = w.createBody(bdWall);
		
		wall.createFixture(wallShape, 0.0f);
		
		
		
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
		bdA.position.set(0.0f, 1.6f);
		Body bA = w.createBody(bdA);
		bA.createFixture(fdLegA);
		bA.createFixture(fdFootA);
		

		// Joint
		// -----
		RevoluteJointDef rjdWall = new RevoluteJointDef();
		rjdWall.initialize(wall, bA, new Vec2(0.0f, 1.6f));
		rjdWall.motorSpeed = -2.0f * MathUtils.PI;

		rjdWall.maxMotorTorque = 10000.0f;
		rjdWall.enableMotor = false;

		rjdWall.lowerAngle = -0.75f * MathUtils.PI;
		rjdWall.upperAngle = 0.75f * MathUtils.PI;
		rjdWall.enableLimit = false;

		joints = new ArrayList<Joint>();
		joints.add(w.createJoint(rjdWall));

		

	}

	
public void doublePendulum(World w) {
	
	
		
		// Parameters		
		float footRadius = 0.02f;
		float legRadius = 0.02f;
		float bodyDensity = 1.0f;
		
		
		// Static box
		// 
		PolygonShape wallShape = new PolygonShape();
		wallShape.setAsBox(0.1f, 0.1f);
		
	
		BodyDef bdWall = new BodyDef();
		bdWall.position = new Vec2(0.0f, 1.6f);
		
		Body wall = w.createBody(bdWall);
		
		wall.createFixture(wallShape, 0.0f);
		
		
		
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
		bdA.position.set(0.0f, 1.6f);
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
		bdB.position.set(0.0f, 1.6f);
		Body bB = w.createBody(bdB);
		bB.createFixture(fdFootB);
		bB.createFixture(fdLegB);

		// Joint
		// -----
		RevoluteJointDef rjdLegs = new RevoluteJointDef();
		rjdLegs.initialize(bA, bB, new Vec2(0.0f, 2.4f));

		rjdLegs.motorSpeed = -2.0f * MathUtils.PI;

		rjdLegs.maxMotorTorque = 10000.0f;
		rjdLegs.enableMotor = false;

		rjdLegs.lowerAngle = -0.75f * MathUtils.PI;
		rjdLegs.upperAngle = 0.75f * MathUtils.PI;
		rjdLegs.enableLimit = false;

		// rjdLegs.collideConnected = true;
		RevoluteJoint m_joint = (RevoluteJoint) w.createJoint(rjdLegs);
		setMotors(new ArrayList<Joint>(1));
		getJoints().add(m_joint);
		
		RevoluteJointDef rjdWall = new RevoluteJointDef();
		rjdWall.initialize(wall, bA, new Vec2(0.0f, 1.6f));
		w.createJoint(rjdWall);

		

	}
	
public void compassLikeUneven(World w) {
		
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
		
		PolygonShape legAddedShapeA = new PolygonShape();
		legAddedShapeA.setAsBox(0.1f, 0.1f, new Vec2(0.05f, 0.8f), 0.0f);


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
		
		FixtureDef fdAddedLegA = new FixtureDef();
		fdAddedLegA.shape = legAddedShapeA;
		fdAddedLegA.density = bodyDensity;
		fdAddedLegA.filter.groupIndex = -1;


		// The leg body
		BodyDef bdA = new BodyDef();
		bdA.type = BodyType.DYNAMIC;
		bdA.position.set(0.0f, 0.05f);
		Body bA = w.createBody(bdA);
		bA.createFixture(fdLegA);
		bA.createFixture(fdFootA);
		bA.createFixture(fdAddedLegA);
		
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
		setMotors(new ArrayList<Joint>());
		getJoints().add(m_joint);
		

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
		setMotors(new ArrayList<Joint>(1));
		getJoints().add(m_joint);
		
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
	
public void threeLinksOneKneeAsymetric(World w) {
	
	// parameters
	float footRadius_A = 0.05f;
	float footRadius_B = 0.05f;
	
	float legRadiusX = 0.05f;
			
	
	float legRadiusY_A = 0.2f;
	float legRadiusY_B = 0.2f;

	Vec2 legPosition = new Vec2(0.0f, 0.05f);
	Vec2 legPosition_B = new Vec2(0.0f, 0.05f);
	
	float bodyDensity = 1.0f;
	
	PolygonShape upperLegAShape = new PolygonShape();
	upperLegAShape.setAsBox(legRadiusX, legRadiusY_A, new Vec2(0.0f, -legRadiusY_A), 0.0f);
	
	

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
	footShapeA.m_radius = footRadius_A;

	PolygonShape lowerLegShapeA = new PolygonShape();
	lowerLegShapeA.setAsBox(legRadiusX, legRadiusY_A+0.01f, new Vec2(0.0f, legRadiusY_A+0.01f), 0.0f);

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
	bodyDefLowerLegA.position.set(legPosition.x, legPosition.y-0.01f);
	Body bodyLowerLegA = w.createBody(bodyDefLowerLegA);
	bodyLowerLegA.createFixture(fixDefFootA);
	bodyLowerLegA.createFixture(fixDefLowerLegA);

	// Leg B
	// -----

	// Shapes
	CircleShape footShapeB = new CircleShape();
	footShapeB.m_radius = footRadius_B;
	
	//PolygonShape legAddedShape = new PolygonShape();
	//legAddedShape.setAsBox(0.1f, 0.1f, new Vec2(0.05f, 0.8f), 0.0f);

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
	/*
	FixtureDef fdAddedLeg = new FixtureDef();
	fdAddedLeg.shape = legAddedShape;
	fdAddedLeg.density = bodyDensity;
	fdAddedLeg.filter.groupIndex = -1;
	 */
	// The leg body
	BodyDef bodyDefLegB = new BodyDef();
	bodyDefLegB.type = BodyType.DYNAMIC;
	bodyDefLegB.position.set(legPosition_B);
	Body bodyLegB = w.createBody(bodyDefLegB);
	bodyLegB.createFixture(fixDefFootB);
	bodyLegB.createFixture(fixDefLegB);
	//bodyLegB.createFixture(fdAddedLeg);

	
	// Joints
	// -----
	RevoluteJointDef jointDefUpperLegAWithLegB = new RevoluteJointDef();
	jointDefUpperLegAWithLegB.initialize(bodyUpperLegA, bodyLegB, new Vec2(0.0f, 0.85f));
	

	jointDefUpperLegAWithLegB.motorSpeed = MathUtils.PI;

	jointDefUpperLegAWithLegB.maxMotorTorque = 10000.0f;
	jointDefUpperLegAWithLegB.enableMotor = false;

	jointDefUpperLegAWithLegB.lowerAngle = -0.75f * MathUtils.PI;
	jointDefUpperLegAWithLegB.upperAngle = 0.75f * MathUtils.PI;
	jointDefUpperLegAWithLegB.enableLimit = true;

	jointDefUpperLegAWithLegB.collideConnected = true;
	RevoluteJoint hipJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLegB);
	setMotors(new ArrayList<Joint>(1));
	getJoints().add(hipJoint);


	// -----------
	
	RevoluteJointDef jointDefUpperLegAWithLowerLegA = new RevoluteJointDef();
	
	jointDefUpperLegAWithLowerLegA.initialize(bodyUpperLegA, bodyLowerLegA, new Vec2(0.0f, 0.45f));


	jointDefUpperLegAWithLowerLegA.motorSpeed = MathUtils.PI;

	jointDefUpperLegAWithLowerLegA.maxMotorTorque = 10000.0f;
	jointDefUpperLegAWithLowerLegA.enableMotor = false;

	jointDefUpperLegAWithLowerLegA.lowerAngle = (float) (-0.5f*Math.PI);
	jointDefUpperLegAWithLowerLegA.upperAngle = (float) (0.15f*Math.PI);
	jointDefUpperLegAWithLowerLegA.collideConnected = false;
	jointDefUpperLegAWithLowerLegA.enableLimit = true;
	
	jointDefUpperLegAWithLowerLegA.collideConnected = false;
	RevoluteJoint kneeJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLowerLegA);

	setMotors(new ArrayList<Joint>());
	getJoints().add(hipJoint);
	getJoints().add(kneeJoint);
}
	

	public void threeLinksPrismaticKnee(World w) {
		
		// parameters
		float footRadius_A = 0.03f;
		float footRadius_B = 0.03f;
		
		float legRadiusX = 0.05f;
				
		
		float legRadiusY_A = 0.2f;
		float legRadiusY_B = 0.2f;

		Vec2 legPosition = new Vec2(0.0f, 0.05f);
		Vec2 legPosition_B = new Vec2(0.0f, 0.05f);
		
		float bodyDensity = 1.0f;
		
		PolygonShape upperLegAShape = new PolygonShape();
		upperLegAShape.setAsBox(legRadiusX, legRadiusY_A, new Vec2(0.0f, -legRadiusY_A), 0.0f);
		
		
	
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
		footShapeA.m_radius = footRadius_A;

		PolygonShape lowerLegShapeA = new PolygonShape();
		lowerLegShapeA.setAsBox(legRadiusX, legRadiusY_A, new Vec2(0.0f, legRadiusY_A), 0.0f);

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

		jointDefUpperLegAWithLegB.maxMotorTorque = 10.0f;
		jointDefUpperLegAWithLegB.enableMotor = false;

		jointDefUpperLegAWithLegB.lowerAngle = -0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.upperAngle = 0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.enableLimit = true;

		jointDefUpperLegAWithLegB.collideConnected = true;
		RevoluteJoint hipJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLegB);
		setMotors(new ArrayList<Joint>(1));
		getJoints().add(hipJoint);


		// -----------
		
		PrismaticJointDef jointDefUpperLegAWithLowerLegA = new PrismaticJointDef();
		
		jointDefUpperLegAWithLowerLegA.initialize(bodyUpperLegA, bodyLowerLegA, new Vec2(0.0f, 0.45f), new Vec2(0.0f, 1.0f));


		jointDefUpperLegAWithLowerLegA.motorSpeed = MathUtils.PI;

		jointDefUpperLegAWithLowerLegA.maxMotorForce = 10.0f;
		jointDefUpperLegAWithLowerLegA.enableMotor = false;

		jointDefUpperLegAWithLowerLegA.lowerTranslation = -0.05f;
		jointDefUpperLegAWithLowerLegA.upperTranslation = 0.05f;
		jointDefUpperLegAWithLowerLegA.collideConnected = false;
		jointDefUpperLegAWithLowerLegA.enableLimit = true;
		
		jointDefUpperLegAWithLowerLegA.collideConnected = false;
		PrismaticJoint kneeJoint = (PrismaticJoint) w.createJoint(jointDefUpperLegAWithLowerLegA);

		setMotors(new ArrayList<Joint>());
		getJoints().add(hipJoint);
		getJoints().add(kneeJoint);
	}

	public BipedMorphology(World w) {

		//ground(w);
		//guide(w);
		//compassLikeWithHip(w);
		//compassLikeUneven(w);
		//compassLike(w);
		//threeLinksOneKneeAsymetric(w);
		//doublePendulum(w);
		simplePendulum(w);

	}

	public ArrayList<Joint> getJoints() {
		return joints;
	}

	public void setMotors(ArrayList<Joint> joints) {
		this.joints = joints;
	}

	public void threeLinksOneKnee(World w) {
		
		// parameters
		float footRadius_A = 0.08f;
		float footRadius_B = 0.05f;
		
		float legRadiusX = 0.05f;
				
		
		float legRadiusY_A = 0.2f;
		float legRadiusY_B = 0.2f;
	
		Vec2 legPosition = new Vec2(0.0f, 0.05f);
		Vec2 legPosition_B = new Vec2(0.0f, 0.05f);
		
		float bodyDensity = 1.0f;
		
		PolygonShape upperLegAShape = new PolygonShape();
		upperLegAShape.setAsBox(legRadiusX, legRadiusY_A, new Vec2(0.0f, -legRadiusY_A), 0.0f);
		
		
	
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
		footShapeA.m_radius = footRadius_A;
	
		PolygonShape lowerLegShapeA = new PolygonShape();
		lowerLegShapeA.setAsBox(legRadiusX, legRadiusY_A, new Vec2(0.0f, legRadiusY_A), 0.0f);
	
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
		
		//PolygonShape legAddedShape = new PolygonShape();
		//legAddedShape.setAsBox(0.1f, 0.1f, new Vec2(0.05f, 0.8f), 0.0f);
	
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
		/*
		FixtureDef fdAddedLeg = new FixtureDef();
		fdAddedLeg.shape = legAddedShape;
		fdAddedLeg.density = bodyDensity;
		fdAddedLeg.filter.groupIndex = -1;
		 */
		// The leg body
		BodyDef bodyDefLegB = new BodyDef();
		bodyDefLegB.type = BodyType.DYNAMIC;
		bodyDefLegB.position.set(legPosition_B);
		Body bodyLegB = w.createBody(bodyDefLegB);
		bodyLegB.createFixture(fixDefFootB);
		bodyLegB.createFixture(fixDefLegB);
		//bodyLegB.createFixture(fdAddedLeg);
	
		
		// Joints
		// -----
		RevoluteJointDef jointDefUpperLegAWithLegB = new RevoluteJointDef();
		jointDefUpperLegAWithLegB.initialize(bodyUpperLegA, bodyLegB, new Vec2(0.0f, 0.85f));
		
	
		jointDefUpperLegAWithLegB.motorSpeed = MathUtils.PI;
	
		jointDefUpperLegAWithLegB.maxMotorTorque = 10000.0f;
		jointDefUpperLegAWithLegB.enableMotor = false;
	
		jointDefUpperLegAWithLegB.lowerAngle = -0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.upperAngle = 0.75f * MathUtils.PI;
		jointDefUpperLegAWithLegB.enableLimit = true;
	
		jointDefUpperLegAWithLegB.collideConnected = true;
		RevoluteJoint hipJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLegB);
		setMotors(new ArrayList<Joint>(1));
		getJoints().add(hipJoint);
	
	
		// -----------
		
		RevoluteJointDef jointDefUpperLegAWithLowerLegA = new RevoluteJointDef();
		
		jointDefUpperLegAWithLowerLegA.initialize(bodyUpperLegA, bodyLowerLegA, new Vec2(0.0f, 0.45f));
	
	
		jointDefUpperLegAWithLowerLegA.motorSpeed = MathUtils.PI;
	
		jointDefUpperLegAWithLowerLegA.maxMotorTorque = 10000.0f;
		jointDefUpperLegAWithLowerLegA.enableMotor = false;
	
		jointDefUpperLegAWithLowerLegA.lowerAngle = (float) (-0.5f*Math.PI);
		jointDefUpperLegAWithLowerLegA.upperAngle = (float) (0.15f*Math.PI);
		jointDefUpperLegAWithLowerLegA.collideConnected = false;
		jointDefUpperLegAWithLowerLegA.enableLimit = true;
		
		jointDefUpperLegAWithLowerLegA.collideConnected = false;
		RevoluteJoint kneeJoint = (RevoluteJoint) w.createJoint(jointDefUpperLegAWithLowerLegA);
	
		setMotors(new ArrayList<Joint>());
		getJoints().add(hipJoint);
		getJoints().add(kneeJoint);
	}
}
