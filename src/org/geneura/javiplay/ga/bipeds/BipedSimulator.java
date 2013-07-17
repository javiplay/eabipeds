package org.geneura.javiplay.ga.bipeds;

import java.util.ArrayList;

import javax.swing.plaf.basic.BasicSliderUI.ActionScroller;

import org.geneura.javiplay.ga.bipeds.BipedMotorActions.MotorActions;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.Shape;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.MathUtils;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.Fixture;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.joints.Joint;
import org.jbox2d.dynamics.joints.RevoluteJoint;
import org.jbox2d.dynamics.joints.RevoluteJointDef;
import org.jbox2d.testbed.framework.TestbedSettings;
import org.jbox2d.testbed.framework.TestbedTest;

import es.ugr.osgiliath.evolutionary.basiccomponents.genomes.ListGenome;
import es.ugr.osgiliath.evolutionary.individual.Gene;
import es.ugr.osgiliath.evolutionary.individual.Individual;

public class BipedSimulator extends TestbedTest {

	public RevoluteJoint m_joint;
	public ArrayList<RevoluteJoint> motors;
	public ArrayList<Gene> genes;

	private boolean isLeft = false;
	MotorActions current_action = MotorActions.FREE;
	int current_duration = 0;
	int current_gene;
	int acc_duration = 0;
	int individual = 0;
	int stepCount = 0;
	private float bodyDensity = 1.0f;
	public BipedFitnessCalculator bipedFitness;
	public double fitness = 0;


	@Override
	public String getTestName() {
		return "Two Legged Robot";
	}
	
	
	@Override
	public void step(TestbedSettings settings) {
		super.step(settings);
		stepCount++;
		addTextLine("Joint action: " + current_action.toString()
				+ ", duration: " + current_duration + "ms");
		// addTextLine("Keys: (l) limits, (m) motor, (a) left, (d) right");
		Vec2 v = new Vec2();
		m_joint.getAnchorA(v);

		Vec2 mpos = getMotorPosition();

		if (mpos.x > fitness)
			fitness = mpos.x;

		addTextLine("Joint position X: " + v.x);
		addTextLine("Joint position Y: " + v.y);
		addTextLine("STEPS: " + getStepCount());
		addTextLine("Gene: " + current_gene);
		addTextLine("Individual: " + individual);

	
			if (current_gene < genes.size()) {
				// check next gene with accumulated duration (in steps) of the
				// actions
				if (stepCount > acc_duration
						/ (1000 / settings.getSetting(
								TestbedSettings.Hz).value)) {
					processGene((BipedGene) genes.get(current_gene));
					acc_duration += ((BipedGene) genes.get(current_gene)).duration;
					current_gene++;
					
				}
			} else {
				System.out.println("Fitness live:" + fitness);
				reset();
			}
	
	}

	@Override
	public void initTest(boolean arg0) {
		
		setTitle(getTestName());

		// Ground
		BodyDef bdGround = new BodyDef();
		Body ground = getWorld().createBody(bdGround);
		PolygonShape shape = new PolygonShape();
		shape.setAsEdge(new Vec2(-40.0f, 0.0f), new Vec2(40.0f, 0.0f));
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
		Body bA = getWorld().createBody(bdA);
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
		Body bB = getWorld().createBody(bdB);
		bB.createFixture(fdFootB);
		bB.createFixture(fdLegB);

		// Hinge
		// -----
		RevoluteJointDef rjdLegs = new RevoluteJointDef();
		rjdLegs.initialize(bA, bB, new Vec2(0.0f, 1.3f));

		rjdLegs.motorSpeed = -1.0f * MathUtils.PI;
		rjdLegs.maxMotorTorque = 10000.0f;
		rjdLegs.enableMotor = false;

		rjdLegs.lowerAngle = -0.25f * MathUtils.PI;
		rjdLegs.upperAngle = 0.5f * MathUtils.PI;
		rjdLegs.enableLimit = false;

		// rjdLegs.collideConnected = true;
		m_joint = (RevoluteJoint) getWorld().createJoint(rjdLegs);
		motors = new ArrayList<RevoluteJoint>(1);
		motors.add(m_joint);				

	}

	@Override
	public void reset() {

		super.reset();
		current_gene = 0;
		stepCount = 0;
		fitness = 0;
		//processGene((BipedGene) genes.get(current_gene));
		acc_duration = 0;
		individual = 1;
	}

	public void reset(ArrayList<Gene> gl) {
	
		fitness = 0;
		genes = gl;
		super.reset();
		current_gene = 0;
		stepCount = 0;
		//processGene((BipedGene) genes.get(current_gene));
		acc_duration = 0;
		individual++;
		

	}

	public void processGene(BipedGene g) {

		for (int i = 0; i < motors.size(); i++) {
			current_action = g.actions.get(i);
			current_duration = g.duration;
			RevoluteJoint joint = motors.get(i);
			float speed = BipedMotorActions.MAX_SPEED;

			switch (g.actions.get(i)) {
			case FREE:
				joint.enableMotor(false);
				break;
			case LEFT:
				joint.setMotorSpeed(-speed);
				joint.enableMotor(true);
				break;
			case RIGHT:
				joint.setMotorSpeed(speed);
				joint.enableMotor(true);
				break;
			case STOP:
				joint.setMotorSpeed(0);
				joint.enableMotor(true);
				break;
			}

		}

	}

	public Vec2 getMotorPosition() {
		Vec2 v = new Vec2();
		m_joint.getAnchorA(v);
		return v;
	}

}
