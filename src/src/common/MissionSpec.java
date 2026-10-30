package common;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * An immutable description of what a job demands of an agent: a name and the capabilities
 * required.
 *
 * <p>This is the thing an agent is asked about: {@code agent.canPerform(spec)}. It is a plain
 * value class rather than an interface so that {@code agents} can accept it without importing
 * {@code missions}; the dependency arrow therefore points only one way
 * ({@code missions -> agents -> common}).</p>
 *
 * <p><b>Semantics:</b> a spec is satisfied by an agent that has <i>all</i> of its capabilities
 * ({@link #isSatisfiedBy}). A mission with several parts (a vehicle and a scientist) hands each
 * agent the spec for the part it would play, so "all of" is always the right test.</p>
 */
public final class MissionSpec {

    private final String name;
    private final Set<Capability> requiredCapabilities;

    public MissionSpec(String name, Set<Capability> requiredCapabilities) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A mission spec needs a name.");
        }
        if (requiredCapabilities == null || requiredCapabilities.isEmpty()) {
            throw new IllegalArgumentException("A mission spec needs at least one required capability.");
        }
        this.name = name;
        this.requiredCapabilities = Collections.unmodifiableSet(EnumSet.copyOf(requiredCapabilities));
    }

    /** Convenience: {@code MissionSpec.of("vehicle", SURFACE_TRAVEL)}. */
    public static MissionSpec of(String name, Capability first, Capability... rest) {
        EnumSet<Capability> set = EnumSet.of(first, rest);
        return new MissionSpec(name, set);
    }

    public String getName() {
        return name;
    }

    /** Unmodifiable. */
    public Set<Capability> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public boolean requires(Capability capability) {
        return requiredCapabilities.contains(capability);
    }

    /**
     * True if the offered capabilities cover everything this spec requires. Agents implement
     * {@code canPerform} with this: {@code return spec.isSatisfiedBy(capabilities);}
     */
    public boolean isSatisfiedBy(Set<Capability> offered) {
        return offered != null && offered.containsAll(requiredCapabilities);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MissionSpec)) {
            return false;
        }
        MissionSpec other = (MissionSpec) o;
        return name.equals(other.name) && requiredCapabilities.equals(other.requiredCapabilities);
    }

    @Override
    public int hashCode() {
        return 31 * name.hashCode() + requiredCapabilities.hashCode();
    }

    @Override
    public String toString() {
        return name + requiredCapabilities;
    }
}