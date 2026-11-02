package colony;

import agents.Agent;
import common.ColonyContext;
import common.Responder;
import emergencies.Emergency;
import emergencies.EmergencyManager;
import common.Location;
import common.OperationResult;
import common.ResourceRequest;
import common.ResourceType;
import environment.Environment;
import facilities.Facility;
import missions.Mission;
import missions.MissionManager;
import resources.ResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;


public class MarsColony implements ColonyContext {

    private final List<Agent> agents      = new ArrayList<>();
    private final List<Facility> facilities = new ArrayList<>();

    private final ResourceManager  resourceManager;
    private final Environment      environment;
    private final MissionManager   missionManager;
    private final EmergencyManager emergencyManager;

    public MarsColony(Environment environment) {
        this.environment     = Objects.requireNonNull(environment, "environment");
        this.resourceManager = new ResourceManager(facilities);       // shares the same list
        this.missionManager  = new MissionManager(resourceManager, environment);
        this.emergencyManager = new EmergencyManager();
    }

    // ---------- wiring ----------
    public void addAgent(Agent a)       { if (a != null) agents.add(a); }
    public void addFacility(Facility f) { if (f != null) facilities.add(f); }

    // ---------- delegation ----------
    public void triggerEmergency(Emergency emergency) {
        List<Responder> responders = new ArrayList<>(agents);
        emergencyManager.triggerEmergency(emergency, responders, this);
    }

    public OperationResult submitMission(Mission m) {
        return missionManager.submit(m);
    }

    public int getResourceTotal(ResourceType type) {
        return resourceManager.getTotal(type);
    }

    public boolean canConsume(List<ResourceRequest> reqs) {
        return resourceManager.canConsume(reqs);
    }

    /** Resolves a Location to the Facility it names, or null if it is the surface. */
    public Facility resolve(Location loc) {
        if (loc == null || loc.isSurface()) return null;
        for (Facility f : facilities) {
            if (f.getId().equals(loc.getFacilityId())) return f;
        }
        return null;
    }

    // ---------- time ----------
    /** One simulation tick: environment first, then missions. */
    public void tick() {
        environment.advanceTime(1);
        missionManager.advanceTime(1);
    }

    // ---------- accessors ----------
    public List<Agent>    getAgents()     { return Collections.unmodifiableList(agents); }
    public List<Facility> getFacilities() { return Collections.unmodifiableList(facilities); }
    public Environment    getEnvironment(){ return environment; }
    public MissionManager getMissionManager() { return missionManager; }
    public EmergencyManager getEmergencyManager() { return emergencyManager; }
    public ResourceManager getResourceManager() { return resourceManager; }

    // ---------- emergency context ----------
    @Override
    public boolean isFacilityOperational(String facilityId) {
        Facility facility = findFacility(facilityId);
        return facility != null && facility.isOperational();
    }

    @Override
    public void setFacilityOperational(String facilityId, boolean operational) {
        Facility facility = requireFacility(facilityId);
        if (operational) facility.repair(); else facility.damaged();
        missionManager.reevaluateActiveMission();
    }

    @Override
    public double getFacilityResource(String facilityId, ResourceType type) {
        return requireFacility(facilityId).getResource(Objects.requireNonNull(type, "type"));
    }

    @Override
    public void deductFacilityResource(String facilityId, ResourceType type, double amount) {
        int quantity = wholeQuantity(amount);
        if (quantity == 0) return;
        requireFacility(facilityId).directReduceResource(Objects.requireNonNull(type, "type"), quantity);
    }

    @Override
    public boolean hasResource(ResourceType type, double requiredAmount) {
        int quantity = wholeQuantity(requiredAmount);
        return quantity == 0 || resourceManager.getTotal(Objects.requireNonNull(type, "type")) >= quantity;
    }

    @Override
    public boolean consumeResource(ResourceType type, double amount) {
        int quantity = wholeQuantity(amount);
        if (quantity == 0) return true;
        return resourceManager.consume(Collections.singletonList(
                new ResourceRequest(Objects.requireNonNull(type, "type"), quantity))).isSuccess();
    }

    @Override
    public void setEntityHealth(String entityId, double health) {
        if (!Double.isFinite(health)) throw new IllegalArgumentException("Health must be finite.");
        requireAgent(entityId).setHealth((int) Math.round(health));
    }

    @Override
    public double getEntityHealth(String entityId) {
        return requireAgent(entityId).getHealth();
    }

    @Override
    public void setEntityDamaged(String entityId, boolean damaged) {
        requireAgent(entityId).setHealth(damaged ? 0 : 100);
        missionManager.reevaluateActiveMission();
    }

    @Override
    public boolean failActiveMission(String reason) {
        return missionManager.failActiveMission(reason).isSuccess();
    }

    @Override
    public void damageFacilityEquipment(String facilityId) {
        for (facilities.Equipment equipment : requireFacility(facilityId).getEquipmentList()) {
            equipment.damaged();
        }
        missionManager.reevaluateActiveMission();
    }

    private Facility findFacility(String facilityId) {
        if (facilityId == null) return null;
        for (Facility facility : facilities) {
            if (facility.getId().equals(facilityId)) return facility;
        }
        return null;
    }

    private Facility requireFacility(String facilityId) {
        Facility facility = findFacility(facilityId);
        if (facility == null) throw new IllegalArgumentException("Unknown facility: " + facilityId);
        return facility;
    }

    private Agent requireAgent(String entityId) {
        for (Agent agent : agents) {
            if (agent.getId().equals(entityId)) return agent;
        }
        throw new IllegalArgumentException("Unknown agent: " + entityId);
    }

    private static int wholeQuantity(double amount) {
        if (!Double.isFinite(amount) || amount < 0 || amount > Integer.MAX_VALUE || amount != Math.rint(amount)) {
            throw new IllegalArgumentException("Resource amounts must be whole numbers between 0 and "
                    + Integer.MAX_VALUE + ": " + amount);
        }
        return (int) amount;
    }

    // ---------- report ----------
    public String generateStatusReport() {
        StringBuilder sb = new StringBuilder("=== COLONY STATUS ===\n");
        sb.append("Environment: ").append(environment).append('\n');
        sb.append("Agents: ").append(agents.size())
                .append("  Facilities: ").append(facilities.size()).append('\n');

        sb.append("Resources (operational only):\n");
        for (ResourceType t : ResourceType.values()) {
            sb.append("  ").append(t).append(": ").append(getResourceTotal(t)).append('\n');
        }

        sb.append(missionManager.getStatusReport()).append('\n');
        return sb.toString();
    }
}