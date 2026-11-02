package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class Drone extends Robot {

    public Drone(int id, String name, Location location, int battery) {
        super(id, name, location, battery);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.AERIAL_RECONNAISSANCE);
    }

}