package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class Engineer extends CrewMember {

    public Engineer(int id, String name, Location location) {
        super(id, name, location);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.REPAIR, Capability.FIRE_RESPONSE);
    }

}