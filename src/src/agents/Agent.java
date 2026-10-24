package agents;

public abstract class Agent {
    private final int id;
    private final String name;
    private String location;
    private int health;
    private boolean onMission;

    public Agent(int id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.health = 100;
        this.onMission = false;
    }

    public abstract boolean canPerform(String missionType);

    public abstract boolean canHandle(String emergencyType);

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

    public void moveTo(String newLocation) {
        this.location = newLocation;
    }

    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(100, health));
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLocation() {
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