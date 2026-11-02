
package facilities;
import common.*;



public class Habitat extends Facility {
       
    private final LifeSupportSystem lifeSupportSystem;

    public Habitat(String id, String name, Location location, int capacity) {
        super(id, name, location, capacity);
        this.lifeSupportSystem = new LifeSupportSystem();
    }

    public LifeSupportSystem getLifeSupportSystem() {
        return lifeSupportSystem;
    }

   
    public int applyOxygenLeak(int amount) {
        return getInventory().deductAndClamp(ResourceType.OXYGEN, amount);
    }

    @Override
    public String toString() {
        return super.toString() + " | " + lifeSupportSystem;
    }
}
