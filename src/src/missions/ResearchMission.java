package missions;

import agents.Agent;
import agents.CrewMember;
import common.Capability;
import common.Location;
import common.OperationResult;
import common.ResourceRequest;
import facilities.Equipment;
import facilities.Laboratory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Scientists perform an experiment in a laboratory (spec section 6).
 *
 * <p>Needs scientists with a particular skill, an operational laboratory, working scientific
 * equipment and enough resources. If the laboratory stops being operational while the
 * experiment runs (e.g. after a fire, spec 9.3) the mission fails on the next check.</p>
 */
public class ResearchMission extends Mission {

    public static final int DEFAULT_DURATION = 4;

    private final String experiment;
    private final Laboratory laboratory;
    private final List<Equipment> scientificEquipment;

    /**
     * @param experiment          name of the experiment
     * @param duration            time units
     * @param scientists          the researchers
     * @param laboratory          the laboratory used (must be operational)
     * @param scientificEquipment equipment that must be operational (may be empty)
     * @param resources           colony resources consumed on acceptance (may be empty)
     * @param requiredSkill       skill every scientist must have, or {@code null}
     */
    public ResearchMission(String experiment, int duration, List<Agent> scientists, Laboratory laboratory,
                           List<Equipment> scientificEquipment, List<ResourceRequest> resources,
                           String requiredSkill) {
        super("Research: " + experiment, duration, scientists, buildRoles(requiredSkill), resources, 0);
        if (laboratory == null) {
            throw new IllegalArgumentException("A research mission needs a laboratory.");
        }
        this.experiment = experiment;
        this.laboratory = laboratory;
        this.scientificEquipment = scientificEquipment == null
                ? Collections.<Equipment>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(scientificEquipment));
    }

    /** Convenience: default duration (4), no specific skill. */
    public ResearchMission(String experiment, List<Agent> scientists, Laboratory laboratory,
                           List<Equipment> scientificEquipment, List<ResourceRequest> resources) {
        this(experiment, DEFAULT_DURATION, scientists, laboratory, scientificEquipment, resources, null);
    }

    private static List<Role> buildRoles(String requiredSkill) {
        return Collections.singletonList(
                new Role("scientist", CrewMember.class, 1, Role.UNLIMITED, requiredSkill, Capability.SCIENTIFIC_RESEARCH));
    }

    public String getExperiment() { return experiment; }

    public Laboratory getLaboratory() { return laboratory; }

    public List<Equipment> getScientificEquipment() { return scientificEquipment; }

    @Override
    public String getOperationName() { return "Research Mission"; }

    @Override
    protected OperationResult checkMissionConditions() {
        if (!laboratory.isOperational()) {
            return OperationResult.failure("Laboratory " + laboratory.getName() + " is not operational.");
        }
        for (Equipment equipment : scientificEquipment) {
            if (!equipment.isOperational()) {
                return OperationResult.failure("Equipment failure: " + equipment.getName() + " is not operational.");
            }
        }
        return OperationResult.success("Laboratory and scientific equipment are operational.");
    }

    @Override
    protected Location getWorkSite() { return Location.inFacility(laboratory.getId()); }
}