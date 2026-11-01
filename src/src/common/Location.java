package common;

import java.util.Objects;

public final class Location {

    private static final Location SURFACE = new Location(null);

    private final String facilityId;   // null means the Martian surface

    private Location(String facilityId) {
        this.facilityId = facilityId;
    }

    public static Location surface() {
        return SURFACE;
    }

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
    public String toString() {
        return facilityId == null ? "Martian surface" : "Facility " + facilityId;
    }
}