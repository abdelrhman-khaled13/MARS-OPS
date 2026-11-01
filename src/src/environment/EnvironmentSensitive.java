package environment;


public interface EnvironmentSensitive {
    /** Returns the name of the operation. */
    String getOperationName();

    /** True if the operation takes place on the Martian surface (blocked by dust storms). */
    boolean requiresSurfaceAccess();

    /** True if the operation depends on communication links (blocked by communication disruption). */
    boolean requiresCommunication();
}