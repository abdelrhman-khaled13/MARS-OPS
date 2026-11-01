package agents;

import common.Capability;
import common.Location;

import java.util.EnumSet;
import java.util.Set;

public class MedicalOfficer extends CrewMember {

    public MedicalOfficer(int id, String name, Location location) {
        super(id, name, location);
    }

    @Override
    public Set<Capability> getCapabilities() {
        return EnumSet.of(Capability.MEDICAL_TREATMENT, Capability.FIRE_RESPONSE, Capability.RESCUE);
    }


    public void treat(CrewMember patient, int healAmount) {
        patient.setHealth(patient.getHealth() + healAmount);
    }
}