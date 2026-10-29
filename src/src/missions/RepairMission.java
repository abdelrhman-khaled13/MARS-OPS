package missions;

import agents.Agent;
import agents.CrewMember;
import agents.Robot;
import common.Capability;
import common.Location;
import common.ResourceRequest;
import common.ResourceType;
import facilities.Equipment;
import facilities.Facility;

import java.util.Arrays;
import java.util.List;


public class RepairMission extends Mission {

    public static final int DEFAULT_DURATION = 2;
    public static final int MINIMUM_BATTERY = 10;

    private final Facility targetFacility;     // exactly one of targetFacility / targetEquipment is set
    private final Equipment targetEquipment;
    private final Facility site;               // where the work happens

    private RepairMission(String targetName, Facility targetFacility, Equipment targetEquipment,
                          Facility site, int duration, List<Agent> agents, List<ResourceRequest> resources) {
        super("Repair of " + targetName, duration, agents, buildRoles(), resources, MINIMUM_BATTERY);
        this.targetFacility = targetFacility;
        this.targetEquipment = targetEquipment;
        this.site = site;
    }

    /** Repairs a damaged facility. */
    public static RepairMission forFacility(Facility facility, int duration,
                                            List<Agent> agents, List<ResourceRequest> resources) {
        require(facility, "facility");
        return new RepairMission(facility.getName(), facility, null, facility, duration, agents, resources);
    }


    public static RepairMission forEquipment(Equipment equipment, Facility host, int duration,
                                             List<Agent> agents, List<ResourceRequest> resources) {
        require(equipment, "equipment");
        require(host, "host facility");
        return new RepairMission(equipment.getName(), null, equipment, host, duration, agents, resources);
    }

    /** The example from spec section 5: 2 spare parts and 5 battery units. */
    public static List<ResourceRequest> defaultResources() {
        return Arrays.asList(
                new ResourceRequest(ResourceType.SPARE_PARTS, 2),
                new ResourceRequest(ResourceType.BATTERY_POWER, 5));
    }

    private static List<Role> buildRoles() {
        return Arrays.asList(
                new Role("engineer", CrewMember.class, 1, Role.UNLIMITED, null, Capability.REPAIR),
                new Role("repair robot", Robot.class, 1, Role.UNLIMITED, null, Capability.REPAIR));
    }

    private static void require(Object value, String what) {
        if (value == null) {
            throw new IllegalArgumentException("A repair mission needs a " + what + ".");
        }
    }

    public Facility getTargetFacility() { return targetFacility; }

    public Equipment getTargetEquipment() { return targetEquipment; }

    @Override
    public String getOperationName() { return "Repair Mission"; }

    // The target is damaged by definition, so no "is it operational?" condition applies here.

    @Override
    protected Location getWorkSite() { return Location.inFacility(site.getId()); }

    @Override
    protected void onCompleted() {
        if (targetFacility != null) {
            targetFacility.repair();
        } else {
            targetEquipment.repair();
        }
    }
}