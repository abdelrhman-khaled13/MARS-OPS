
package facilities;
import common.Location;


public class CommunicationStation extends Facility {
    private double signalStrength; // 0.0 to 1.0

    public CommunicationStation(String id, String name, Location location, int capacity) {
        super(id, name, location, capacity);
        this.signalStrength = 1.0;
    }

    public double getSignalStrength() {
        return signalStrength;
    }

    public void setSignalStrength(double signalStrength) {
        this.signalStrength = Math.max(0.0, Math.min(1.0, signalStrength));
    }
}
