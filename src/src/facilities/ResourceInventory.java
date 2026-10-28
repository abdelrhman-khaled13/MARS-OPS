package facilities;

import common.ResourceType;
import java.util.*;



public class ResourceInventory {
    private final Map<ResourceType, Integer> quantities;

    public ResourceInventory() {
        this.quantities = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            quantities.put(type, 0);
        }
    }

    
    public int get(ResourceType type) {
        return quantities.getOrDefault(type, 0);
    }

   
    public void add(ResourceType type, int amount) {
        if (amount > 0) {
            quantities.put(type, get(type) + amount);
        }
    }

   
    public boolean remove(ResourceType type, int amount) {
        if (amount <= 0)
             return false;
        int current = get(type);
        if (current >= amount) {
            quantities.put(type, current - amount);
            return true;
        }
        return false;
    }

  
    public int deductAndClamp(ResourceType type, int amount) {
        if (amount <= 0) 
            return 0;
        int current = get(type);
        int actualLost = Math.min(current, amount);
        quantities.put(type, current - actualLost);
        return actualLost;
    }

    
    public Map<ResourceType, Integer> getAll() {
        return Collections.unmodifiableMap(quantities);
    }

    @Override
    public String toString() {
        return quantities.toString();
    }
}
