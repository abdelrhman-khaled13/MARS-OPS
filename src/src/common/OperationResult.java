package common;


public final class OperationResult {

    private final boolean success;
    private final String message;

    private OperationResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static OperationResult success(String message) {
        return new OperationResult(true, message == null ? "" : message);
    }

    public static OperationResult success() {
        return new OperationResult(true, "OK");
    }

    /** @param reason must say why; a failure without a reason is a bug */
    public static OperationResult failure(String reason) {
        if (reason == null || reason.trim().isEmpty()) {
            throw new IllegalArgumentException("A failure must carry a reason.");
        }
        return new OperationResult(false, reason);
    }

    public boolean isSuccess() {
        return success;
    }

    public boolean isFailure() {
        return !success;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return (success ? "SUCCESS: " : "FAILURE: ") + message;
    }
}