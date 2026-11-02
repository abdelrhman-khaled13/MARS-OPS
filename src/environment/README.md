# Environment Package

## Decisions & Rules

### 1. Environmental Conditions (`EnvironmentalCondition`)
* **`NORMAL`**: All surface operations and communications are permitted.
* **`DUST_STORM`**: Disallows surface access (`allowsSurfaceOperations() == false`). Prevents surface exploration missions from starting and aborts active surface rover operations.
* **`COMMUNICATION_DISRUPTION`**: Disallows wireless signal transmission (`allowsCommunication() == false` and `allowsRemoteDroneControl() == false`). Prevents remote drone control and communication-dependent operations.

### 2. Polymorphic Constraint Evaluation (`evaluate`)
* Rather than scattering condition checks throughout the codebase, `EnvironmentalCondition.evaluate(EnvironmentSensitive op)` validates any operation implementing `EnvironmentSensitive` against current weather rules.

### 3. Observer Pattern for Instant Re-evaluation
* The `Environment` class maintains a list of state listeners (`Consumer<EnvironmentalCondition>`).
* Whenever the condition changes (e.g., a dust storm strikes), `Environment` immediately notifies all subscribers.
* **Integration**: `MissionManager` subscribes to state shifts and immediately re-evaluates active missions upon weather changes without waiting for the next simulation time tick.

### 4. Timed vs. Open-Ended Events
* Weather conditions can be assigned a specific positive duration (e.g., 3 time ticks) or set as open-ended (`duration = -1`).
* Decrementing time ticks inside `advanceTime()` automatically resets timed conditions back to `NORMAL` when the duration reaches 0.