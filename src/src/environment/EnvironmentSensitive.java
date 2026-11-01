package environment;


public interface EnvironmentSensitive {
    String getOperationName();

    boolean requiresSurfaceAccess();

    boolean requiresCommunication();
}