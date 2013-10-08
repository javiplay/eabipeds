package org.geneura.javiplay.bipeds.ea.linearcontroller;
import java.util.HashMap;
import java.util.Map;

public enum MotorActions {
	FREE(0), LEFT(1), RIGHT(2), STOP(3);
	private int value;
	private static final Map<Integer, MotorActions> typesByValue = new HashMap<Integer, MotorActions>();
	
	static {
		for (MotorActions type : MotorActions.values()) {
			typesByValue.put(type.value, type);
		}
	}

	private MotorActions(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return value;
	}
	
	public static MotorActions forValue(int value) {
		return typesByValue.get(value);
	}
	public static int Length() {
		return 4;
	}
}
