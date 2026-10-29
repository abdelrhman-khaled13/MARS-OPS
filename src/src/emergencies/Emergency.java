package emergencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Emergency {
    // Status Constants
    public static final String STATUS_PENDING = "PENDING";
    public static final String STATUS_RESOLVED = "RESOLVED";
    public static final String STATUS_UNRESOLVED = "UNRESOLVED";

    // Emergency Type Constants
    public static final String TYPE_OXYGEN_LEAK = "OXYGEN_LEAK";
    public static final String TYPE_FIRE = "FIRE";
    public static final String TYPE_ROVER_BREAKDOWN = "ROVER_BREAKDOWN";
    public static final String TYPE_MEDICAL_EMERGENCY = "MEDICAL_EMERGENCY";

    private final String id;
    private final String type;
    private final String location;
    private String status;
    private String failureReason;
    private final List<Responder> dispatchedResponders;

    public Emergency(String id, String type, String location) {
        this.id = id;
        this.type = type;
        this.location = location;
        this.status = STATUS_PENDING;
        this.failureReason = "";
        this.dispatchedResponders = new ArrayList<>();
    }

    public String getId() { return id; }
    public String getType() { return type; }
    public String getLocation() { return location; }
    public String getStatus() { return status; }
    public String getFailureReason() { return failureReason; }
    public List<Responder> getDispatchedResponders() { 
        return Collections.unmodifiableList(dispatchedResponders); 
    }

    protected void setResolved() {
        this.status = STATUS_RESOLVED;
        this.failureReason = "";
    }

    protected void setUnresolved(String reason) {
        this.status = STATUS_UNRESOLVED;
        this.failureReason = reason;
    }

    protected void addDispatchedResponder(Responder responder) {
        if (responder != null) {
            responder.setLocation(this.location);
            this.dispatchedResponders.add(responder);
        }
    }

    public abstract boolean handle(List<Responder> colonyAgents, ColonyContext colony);
}