package emergencies;

public interface Responder {
    String getId();
    String getName();
    String getLocation();
    void setLocation(String newLocation);
    
    // Capability and availability queries using String constants
    boolean canHandleEmergency(String emergencyType);
    boolean isAvailable(); // Must return true if !isOnActiveMission() && isUsable()
    boolean isUsable();
    boolean isOnActiveMission();
    
    boolean isHuman();
    boolean isRobotic();
}