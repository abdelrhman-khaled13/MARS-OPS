package environment;

import common.OperationResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;


public class Environment {

    private static final int OPEN_ENDED = -1;

    private EnvironmentalCondition condition = EnvironmentalCondition.NORMAL;
    private int remainingTime = OPEN_ENDED;
    private final List<Consumer<EnvironmentalCondition>> listeners = new ArrayList<>();

    // ------------------------------------------------------------------ state

    public EnvironmentalCondition getCondition() {
        return condition;
    }


    public int getRemainingTime() {
        return remainingTime;
    }

    public void setCondition(EnvironmentalCondition newCondition) {
        apply(newCondition, OPEN_ENDED);
    }

    public void setCondition(EnvironmentalCondition newCondition, int duration) {
        if (duration <= 0) {
            throw new IllegalArgumentException("Duration must be positive: " + duration);
        }
        apply(newCondition, duration);
    }

    public void clearCondition() {
        apply(EnvironmentalCondition.NORMAL, OPEN_ENDED);
    }

    // ------------------------------------------------------------------ time

    public void advanceTime(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Cannot advance time by a negative amount: " + units);
        }
        for (int i = 0; i < units; i++) {
            advanceOneUnit();
        }
    }

    public void advanceTime() {
        advanceTime(1);
    }

    private void advanceOneUnit() {
        if (remainingTime == OPEN_ENDED) {
            return;
        }
        remainingTime--;
        if (remainingTime == 0) {
            apply(EnvironmentalCondition.NORMAL, OPEN_ENDED);
        }
    }

    // --------------------------------------------------------------- queries


    public OperationResult check(EnvironmentSensitive operation) {
        return condition.evaluate(Objects.requireNonNull(operation, "operation"));
    }

    public boolean isSurfaceSafe() {
        return condition.allowsSurfaceOperations();
    }

    public boolean isCommunicationAvailable() {
        return condition.allowsCommunication();
    }

    public boolean canControlRemoteDrones() {
        return condition.allowsRemoteDroneControl();
    }

    // ------------------------------------------------------------- listeners

    public void addChangeListener(Consumer<EnvironmentalCondition> listener) {
        listeners.add(Objects.requireNonNull(listener, "listener"));
    }

    private void apply(EnvironmentalCondition next, int duration) {
        Objects.requireNonNull(next, "condition");
        boolean changed = next != condition;
        condition = next;
        remainingTime = (next == EnvironmentalCondition.NORMAL) ? OPEN_ENDED : duration;
        if (changed) {
            // iterate over a copy: a listener may legitimately register another listener
            for (Consumer<EnvironmentalCondition> listener : new ArrayList<>(listeners)) {
                listener.accept(next);
            }
        }
    }

    @Override
    public String toString() {
        return remainingTime == OPEN_ENDED
                ? condition.getDisplayName()
                : condition.getDisplayName() + " (" + remainingTime + " time unit(s) remaining)";
    }
}