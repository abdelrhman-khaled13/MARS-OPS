package emergencies;

import java.util.List;

public class RoverBreakdownEmergency extends Emergency {
    private final String roverId;
    private final double requiredSpareParts;
    private final double requiredBatteryPower;

    public RoverBreakdownEmergency(String id, String roverId, String surfaceLocation, double requiredSpareParts, double requiredBatteryPower) {
        super(id, TYPE_ROVER_BREAKDOWN, surfaceLocation);
        this.roverId = roverId;
        this.requiredSpareParts = requiredSpareParts;
        this.requiredBatteryPower = requiredBatteryPower;
    }

    @Override
    public boolean handle(List<Responder> colonyAgents, ColonyContext colony) {
        colony.setEntityDamaged(roverId, true);

        Responder repairAgent = null;
        for (Responder agent : colonyAgents) {
            if (agent.isAvailable() && agent.canHandleEmergency(TYPE_ROVER_BREAKDOWN)) {
                repairAgent = agent;
                break;
            }
        }

        if (repairAgent == null) {
            setUnresolved("No available repair agent capable of fixing the rover.");
            return false;
        }

        if (!colony.hasResource("SPARE_PARTS", requiredSpareParts) || !colony.hasResource("BATTERY", requiredBatteryPower)) {
            setUnresolved("Insufficient spare parts or battery power for repair.");
            return false;
        }

        colony.consumeResource("SPARE_PARTS", requiredSpareParts);
        colony.consumeResource("BATTERY", requiredBatteryPower);
        colony.setEntityDamaged(roverId, false);
        addDispatchedResponder(repairAgent);
        setResolved();
        return true;
    }
}