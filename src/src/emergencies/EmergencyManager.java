package emergencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EmergencyManager {
    private final List<Emergency> activeEmergencies;
    private final List<Emergency> emergencyHistory;

    public EmergencyManager() {
        this.activeEmergencies = new ArrayList<>();
        this.emergencyHistory = new ArrayList<>();
    }

    public void triggerEmergency(Emergency emergency, List<Responder> colonyAgents, ColonyContext colony) {
        activeEmergencies.add(emergency);
        boolean success = emergency.handle(colonyAgents, colony);
        
        activeEmergencies.remove(emergency);
        emergencyHistory.add(emergency);

        System.out.println("\n--- EMERGENCY LOG [" + emergency.getId() + "] ---");
        System.out.println("Type: " + emergency.getType());
        System.out.println("Location: " + emergency.getLocation());
        System.out.println("Status: " + emergency.getStatus());
        if (!success) {
            System.out.println("Reason: " + emergency.getFailureReason());
        } else {
            System.out.println("Dispatched Responders: " + emergency.getDispatchedResponders().size());
        }
        System.out.println("-----------------------------------\n");
    }

    public List<Emergency> getEmergencyHistory() {
        return Collections.unmodifiableList(emergencyHistory);
    }

    public List<Emergency> getActiveEmergencies() {
        return Collections.unmodifiableList(activeEmergencies);
    }
}