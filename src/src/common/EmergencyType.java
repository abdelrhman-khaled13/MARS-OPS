package common;

import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public enum EmergencyType {
    OXYGEN_LEAK("Oxygen leak", Capability.REPAIR),
    FIRE("Fire", Capability.FIRE_RESPONSE),
    ROVER_BREAKDOWN("Rover breakdown", Capability.REPAIR),
    MEDICAL_EMERGENCY("Medical emergency", Capability.MEDICAL_TREATMENT);

    private final String displayName;
    private final Set<Capability> requiredCapabilities;

    EmergencyType(String displayName, Capability... requiredCapabilities) {
        this.displayName = displayName;
        this.requiredCapabilities = Collections.unmodifiableSet(
                EnumSet.copyOf(Arrays.asList(requiredCapabilities)));
    }

    public String getDisplayName() {
        return displayName;
    }

    public Set<Capability> getRequiredCapabilities() {
        return requiredCapabilities;
    }
}
