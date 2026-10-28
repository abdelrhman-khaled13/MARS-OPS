/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
public class Equipment {
    private final String id;
    private final String name;
    private boolean damaged;

    public Equipment(String id, String name) {
        this.id = id;
        this.name = name;
        this.damaged = false;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isDamaged() {
        return damaged;
    }

    public void setDamaged(boolean damaged) {
        this.damaged = damaged;
    }

    @Override
    public String toString() {
        return name + " [ID: " + id + ", Damaged: " + damaged + "]";
    }
}
