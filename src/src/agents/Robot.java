package agents;

public abstract class Robot extends Agent {
    private int battery;

    public Robot(int id, String name, String location, int battery) {
        super(id, name, location);
        this.battery = Math.max(0, Math.min(100, battery));
    }

    public boolean drainBattery(int amount) {
        if (amount > battery) {
            return false;
        }
        battery -= amount;
        return true;
    }

    public void recharge(int amount) {
        battery = Math.min(100, battery + amount);
    }

    public boolean hasBattery(int amount) {
        return battery >= amount;
    }

    public int getBattery() {
        return battery;
    }

    @Override
    public boolean isAvailable() {
        return super.isAvailable() && battery > 0;
    }

    @Override
    public String toString() {
        return super.toString() + " | battery=" + battery;
    }
}