/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
public class CommunicationStation extends Facility{
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
