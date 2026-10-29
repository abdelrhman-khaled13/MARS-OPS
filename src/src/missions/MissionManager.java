package missions;

import agents.Agent;
import common.OperationResult;
import environment.Environment;
import resources.ResourceManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


public class MissionManager {

    private final ResourceManager resourceManager;
    private final Environment environment;
    private final List<Mission> missions = new ArrayList<>();   // every submitted mission, in order
    private Mission activeMission;                              // 0..1

    public MissionManager(ResourceManager resourceManager, Environment environment) {
        this.resourceManager = Objects.requireNonNull(resourceManager, "resourceManager");
        this.environment = Objects.requireNonNull(environment, "environment");
        // "What happens to missions already in progress when conditions change?": they are
        // re-checked the instant the condition changes, not only at the next time unit.
        this.environment.addChangeListener(condition -> reevaluateActiveMission());
    }

    // =====================================================================
    //  Submission
    // =====================================================================


    public OperationResult submit(Mission mission) {
        Objects.requireNonNull(mission, "mission");

        if (mission.getState() != MissionState.PENDING) {
            // not touched: a processed mission keeps its state and its reason
            return OperationResult.failure("Mission " + mission.getId() + " was already processed ("
                    + mission.getState() + "). Failed missions are not retried; create a new mission.");
        }
        missions.add(mission);

        if (activeMission != null) {
            return reject(mission, "Another mission is already active: " + activeMission.getId()
                    + " (" + activeMission.getState() + ").");
        }

        OperationResult environmentCheck = environment.check(mission);
        if (!environmentCheck.isSuccess()) {
            return reject(mission, environmentCheck.getMessage());
        }

        OperationResult agentCheck = mission.checkAgents();
        if (!agentCheck.isSuccess()) {
            return reject(mission, agentCheck.getMessage());
        }

        OperationResult conditionCheck = mission.checkConditions(true);
        if (!conditionCheck.isSuccess()) {
            return reject(mission, conditionCheck.getMessage());
        }

        if (!mission.getResourceRequirements().isEmpty()) {
            OperationResult consumption = resourceManager.consume(mission.getResourceRequirements());
            if (!consumption.isSuccess()) {
                return reject(mission, consumption.getMessage());
            }
        }

        mission.accept();
        activeMission = mission;
        return OperationResult.success("Mission " + mission.getId() + " (" + mission.getName() + ") is ASSIGNED.");
    }

    private OperationResult reject(Mission mission, String reason) {
        mission.fail(reason);
        return OperationResult.failure(reason);
    }

    // =====================================================================
    //  Time
    // =====================================================================

    /** Advances all missions by {@code units} time units (one at a time). */
    public void advanceTime(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Cannot advance time by a negative amount: " + units);
        }
        for (int i = 0; i < units && activeMission != null; i++) {
            tick();
        }
    }

    public void advanceTime() {
        advanceTime(1);
    }

    /**
     * One time unit for the active mission:
     * <ol>
     *   <li>re-check everything that could stop it (environment, agents, battery, equipment);</li>
     *   <li>if it is ASSIGNED, start it (&rarr; IN_PROGRESS); the starting unit counts as its first;</li>
     *   <li>advance it by one unit; when the duration is reached it COMPLETES.</li>
     * </ol>
     */
    private void tick() {
        reevaluateActiveMission();
        if (activeMission == null) {
            return;                                   // it failed this unit
        }
        Mission mission = activeMission;
        if (mission.getState() == MissionState.ASSIGNED) {
            mission.begin();
        }
        if (mission.advanceOneUnit()) {
            mission.complete();
            activeMission = null;
        }
    }

    // =====================================================================
    //  Reacting to the outside world
    // =====================================================================

    /**
     * Re-checks the active mission against the environment and its own conditions and fails
     * it if it can no longer proceed. Runs automatically on every time unit and whenever the
     * environment changes. Other packages should call it right after they change something a
     * mission depends on (e.g. a fire makes the laboratory unavailable, a rover breaks down,
     * equipment is damaged). <b>Mark the agent / facility as damaged first, then call this.</b>
     */
    public void reevaluateActiveMission() {
        if (activeMission == null) {
            return;
        }
        Mission mission = activeMission;
        boolean notYetStarted = (mission.getState() == MissionState.ASSIGNED);

        OperationResult environmentCheck = environment.check(mission);
        if (!environmentCheck.isSuccess()) {
            failActive(notYetStarted
                    ? environmentCheck.getMessage()
                    : "Interrupted by environmental change: " + environmentCheck.getMessage());
            return;
        }
        OperationResult conditionCheck = mission.checkConditions(notYetStarted);
        if (!conditionCheck.isSuccess()) {
            failActive(conditionCheck.getMessage());
        }
    }

    /** Fails the active mission for a reason decided elsewhere. */
    public OperationResult failActiveMission(String reason) {
        if (activeMission == null) {
            return OperationResult.failure("There is no active mission.");
        }
        String id = activeMission.getId();
        failActive(reason);
        return OperationResult.success("Mission " + id + " failed: " + reason);
    }

    private void failActive(String reason) {
        Mission mission = activeMission;
        mission.fail(reason);          // releases the agents
        activeMission = null;
    }

    // =====================================================================
    //  Queries (for emergencies, reports and the UI)
    // =====================================================================

    public boolean hasActiveMission() {
        return activeMission != null;
    }

    public Optional<Mission> getActiveMission() {
        return Optional.ofNullable(activeMission);
    }

    /** True if the agent belongs to the active mission (emergency responders must not). */
    public boolean isInvolvedInActiveMission(Agent agent) {
        return activeMission != null && activeMission.involves(agent);
    }

    /** Every mission ever submitted, rejected ones included, oldest first. */
    public List<Mission> getMissions() {
        return Collections.unmodifiableList(missions);
    }

    public List<Mission> getMissions(MissionState state) {
        List<Mission> result = new ArrayList<>();
        for (Mission mission : missions) {
            if (mission.getState() == state) {
                result.add(mission);
            }
        }
        return result;
    }

    /** Text block for the colony status report. */
    public String getStatusReport() {
        StringBuilder sb = new StringBuilder("Missions: ");
        sb.append(activeMission != null ? 1 : 0).append(" active, ")
                .append(getMissions(MissionState.COMPLETED).size()).append(" completed, ")
                .append(getMissions(MissionState.FAILED).size()).append(" failed\n");
        sb.append("  Active mission: ").append(activeMission != null ? activeMission : "none");
        for (Mission mission : missions) {
            if (mission.getState().isFinal()) {
                sb.append("\n  ").append(mission);
            }
        }
        return sb.toString();
    }
}