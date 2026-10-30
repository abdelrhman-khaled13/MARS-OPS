package common;

import java.util.Objects;

/**
 * Where an agent is: on the Martian surface, or inside a facility (spec 9.1).
 *
 * <p>Immutable value. A facility is referred to by its id (a String), never by reference, so
 * {@code common} does not depend on the facilities package and no dependency cycle appears.</p>
 */
public final class Location {

    private static final Location SURFACE = new Location(null);

    private final String facilityId;   // null means the Martian surface

    private Location(String facilityId) {
        this.facilityId = facilityId;
    }

    /** The Martian surface (outside every facility). */
    public static Location surface() {
        return SURFACE;
    }

    /** Inside the facility with the given id. */
    public static Location inFacility(String facilityId) {
        if (facilityId == null || facilityId.trim().isEmpty()) {
            throw new IllegalArgumentException("A facility location needs a facility id.");
        }
        return new Location(facilityId);
    }

    public boolean isSurface() {
        return facilityId == null;
    }

    public boolean isInFacility() {
        return facilityId != null;
    }

    /** @throws IllegalStateException if this location is the surface */
    public String getFacilityId() {
        if (facilityId == null) {
            throw new IllegalStateException("The Martian surface is not a facility.");
        }
        return facilityId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location)) {
            return false;
        }
        return Objects.equals(facilityId, ((Location) o).facilityId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(facilityId);
    }

    @Override
    public String toString() {
        return facilityId == null ? "Martian surface" : "Facility " + facilityId;
    }
}