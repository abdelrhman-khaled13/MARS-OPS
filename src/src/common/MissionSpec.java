package common;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;


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

    public static MissionSpec of(String name, Capability first, Capability... rest) {
        EnumSet<Capability> set = EnumSet.of(first, rest);
        return new MissionSpec(name, set);
    }

    public String getName() {
        return name;
    }

    public Set<Capability> getRequiredCapabilities() {
        return requiredCapabilities;
    }

    public boolean requires(Capability capability) {
        return requiredCapabilities.contains(capability);
    }

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