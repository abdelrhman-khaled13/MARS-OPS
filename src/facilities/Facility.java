
package facilities;

import common.*;
import java.util.*;


public abstract class Facility {
    private final String facilityId;
    private final String name;
    private final Location location;
    private int capacity;
    private boolean operational;
    private final ResourceInventory inventory;   
    private final List<Equipment> equipment;     

    public Facility(String facilityId, String name, Location location, int capacity) {
        this.facilityId = facilityId;
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.operational = true;
        this.inventory = new ResourceInventory();
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

    
    public void damaged() {
        this.operational = false;
    }

    
    public void repair() {
        this.operational = true;
    }

    

   
    public ResourceInventory getInventory() {
        return inventory;
    }

    public int getResource(ResourceType type) {
        return inventory.get(type);
    }

    public void addResource(ResourceType type, int amount) {
        inventory.add(type, amount);
    }

    public boolean deductResource(ResourceType type, int amount) {
        return inventory.remove(type, amount);
    }

    
    public int directReduceResource(ResourceType type, int amount) {
        return inventory.deductAndClamp(type, amount);
    }

    public Map<ResourceType, Integer> getAllResources() {
        return inventory.getAll();
    }

    

    public void addEquipment(Equipment eq) {
        equipment.add(eq);
    }

    
    public boolean removeEquipment(Equipment eq) {
        return equipment.remove(eq);
    }

    public List<Equipment> getEquipmentList() {
        return Collections.unmodifiableList(equipment);
    }

   

    @Override
    public String toString() {
        return getClass().getSimpleName() + " [Name: " + name + ", Operational: " + operational + 
               ", Resources: " + inventory + "]";
    }
}
