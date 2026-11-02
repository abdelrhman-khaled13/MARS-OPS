package environment;

import common.OperationResult;


public enum EnvironmentalCondition {

    NORMAL("Normal conditions", "normal conditions"),

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
    private final String phrase;

    EnvironmentalCondition(String displayName, String phrase) {
        this.displayName = displayName;
        this.phrase = phrase;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean allowsSurfaceOperations() {
        return true;
    }

    public boolean allowsCommunication() {
        return true;
    }

    public boolean allowsRemoteDroneControl() {
        return allowsCommunication();
    }

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