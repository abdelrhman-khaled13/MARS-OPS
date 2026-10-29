package missions;

import agents.Agent;
import agents.CrewMember;
import agents.Robot;
import common.Capability;
import common.Location;
import common.MissionSpec;
import common.OperationResult;
import common.ResourceRequest;
import environment.EnvironmentSensitive;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;


public abstract class Mission implements EnvironmentSensitive {

    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(1);

    private final String id;
    private final String name;
    private final int duration;
    private final List<Agent> agents;
    private final List<Role> roles;
    private final List<ResourceRequest> resourceRequirements;
    private final int minimumBatteryLevel;

    private MissionState state = MissionState.PENDING;
    private int elapsed;
    private String failureReason;
    private boolean agentsReserved;
    private final Map<Agent, Location> startLocations = new LinkedHashMap<>();
    private final List<String> eventLog = new ArrayList<>();


    protected Mission(String name, int duration, List<Agent> agents, List<Role> roles,
                      List<ResourceRequest> resourceRequirements, int minimumBatteryLevel) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("A mission needs a name.");
        }
        if (duration <= 0) {
            throw new IllegalArgumentException("Mission duration must be positive: " + duration);
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("A mission needs at least one role.");
        }
        if (minimumBatteryLevel < 0) {
            throw new IllegalArgumentException("Minimum battery level cannot be negative.");
        }
        List<Agent> agentCopy = new ArrayList<>();
        if (agents != null) {
            for (Agent agent : agents) {
                if (agent == null) {
                    throw new IllegalArgumentException("The agent list contains null.");
                }
                agentCopy.add(agent);
            }
        }
        this.id = String.format("MSN-%03d", ID_SEQUENCE.getAndIncrement());
        this.name = name;
        this.duration = duration;
        this.agents = Collections.unmodifiableList(agentCopy);
        this.roles = Collections.unmodifiableList(new ArrayList<>(roles));
        this.resourceRequirements = resourceRequirements == null
                ? Collections.<ResourceRequest>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(resourceRequirements));
        this.minimumBatteryLevel = minimumBatteryLevel;
    }

    // =====================================================================
    //  Public read-only view
    // =====================================================================

    public String getId() { return id; }

    public String getName() { return name; }

    public int getDuration() { return duration; }

    /** Time units of work completed so far. */
    public int getElapsed() { return elapsed; }

    public int getRemaining() { return duration - elapsed; }

    public MissionState getState() { return state; }

    /** Why the mission failed, or {@code null} if it has not failed. */
    public String getFailureReason() { return failureReason; }

    public List<Agent> getAgents() { return agents; }

    public List<Role> getRoles() { return roles; }

    public List<ResourceRequest> getResourceRequirements() { return resourceRequirements; }

    public int getMinimumBatteryLevel() { return minimumBatteryLevel; }

    public boolean involves(Agent agent) { return agents.contains(agent); }

    /** Chronological record of every state change and progress step. */
    public List<String> getEventLog() { return Collections.unmodifiableList(eventLog); }

    // =====================================================================
    //  EnvironmentSensitive: missions are surface/communication free unless they say otherwise
    // =====================================================================

    @Override
    public boolean requiresSurfaceAccess() { return false; }

    @Override
    public boolean requiresCommunication() { return false; }

    // =====================================================================
    //  Subclass hooks
    // =====================================================================

    /**
     * Mission-specific conditions that must hold when the mission starts <i>and on every
     * time unit while it runs</i> (equipment still working, laboratory still operational...).
     * A failure here while IN_PROGRESS is how "equipment failure" fails a mission.
     */
    protected OperationResult checkMissionConditions() {
        return OperationResult.success("No mission-specific conditions.");
    }

    /** Where the work happens. Agents are moved there at start. {@code null} = they stay put. */
    protected Location getWorkSite() { return null; }

    /** Applies the mission's outcome when it completes (repair the target, recover the casualty...). */
    protected void onCompleted() { }

    // =====================================================================
    //  Validation (package-private: called by the MissionManager)
    // =====================================================================

    /**
     * Submission-time check of the agents: every agent must fit a role, every role must be
     * filled, and every agent must be available.
     */
    OperationResult checkAgents() {
        int[] filled = new int[roles.size()];
        Set<Agent> seen = new HashSet<>();

        for (Agent agent : agents) {
            if (!seen.add(agent)) {
                return OperationResult.failure("Agent " + agent.getName() + " was submitted more than once.");
            }
            boolean fitsSomeRole = false;
            boolean placed = false;
            for (int i = 0; i < roles.size() && !placed; i++) {
                Role role = roles.get(i);
                if (role.accepts(agent)) {
                    fitsSomeRole = true;
                    if (filled[i] < role.getMax()) {
                        filled[i]++;
                        placed = true;
                    }
                }
            }
            if (!fitsSomeRole) {
                return OperationResult.failure("Agent " + agent.getName()
                        + " does not possess the required capability (roles: " + describeRoles() + ").");
            }
            if (!placed) {
                return OperationResult.failure("Agent " + agent.getName()
                        + " is surplus: every role it could fill is already full.");
            }
            if (!agent.isAvailable()) {
                return OperationResult.failure("Agent " + agent.getName() + " is not available.");
            }
        }

        for (int i = 0; i < roles.size(); i++) {
            Role role = roles.get(i);
            if (filled[i] < role.getMin()) {
                return OperationResult.failure("No suitable agent for the required role: " + role.describe() + ".");
            }
        }
        return OperationResult.success("All roles are filled by capable, available agents.");
    }


    OperationResult checkConditions(boolean starting) {
        int batteryFloor = starting ? Math.max(minimumBatteryLevel, 1) : 1;
        for (Agent agent : agents) {
            if (!agent.isUsable()) {
                return OperationResult.failure("Agent " + agent.getName() + " is no longer in usable condition.");
            }
            if (agent instanceof Robot) {
                int battery = ((Robot) agent).getBatteryLevel();
                if (battery < batteryFloor) {
                    return OperationResult.failure("Insufficient battery: " + agent.getName()
                            + " has " + battery + " but needs at least " + batteryFloor + ".");
                }
            }
        }
        return checkMissionConditions();
    }

    // =====================================================================
    //  Lifecycle transitions (package-private: driven only by the MissionManager)
    // =====================================================================

    /** PENDING -> ASSIGNED. Reserves every agent. */
    void accept() {
        transitionTo(MissionState.ASSIGNED, "accepted");
        for (Agent agent : agents) {
            agent.setAvailable(false);
        }
        agentsReserved = true;
    }

    /** ASSIGNED -> IN_PROGRESS. Remembers where agents were, then moves them to the work site. */
    void begin() {
        transitionTo(MissionState.IN_PROGRESS, "started");
        Location site = getWorkSite();
        for (Agent agent : agents) {
            startLocations.put(agent, agent.getLocation());
            if (site != null) {
                agent.setLocation(site);
            }
        }
    }

    /**
     * Advances the mission by exactly one time unit.
     *
     * @return true when the required duration has been reached
     */
    boolean advanceOneUnit() {
        if (state != MissionState.IN_PROGRESS) {
            throw new IllegalStateException("Mission " + id + " is " + state + "; only IN_PROGRESS missions progress.");
        }
        elapsed++;
        eventLog.add("Progress " + elapsed + "/" + duration);
        return elapsed >= duration;
    }

    /** IN_PROGRESS -> COMPLETED. Applies the outcome, then releases the agents. */
    void complete() {
        transitionTo(MissionState.COMPLETED, "duration reached");
        onCompleted();
        releaseAgents();
    }


    void fail(String reason) {
        boolean wasPending = (state == MissionState.PENDING);
        transitionTo(MissionState.FAILED, (wasPending ? "rejected: " : "") + reason);
        failureReason = reason;
        releaseAgents();
    }


    private void releaseAgents() {
        if (!agentsReserved) {
            return;
        }
        for (Agent agent : agents) {
            Location origin = startLocations.get(agent);
            if (origin != null && agent.isUsable()) {
                agent.setLocation(origin);
            }
            agent.setAvailable(true);
        }
        agentsReserved = false;
    }

    private void transitionTo(MissionState next, String note) {
        if (!state.canTransitionTo(next)) {
            throw new IllegalStateException("Illegal transition " + state + " -> " + next + " for mission " + id);
        }
        eventLog.add(state + " -> " + next + (note == null ? "" : " (" + note + ")"));
        state = next;
    }

    private String describeRoles() {
        StringBuilder sb = new StringBuilder();
        for (Role role : roles) {
            if (sb.length() > 0) {
                sb.append("; ");
            }
            sb.append(role.describe());
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        String progress = (state == MissionState.IN_PROGRESS) ? " " + elapsed + "/" + duration : "";
        String reason = (state == MissionState.FAILED) ? " - " + failureReason : "";
        return "[" + id + "] " + name + " (" + state + progress + ")" + reason;
    }

    // =====================================================================
    //  Role
    // =====================================================================


    public static final class Role {

        public static final int UNLIMITED = Integer.MAX_VALUE;

        private final String name;
        private final Class<? extends Agent> kind;
        private final Set<Capability> capabilities;
        private final MissionSpec spec;
        private final String requiredSkill;
        private final int min;
        private final int max;

        /**
         * @param name          e.g. "vehicle"
         * @param kind          {@code Human.class}, {@code Robot.class} or {@code Agent.class}
         * @param min           how many agents must fill the role
         * @param max           how many may fill it ({@link #UNLIMITED} for no cap)
         * @param requiredSkill a Human skill the agent must have, or {@code null}
         * @param capabilities  what the agent must be able to do (at least one)
         */
        public Role(String name, Class<? extends Agent> kind, int min, int max,
                    String requiredSkill, Capability... capabilities) {
            if (capabilities == null || capabilities.length == 0) {
                throw new IllegalArgumentException("A role needs at least one capability.");
            }
            if (min < 0 || max < min || max == 0) {
                throw new IllegalArgumentException("Invalid role bounds: min=" + min + ", max=" + max);
            }
            this.name = name;
            this.kind = kind;
            this.capabilities = Collections.unmodifiableSet(EnumSet.copyOf(Arrays.asList(capabilities)));
            this.spec = new MissionSpec(name, this.capabilities);
            this.requiredSkill = requiredSkill;
            this.min = min;
            this.max = max;
        }

        public String getName() { return name; }

        public int getMin() { return min; }

        public int getMax() { return max; }

        public MissionSpec getSpec() { return spec; }

        /** Does this agent fit the role? Asks the agent; never checks a concrete type. */
        boolean accepts(Agent agent) {
            if (!kind.isInstance(agent)) {
                return false;
            }
            if (!agent.canPerform(spec)) {
                return false;
            }
            if (requiredSkill != null) {
                return agent instanceof Human && ((Human) agent).hasSkill(requiredSkill);
            }
            return true;
        }

        String describe() {
            return name + " [" + kind.getSimpleName() + ", needs " + capabilities
                    + (requiredSkill != null ? ", skill '" + requiredSkill + "'" : "") + "]";
        }

        @Override
        public String toString() {
            return describe();
        }
    }
}