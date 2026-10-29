package missions;

import agents.Agent;
import agents.CrewMember;
import agents.Robot;
import common.Capability;
import common.Location;
import common.OperationResult;
import common.ResourceRequest;
import facilities.CommunicationStation;
import facilities.Facility;

import java.util.Arrays;
import java.util.List;


public class RescueMission extends Mission {

    public static final int DEFAULT_DURATION = 3;
    public static final int DEFAULT_MINIMUM_BATTERY = 30;

    private final Agent stranded;
    private final Facility recoveryFacility;
    private final CommunicationStation communicationStation;


    public RescueMission(Agent stranded, Facility recoveryFacility, CommunicationStation communicationStation,
                         int duration, List<Agent> rescuers, List<ResourceRequest> resources,
                         int minimumBatteryLevel) {
        super("Rescue of " + (stranded == null ? "?" : stranded.getName()), duration, rescuers, buildRoles(),
                resources, minimumBatteryLevel);
        if (stranded == null || recoveryFacility == null || communicationStation == null) {
            throw new IllegalArgumentException(
                    "A rescue mission needs a stranded entity, a recovery facility and a communication station.");
        }
        if (rescuers != null && rescuers.contains(stranded)) {
            throw new IllegalArgumentException("An agent cannot be part of its own rescue team.");
        }
        this.stranded = stranded;
        this.recoveryFacility = recoveryFacility;
        this.communicationStation = communicationStation;
    }

    /** Convenience: default duration (3) and battery floor. */
    public RescueMission(Agent stranded, Facility recoveryFacility, CommunicationStation communicationStation,
                         List<Agent> rescuers, List<ResourceRequest> resources) {
        this(stranded, recoveryFacility, communicationStation, DEFAULT_DURATION, rescuers, resources,
                DEFAULT_MINIMUM_BATTERY);
    }

    private static List<Role> buildRoles() {
        return Arrays.asList(
                new Role("vehicle", Robot.class, 1, 1, null, Capability.SURFACE_TRAVEL),
                new Role("rescuer", CrewMember.class, 1, Role.UNLIMITED, null, Capability.RESCUE));
    }

    public Agent getStranded() { return stranded; }

    public Facility getRecoveryFacility() { return recoveryFacility; }

    public CommunicationStation getCommunicationStation() { return communicationStation; }

    @Override
    public String getOperationName() { return "Rescue Mission"; }

    @Override
    public boolean requiresSurfaceAccess() { return true; }

    @Override
    public boolean requiresCommunication() { return true; }

    @Override
    protected OperationResult checkMissionConditions() {
        if (!communicationStation.isOperational()) {
            return OperationResult.failure("Communication station " + communicationStation.getName()
                    + " is not operational.");
        }
        if (!recoveryFacility.isOperational()) {
            return OperationResult.failure("Recovery facility " + recoveryFacility.getName()
                    + " is not operational.");
        }
        if (!Location.surface().equals(stranded.getLocation())) {
            return OperationResult.failure(stranded.getName() + " is not stranded on the surface.");
        }
        return OperationResult.success("Rescue preconditions hold.");
    }

    @Override
    protected Location getWorkSite() { return Location.surface(); }

    @Override
    protected void onCompleted() {
        stranded.setLocation(Location.inFacility(recoveryFacility.getId()));
    }
}