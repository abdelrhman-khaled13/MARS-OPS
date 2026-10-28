/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
public class Powerstation extends Facility {
    private double powerOutputKw;

    public Powerstation(String id, String name, Location location, int capacity, double powerOutputKw) {
        super(id, name, location, capacity);
        this.powerOutputKw = powerOutputKw;
    }

    public double getPowerOutputKw() {
        return powerOutputKw;
    }
}

