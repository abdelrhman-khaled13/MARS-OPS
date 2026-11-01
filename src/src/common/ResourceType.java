package common;

public enum ResourceType {
    OXYGEN("Oxygen"),
    WATER("Water"),
    FOOD("Food"),
    FUEL("Fuel"),
    BATTERY_POWER("Battery power"),
    MEDICAL_SUPPLIES("Medical supplies"),
    SPARE_PARTS("Spare parts");

    private final String displayName;

    ResourceType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}