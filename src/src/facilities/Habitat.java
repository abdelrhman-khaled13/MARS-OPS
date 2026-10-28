/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
public class Habitat extends Facility {
       
    private final LifeSupportSystem lifeSupportSystem;

    public Habitat(String id, String name, String location, int capacity) {
        super(id, name, location, capacity);
        this.lifeSupportSystem = new LifeSupportSystem();
    }

    public LifeSupportSystem getLifeSupportSystem() {
        return lifeSupportSystem;
    }

    @Override
    public String toString() {
        return super.toString() + " | " + lifeSupportSystem;
    }
}
