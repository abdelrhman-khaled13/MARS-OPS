
package resources;
import common.*;
import facilities.Facility;
import java.util.*;


public class ResourceManager {
    private final List<Facility> facilities;

    public ResourceManager(List<Facility> facilities) {
        this.facilities = facilities;
    }

    
    public int getTotal(ResourceType type) {
        int total = 0;
        for (Facility facility : facilities) {
            if (facility.isOperational()) {
                total += facility.getResource(type);
            }
        }
        return total;
    }

    
    public boolean canConsume(ResourceRequest request) {
        if (request == null || request.getRequirements() == null) {
            return true;
        }

        for (Map.Entry<ResourceType, Integer> entry : request.getRequirements().entrySet()) {
            ResourceType type = entry.getKey();
            int needed = entry.getValue();
            if (getTotal(type) < needed) {
                return false;
            }
        }
        return true;
    }

    /**
     * Atomically consumes resources across operational facilities.
     * If any requested resource cannot be fully supplied, NO resources are deducted.
     */
    public OperationResult consume(ResourceRequest request) {
        if (request == null || request.getRequirements() == null || request.getRequirements().isEmpty()) {
            return OperationResult.ok();
        }

        //   (All-or-Nothing check)
        for (Map.Entry<ResourceType, Integer> entry : request.getRequirements().entrySet()) {
            ResourceType type = entry.getKey();
            int needed = entry.getValue();
            int available = getTotal(type);

            if (available < needed) {
                return OperationResult.fail(
                    "Insufficient " + type + ": required " + needed + ", available " + available
                );
            }
        }

        //   Collect & Sort Operational Facilities 
        List<Facility> operationalFacilities = new ArrayList<>();
        for (Facility facility : facilities) {
            if (facility.isOperational()) {
                operationalFacilities.add(facility);
            }
        }
        // Deterministic Allocation Policy: order by facilityId ascending
        operationalFacilities.sort(Comparator.comparing(Facility::getId));

        // --- PHASE 3: Deduction across multiple facilities ---
        for (Map.Entry<ResourceType, Integer> entry : request.getRequirements().entrySet()) {
            ResourceType type = entry.getKey();
            int remainingToDeduct = entry.getValue();

            for (Facility facility : operationalFacilities) {
                if (remainingToDeduct == 0) break;

                int currentInFacility = facility.getResource(type);
                if (currentInFacility > 0) {
                    int deductAmount = Math.min(currentInFacility, remainingToDeduct);
                    facility.deductResource(type, deductAmount);
                    remainingToDeduct -= deductAmount;
                }
            }
        }

        return OperationResult.ok();
    }
}
