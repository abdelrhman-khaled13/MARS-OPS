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

    /** ASSIGNED or IN_PROGRESS: the mission holds its agents and occupies the single active slot. */
    public boolean isActive() {
        return this == ASSIGNED || this == IN_PROGRESS;
    }

    /** COMPLETED or FAILED. */
    public boolean isFinal() {
        return this == COMPLETED || this == FAILED;
    }
}