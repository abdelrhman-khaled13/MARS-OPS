package emergencies;

public interface ColonyContext {
    boolean isFacilityOperational(String facilityId);
    void setFacilityOperational(String facilityId, boolean operational);
    double getFacilityResource(String facilityId, String resourceType);
    void deductFacilityResource(String facilityId, String resourceType, double amount);

    boolean hasResource(String resourceType, double requiredAmount);
    boolean consumeResource(String resourceType, double amount);

    void setEntityHealth(String entityId, double health);
    double getEntityHealth(String entityId);
    void setEntityDamaged(String entityId, boolean damaged);
}