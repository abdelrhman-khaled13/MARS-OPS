/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;
import java.util.*;
/**
 *
 * @author Lenovo - LOQ
 */
public abstract class Facility {
    private final String facilityId;
    private final String name;
    private final Location location;
    private int capacity;
    private boolean operational;
    private Map<resources.ResourceType, Integer> resources;
    private ArrayList<Equipment> equipment;
    public Facility(String facilityId, String name, Location location, int capacity) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.operational = true;
        this.resources = new EnumMap<>(resources.ResourceType.class);
        this.equipment = new ArrayList<>();
    }

    public String getId() { 
        return facilityId; 
    }
    public String getName() {
        return name;
    }
    public Location getLocation() { 
        return location; 
    }
    public int getCapacity() { 
        return capacity; 
    }
    public boolean isOperational() { 
        return operational;
    }
    public void setOperational(boolean operational) { 
        this.operational = operational; 
    }

    public int getResource(resources.ResourceType type) {
        return resources.getOrDefault(type, 0);
    }

    public void addResource(resources.ResourceType type, int amount) {
        if (amount > 0) {
            resources.put(type, getResource(type) + amount);
        }
    }

    public boolean deductResource(resources.ResourceType type, int amount) {
        int current = getResource(type);
        if (amount <= current) {
            resources.put(type, current - amount);
            return true;
        }
        return false;
    }

    public void directReduceResource(resources.ResourceType type, int amount) {
        int current = getResource(type);
        resources.put(type, Math.max(0 , current - amount));
    }

    public void addEquipment(Equipment eq) {
        equipment.add(eq);
    }

    public List<Equipment> getEquipmentList() {
        return Collections.unmodifiableList(equipment);
    }

    public Map<resources.ResourceType, Integer> getAllResources() {
        return Collections.unmodifiableMap(resources);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [Name: " + name + ", Operational: " + operational + 
               ", Resources: " + resources + "]";
    }
}
