package agents;

import common.Capability;
import common.EmergencyType;
import common.Location;
import common.MissionSpec;
import common.Responder;

import java.util.Set;

public abstract class Agent implements Responder {
    private final int id;
    private final String name;
    private Location location;
    private int health;
    private boolean onMission;

    public Agent(int id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.health = 100;
        this.onMission = false;
    }

    public abstract Set<Capability> getCapabilities();

    public boolean canPerform(MissionSpec spec) {
        return spec != null && spec.isSatisfiedBy(getCapabilities());
    }

    @Override
    public boolean canHandleEmergency(EmergencyType type) {
        return type != null && getCapabilities().containsAll(type.getRequiredCapabilities());
    }

    @Override
    public boolean isOnActiveMission() {
        return onMission;
    }

    @Override
    public abstract boolean isHuman();

    @Override
    public abstract boolean isRobotic();

    public boolean isUsable() {
        return health > 0;
    }

    public boolean isAvailable() {
        return isUsable() && !onMission;
    }

    public boolean assign() {
        if (!isAvailable()) {
            return false;
        }
        onMission = true;
        return true;
    }

    public void release() {
        onMission = false;
    }

    public void moveTo(Location newLocation) {
        this.location = newLocation;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(100, health));
    }

    public void setAvailable(boolean available) {
        this.onMission = !available;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public String getId() {
        return Integer.toString(id);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public int getHealth() {
        return health;
    }

    public boolean isOnMission() {
        return onMission;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + " #" + id + " " + name
                + " | location=" + location
                + " | health=" + health
                + " | available=" + isAvailable();
    }
}