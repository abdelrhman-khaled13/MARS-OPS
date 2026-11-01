package resources;

import common.OperationResult;
import common.ResourceRequest;
import common.ResourceType;
import facilities.Facility;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ResourceManager {
    private final List<Facility> facilities;

    public ResourceManager(List<Facility> facilities) {
        this.facilities = Objects.requireNonNull(facilities, "facilities");
    }

    public int getTotal(ResourceType type) {
        Objects.requireNonNull(type, "type");
        int total = 0;
        for (Facility facility : facilities) {
            if (facility.isOperational()) {
                int amount = facility.getResource(type);
                if (amount > Integer.MAX_VALUE - total) {
                    return Integer.MAX_VALUE;
                }
                total += amount;
            }
        }
        return total;
    }

    public boolean canConsume(List<ResourceRequest> requests) {
        final Map<ResourceType, Integer> totals;
        try {
            totals = aggregate(requests);
        } catch (IllegalArgumentException | ArithmeticException e) {
            return false;
        }

        for (Map.Entry<ResourceType, Integer> entry : totals.entrySet()) {
            if (getTotal(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    public OperationResult consume(List<ResourceRequest> requests) {
        final Map<ResourceType, Integer> totals;
        try {
            totals = aggregate(requests);
        } catch (IllegalArgumentException e) {
            return OperationResult.failure(e.getMessage());
        } catch (ArithmeticException e) {
            return OperationResult.failure("Combined resource quantity is too large.");
        }

        // Validate every resource before changing any facility inventory.
        for (Map.Entry<ResourceType, Integer> entry : totals.entrySet()) {
            int available = getTotal(entry.getKey());
            if (available < entry.getValue()) {
                return OperationResult.failure("Insufficient " + entry.getKey()
                        + ": required " + entry.getValue() + ", available " + available + ".");
            }
        }

        List<Facility> operationalFacilities = new ArrayList<>();
        for (Facility facility : facilities) {
            if (facility.isOperational()) {
                operationalFacilities.add(facility);
            }
        }
        operationalFacilities.sort(Comparator.comparing(Facility::getId,
                Comparator.nullsFirst(String::compareTo)));

        for (Map.Entry<ResourceType, Integer> entry : totals.entrySet()) {
            int remaining = entry.getValue();
            for (Facility facility : operationalFacilities) {
                if (remaining == 0) {
                    break;
                }
                int inFacility = facility.getResource(entry.getKey());
                if (inFacility > 0) {
                    int amount = Math.min(inFacility, remaining);
                    facility.deductResource(entry.getKey(), amount);
                    remaining -= amount;
                }
            }
        }
        return OperationResult.success();
    }

    private Map<ResourceType, Integer> aggregate(List<ResourceRequest> requests) {
        Map<ResourceType, Integer> totals = new LinkedHashMap<>();
        if (requests == null) {
            return totals;
        }
        for (ResourceRequest request : requests) {
            if (request == null) {
                throw new IllegalArgumentException("A resource request list cannot contain null.");
            }
            ResourceType type = request.getType();
            int quantity = request.getQuantity();
            totals.put(type, Math.addExact(totals.getOrDefault(type, 0), quantity));
        }
        return totals;
    }
}
