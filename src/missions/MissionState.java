package missions;


public enum MissionState {
    PENDING,
    ASSIGNED,
    IN_PROGRESS,
    COMPLETED,
    FAILED;

    public boolean canTransitionTo(MissionState next) {
        switch (this) {
            case PENDING:
                return next == ASSIGNED || next == FAILED;
            case ASSIGNED:
                return next == IN_PROGRESS || next == FAILED;
            case IN_PROGRESS:
                return next == COMPLETED || next == FAILED;
            default:
                return false;   // COMPLETED and FAILED are final
        }
    }

    public boolean isActive() {
        return this == ASSIGNED || this == IN_PROGRESS;
    }

    public boolean isFinal() {
        return this == COMPLETED || this == FAILED;
    }
}