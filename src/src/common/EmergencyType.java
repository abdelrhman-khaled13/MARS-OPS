package common;


public enum EmergencyType {
    OXYGEN_LEAK("Oxygen leak"),
    FIRE("Fire"),
    ROVER_BREAKDOWN("Rover breakdown"),
    MEDICAL_EMERGENCY("Medical emergency");

    private final String displayName;

    EmergencyType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}