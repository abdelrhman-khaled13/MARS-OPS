package environment;


public interface EnvironmentSensitive {

    /** Human-readable name used in messages, e.g. "Exploration Mission". */
    String getOperationName();

    /** True if the operation takes place on the Martian surface (blocked by dust storms). */
    boolean requiresSurfaceAccess();

    /** True if the operation depends on communication links (blocked by communication disruption). */
    boolean requiresCommunication();
}