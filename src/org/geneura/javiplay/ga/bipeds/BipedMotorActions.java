package org.geneura.javiplay.ga.bipeds;

import org.jbox2d.common.MathUtils;

public interface BipedMotorActions {
	
	public enum MotorActions {FREE, LEFT, RIGHT, STOP}
	public static final float MAX_SPEED = MathUtils.PI;

}
