package common;


public enum Capability {
    SURFACE_TRAVEL("Surface travel"),
    CARRY_EQUIPMENT("Equipment carrying"),
    AERIAL_RECONNAISSANCE("Aerial reconnaissance"),
    FACILITY_OPERATION("Operation inside facilities"),
    SCIENTIFIC_RESEARCH("Scientific research"),
    REPAIR("Repair"),
    MEDICAL_TREATMENT("Medical treatment"),
    FIRE_RESPONSE("Fire response"),
    RESCUE("Rescue");

    private final String displayName;

    Capability(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}