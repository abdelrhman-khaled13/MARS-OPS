/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
public class LifeSupportSystem {
    private boolean operational;
    private double filtrationEfficiency; 

    public LifeSupportSystem() {
        this.operational = true;
        this.filtrationEfficiency = 0.98;
    }

    public boolean isOperational() {
        return operational;
    }

    public void setOperational(boolean operational) {
        this.operational = operational;
    }

    public double getFiltrationEfficiency() {
        return filtrationEfficiency;
    }

    public void setFiltrationEfficiency(double filtrationEfficiency) {
        this.filtrationEfficiency = filtrationEfficiency;
    }

    @Override
    public String toString() {
        return "LifeSupportSystem [Operational: " + operational + ", Efficiency: " + (filtrationEfficiency * 100) + "%]";
    }
}
