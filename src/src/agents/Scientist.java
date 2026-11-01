package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class Scientist extends CrewMember {

    public Scientist(int id, String name, Location location) {
        super(id, name, location);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.SCIENTIFIC_RESEARCH);
    }

}