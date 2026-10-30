package common;

import java.util.Objects;

/**
 * An immutable "I need this much of this resource" value, e.g. 2 SPARE_PARTS.
 *
 * <p>Missions and emergencies describe what they need with these; the ResourceManager decides
 * whether the colony can supply them. The quantity is always strictly positive, so a request
 * can never be used to add resources by accident.</p>
 */
public final class ResourceRequest {

    private final ResourceType type;
    private final int quantity;

    public ResourceRequest(ResourceType type, int quantity) {
        if (type == null) {
            throw new IllegalArgumentException("A resource request needs a resource type.");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Requested quantity must be positive: " + quantity);
        }
        this.type = type;
        this.quantity = quantity;
    }

    public ResourceType getType() {
        return type;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceRequest)) {
            return false;
        }
        ResourceRequest other = (ResourceRequest) o;
        return quantity == other.quantity && type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, quantity);
    }

    @Override
    public String toString() {
        return quantity + " x " + type;
    }
}