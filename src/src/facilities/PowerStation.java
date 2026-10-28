package facilities;
import common.Location;


public class PowerStation extends Facility {
    private double powerOutputKw;

    public PowerStation(String id, String name, Location location, int capacity, double powerOutputKw) {
        super(id, name, location, capacity);
        this.powerOutputKw = powerOutputKw;
    }

    public double getPowerOutputKw() {
        return powerOutputKw;
    }
}
