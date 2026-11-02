package colony;

import java.util.Objects;

public class Simulation {

    private final MarsColony colony;
    private int time;

    public Simulation(MarsColony colony) {
        this.colony = Objects.requireNonNull(colony, "colony");
        this.time = 0;
    }

    public int getTime() { return time; }

    public void advanceTime(int units) {
        if (units < 0) throw new IllegalArgumentException("units must be >= 0: " + units);
        for (int i = 0; i < units; i++) {
            time++;
            colony.tick();
        }
    }

    public void advanceTime() { advanceTime(1); }
}