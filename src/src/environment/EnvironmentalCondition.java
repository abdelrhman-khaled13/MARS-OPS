package environment;

import common.OperationResult;


public enum EnvironmentalCondition {

    /** Nothing is restricted. */
    NORMAL("Normal conditions", "normal conditions"),

    /** Surface operations cannot begin and ones already under way fail (spec 8.1). */
    DUST_STORM("Dust storm", "a dust storm") {
        @Override
        public boolean allowsSurfaceOperations() {
            return false;
        }
    },


    COMMUNICATION_DISRUPTION("Communication disruption", "a communication disruption") {
        @Override
        public boolean allowsCommunication() {
            return false;
        }
    };

    private final String displayName;
    private final String phrase;   // used inside sentences: "... not allowed during <phrase>."

    EnvironmentalCondition(String displayName, String phrase) {
        this.displayName = displayName;
        this.phrase = phrase;
    }

    public String getDisplayName() {
        return displayName;
    }

    /** Can anything be done on the Martian surface right now? */
    public boolean allowsSurfaceOperations() {
        return true;
    }

    /** Are communication links usable right now? */
    public boolean allowsCommunication() {
        return true;
    }

    /** Remote drones are steered over the communication link, so they follow it. */
    public boolean allowsRemoteDroneControl() {
        return allowsCommunication();
    }

    /** Evaluates the given operation under this condition. */
    public OperationResult evaluate(EnvironmentSensitive operation) {
        if (operation.requiresSurfaceAccess() && !allowsSurfaceOperations()) {
            return OperationResult.failure("Surface operations are not allowed during " + phrase + ".");
        }
        if (operation.requiresCommunication() && !allowsCommunication()) {
            return OperationResult.failure(
                    "Communication-dependent operations are not allowed during " + phrase + ".");
        }
        return OperationResult.success("No environmental restriction applies.");
    }

    @Override
    public String toString() {
        return displayName;
    }
}