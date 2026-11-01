package emergencies;

import common.ColonyContext;
import common.EmergencyType;
import common.Responder;
import common.ResourceType;

import java.util.List;

public class MedicalEmergency extends Emergency {
    private final String patientId;
    private final String facilityId;
    private final double requiredMedicalSupplies;
    private final double healthRestoredAmount;

    public MedicalEmergency(String id, String patientId, String facilityId, double requiredMedicalSupplies, double healthRestoredAmount) {
        super(id, TYPE_MEDICAL_EMERGENCY, facilityId);
        this.patientId = patientId;
        this.facilityId = facilityId;
        this.requiredMedicalSupplies = requiredMedicalSupplies;
        this.healthRestoredAmount = healthRestoredAmount;
    }

    @Override
    public boolean handle(List<Responder> colonyAgents, ColonyContext colony) {
        if (!colony.isFacilityOperational(facilityId)) {
            setUnresolved("Medical facility (" + facilityId + ") is non-operational.");
            return false;
        }

        Responder medicalOfficer = null;
        for (Responder agent : colonyAgents) {
            if (agent.isAvailable() && agent.canHandleEmergency(EmergencyType.valueOf(getType()))) {
                medicalOfficer = agent;
                break;
            }
        }

        if (medicalOfficer == null) {
            setUnresolved("No available Medical Officer.");
            return false;
        }

        if (!colony.hasResource(ResourceType.MEDICAL_SUPPLIES, requiredMedicalSupplies)) {
            setUnresolved("Insufficient medical supplies.");
            return false;
        }

        colony.consumeResource(ResourceType.MEDICAL_SUPPLIES, requiredMedicalSupplies);
        double currentHealth = colony.getEntityHealth(patientId);
        colony.setEntityHealth(patientId, Math.min(100.0, currentHealth + healthRestoredAmount));
        addDispatchedResponder(medicalOfficer);
        setResolved();
        return true;
    }
}