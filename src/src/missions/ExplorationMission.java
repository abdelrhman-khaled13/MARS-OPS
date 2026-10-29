package missions;

import agents.Agent;
import agents.CrewMember;
import agents.Robot;
import common.Capability;
import common.Location;
import common.OperationResult;
import common.ResourceRequest;
import facilities.Equipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class ExplorationMission extends Mission {

    public static final int DEFAULT_DURATION = 3;
    public static final int DEFAULT_MINIMUM_BATTERY = 30;

    private final String region;
    private final List<Equipment> requiredEquipment;


    public ExplorationMission(String region, int duration, List<Agent> agents,
                              List<Equipment> requiredEquipment, List<ResourceRequest> resources,
                              int minimumBatteryLevel, String requiredSkill) {
        super("Exploration of " + region, duration, agents, buildRoles(requiredSkill),
                resources, minimumBatteryLevel);
        this.region = region;
        this.requiredEquipment = requiredEquipment == null
                ? Collections.<Equipment>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(requiredEquipment));
    }

    /** Convenience: default duration (3), default battery floor, no specific skill. */
    public ExplorationMission(String region, List<Agent> agents,
                              List<Equipment> requiredEquipment, List<ResourceRequest> resources) {
        this(region, DEFAULT_DURATION, agents, requiredEquipment, resources, DEFAULT_MINIMUM_BATTERY, null);
    }

    private static List<Role> buildRoles(String requiredSkill) {
        return Arrays.asList(
                new Role("vehicle", Robot.class, 1, 1, null, Capability.SURFACE_TRAVEL),
                new Role("scientist", CrewMember.class, 1, Role.UNLIMITED, requiredSkill, Capability.SCIENTIFIC_RESEARCH));
    }

    public String getRegion() { return region; }

    public List<Equipment> getRequiredEquipment() { return requiredEquipment; }

    @Override
    public String getOperationName() { return "Exploration Mission"; }

    /** Exploration happens on the surface: dust storms block and fail it. */
    @Override
    public boolean requiresSurfaceAccess() { return true; }

    /** All required equipment must work at the start and throughout (equipment failure fails the mission). */
    @Override
    protected OperationResult checkMissionConditions() {
        for (Equipment equipment : requiredEquipment) {
            if (!equipment.isOperational()) {
                return OperationResult.failure("Equipment failure: " + equipment.getName() + " is not operational.");
            }
        }
        return OperationResult.success("Required equipment is operational.");
    }

    /** The exploration team is out on the surface while the mission runs (spec 9.1.2). */
    @Override
    protected Location getWorkSite() { return Location.surface(); }
}