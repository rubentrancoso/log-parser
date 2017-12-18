package ef.com.types;

import java.util.HashMap;
import java.util.Map;

public enum DurationType {
	
	HOUR(0,"hourly"),
	DAY(1,"daily");
	
	private static Map<String, DurationType> map = new HashMap<String, DurationType>();
	
	private int durationType;
	private String description;
	
	static {
        for (DurationType durationType : DurationType.values()) {
            map.put(durationType.description, durationType);
        }
    }	
	
	DurationType(int durationType, String description) {
		this.durationType = durationType;
		this.description = description;
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
}
