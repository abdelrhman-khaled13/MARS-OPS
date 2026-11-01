package common;

public interface Responder {
    String getId();
    String getName();
    Location getLocation();
    void setLocation(Location location);
    boolean canHandleEmergency(EmergencyType type);
    boolean isAvailable();
    boolean isUsable();
    boolean isOnActiveMission();
    boolean isHuman();
    boolean isRobotic();
}
