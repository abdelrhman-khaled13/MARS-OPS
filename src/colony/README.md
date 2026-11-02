# Colony Package

## Rules & Decisions

### 1. Facade & Context Pattern (`MarsColony`)
* `MarsColony` implements `common.ColonyContext`, providing a controlled state mutation interface used by emergency handlers without exposing raw collection fields.
* Wraps all subsystem managers (`ResourceManager`, `MissionManager`, `EmergencyManager`) so callers interact through a single, clean API.

### 2. Dynamic Mission Re-evaluation
* Whenever a facility, equipment item, or agent is damaged via `ColonyContext` mutation methods, `MarsColony` automatically triggers `missionManager.reevaluateActiveMission()`.
* This ensures that any active mission whose required assets become damaged or non-operational fails immediately without waiting for the next clock tick.

### 3. Simulation Time Engine (`Simulation`)
* `Simulation.advanceTime(units)` increments the global clock step-by-step.
* Each tick invokes `colony.tick()`, which sequentially updates the global environment conditions before advancing active missions.

### 4. Status Reporting
* `MarsColony.generateStatusReport()` aggregates environment states, agent counts, operational facility counts, total operational resources, and mission statistics into a single readable output.