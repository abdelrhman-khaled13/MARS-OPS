package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class RepairRobot extends Robot {

    public RepairRobot(int id, String name, Location location, int battery) {
        super(id, name, location, battery);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.REPAIR, Capability.FACILITY_OPERATION, Capability.FIRE_RESPONSE);
    }

}