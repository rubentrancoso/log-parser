package ef.com.types;

import java.util.HashMap;
import java.util.Map;

public enum DurationType {
	
	HOUR(0,"hourly","hour"),
	DAY(1,"daily", "day");
	
	private static Map<String, DurationType> map = new HashMap<String, DurationType>();
	
	private int durationType;
	private String description;
	private String dimension;
	
	static {
        for (DurationType durationType : DurationType.values()) {
            map.put(durationType.description, durationType);
        }
    }	
	
	DurationType(int durationType, String description, String dimension) {
		this.durationType = durationType;
		this.description = description;
		this.dimension = dimension;
	}
	
	public static DurationType getDurationType(String durationType) {
		return map.get(durationType);
	}	
	
	public int durationType() {
		return this.durationType;
	}
	public String description() {
		return this.description;
	}
	
	public String dimension() {
		return this.dimension;
	}
}
