package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class ExplorationRover extends Robot {

    public ExplorationRover(int id, String name, Location location, int battery) {
        super(id, name, location, battery);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.SURFACE_TRAVEL, Capability.CARRY_EQUIPMENT);
    }


    public void breakDown() {
        setHealth(0);
    }
}