package emergencies;

import common.ColonyContext;
import common.EmergencyType;
import common.Responder;
import common.ResourceType;

import java.util.List;

public class FireEmergency extends Emergency {
    private final String labId;
    private final double requiredWater;

    public FireEmergency(String id, String labId, double requiredWater) {
        super(id, TYPE_FIRE, labId);
        this.labId = labId;
        this.requiredWater = requiredWater;
    }

    @Override
    public boolean handle(List<Responder> colonyAgents, ColonyContext colony) {
        colony.setFacilityOperational(labId, false);
        colony.damageFacilityEquipment(labId);

        Responder fireResponder = null;
        Responder medicalOfficer = null;

        for (Responder agent : colonyAgents) {
            if (agent.isAvailable()) {
                if (fireResponder == null && agent.canHandleEmergency(EmergencyType.valueOf(getType()))) {
                    fireResponder = agent;
                } else if (medicalOfficer == null && agent.canHandleEmergency(EmergencyType.MEDICAL_EMERGENCY)) {
                    medicalOfficer = agent;
                }
            }
        }

        if (fireResponder == null || medicalOfficer == null) {
            setUnresolved("Required fire response and/or medical personnel unavailable.");
            return false;
        }

        if (!colony.hasResource(ResourceType.WATER, requiredWater)) {
            setUnresolved("Insufficient water resources for fire suppression.");
            return false;
        }

        colony.consumeResource(ResourceType.WATER, requiredWater);
        addDispatchedResponder(fireResponder);
        addDispatchedResponder(medicalOfficer);
        colony.setFacilityOperational(labId, true);
        setResolved();
        return true;
    }
}