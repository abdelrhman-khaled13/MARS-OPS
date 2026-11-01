package common;

public interface ColonyContext {
    boolean isFacilityOperational(String facilityId);
    void setFacilityOperational(String facilityId, boolean operational);
    double getFacilityResource(String facilityId, ResourceType type);
    void deductFacilityResource(String facilityId, ResourceType type, double amount);
    boolean hasResource(ResourceType type, double requiredAmount);
    boolean consumeResource(ResourceType type, double amount);
    void setEntityHealth(String entityId, double health);
    double getEntityHealth(String entityId);
    void setEntityDamaged(String entityId, boolean damaged);
    boolean failActiveMission(String reason);
    void damageFacilityEquipment(String facilityId);
}
