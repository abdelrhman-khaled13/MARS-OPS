package emergencies;

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

        Responder fireResponder = null;
        Responder medicalOfficer = null;

        for (Responder agent : colonyAgents) {
            if (agent.isAvailable()) {
                if (fireResponder == null && agent.canHandleEmergency(TYPE_FIRE)) {
                    fireResponder = agent;
                } else if (medicalOfficer == null && agent.canHandleEmergency(TYPE_MEDICAL_EMERGENCY)) {
                    medicalOfficer = agent;
                }
            }
        }

        if (fireResponder == null || medicalOfficer == null) {
            setUnresolved("Required fire response and/or medical personnel unavailable.");
            return false;
        }

        if (!colony.hasResource("WATER", requiredWater)) {
            setUnresolved("Insufficient water resources for fire suppression.");
            return false;
        }

        colony.consumeResource("WATER", requiredWater);
        addDispatchedResponder(fireResponder);
        addDispatchedResponder(medicalOfficer);
        colony.setFacilityOperational(labId, true);
        setResolved();
        return true;
    }
}