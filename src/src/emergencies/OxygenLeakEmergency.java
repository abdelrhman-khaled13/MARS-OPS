package emergencies;

import java.util.List;

public class OxygenLeakEmergency extends Emergency {
    private final String habitatId;
    private final double oxygenLostAmount;
    private final double requiredSpareParts;

    public OxygenLeakEmergency(String id, String habitatId, double oxygenLostAmount, double requiredSpareParts) {
        super(id, TYPE_OXYGEN_LEAK, habitatId);
        this.habitatId = habitatId;
        this.oxygenLostAmount = oxygenLostAmount;
        this.requiredSpareParts = requiredSpareParts;
    }

    @Override
    public boolean handle(List<Responder> colonyAgents, ColonyContext colony) {
        colony.deductFacilityResource(habitatId, "OXYGEN", oxygenLostAmount);

        Responder humanResponder = null;
        Responder robotResponder = null;

        for (Responder agent : colonyAgents) {
            if (agent.isAvailable() && agent.canHandleEmergency(TYPE_OXYGEN_LEAK)) {
                if (humanResponder == null && agent.isHuman()) {
                    humanResponder = agent;
                } else if (robotResponder == null && agent.isRobotic()) {
                    robotResponder = agent;
                }
            }
        }

        // Check responder availability
        if (humanResponder == null || robotResponder == null) {
            applyUnresolvedPenalty(colony, "Required responders (1 Human + 1 Robot) are unavailable.");
            return false;
        }

        // Check required spare parts resource
        if (!colony.hasResource("SPARE_PARTS", requiredSpareParts)) {
            applyUnresolvedPenalty(colony, "Insufficient spare parts for repair (" + requiredSpareParts + " required).");
            return false;
        }

        // Repair leak and dispatch responders
        colony.consumeResource("SPARE_PARTS", requiredSpareParts);
        addDispatchedResponder(humanResponder);
        addDispatchedResponder(robotResponder);
        setResolved();
        return true;
    }

    private void applyUnresolvedPenalty(ColonyContext colony, String reason) {
        colony.deductFacilityResource(habitatId, "OXYGEN", oxygenLostAmount);
        setUnresolved(reason);
    }
}