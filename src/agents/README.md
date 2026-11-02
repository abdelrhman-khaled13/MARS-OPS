# Agents Package

## Decisions & Rules

### 1. Abstract Base Hierarchy (`Agent`)

* **Unified Emergency Integration:** `Agent` implements `common.Responder`, allowing emergency handlers to query operational traits (`isHuman()`, `isRobotic()`, `canHandleEmergency()`) without exposing concrete implementations.

* **Polymorphic Capability Matching:** Each concrete subclass explicitly defines an immutable `Set<Capability>`. Mission eligibility is determined dynamically via `agent.canPerform(MissionSpec)`, enforcing strict decoupling between concrete agent types and mission roles.

* **State Lifecycle:**

  * `health`: Bounded integer ($0 \text{ to } 100$). An agent with `health == 0` is unusable.

  * `onMission`: Boolean flag managed during mission lifecycles.

  * `isAvailable()`: Returns `true` only if `isUsable() && !isOnMission()`.

### 2. Human Personnel (`CrewMember`)

* **Fatigue Tracking:** Adds a `fatigue` level ($0 \text{ to } 100$). A human crew member with `fatigue == 100` is considered exhausted and reports `isAvailable() == false`.

* **Qualitative Skills:** Holds a dynamically mutable list of unique skill strings (e.g., `"Geology"`) checked by specific mission roles requiring specialized qualifications.

### 3. Automated Units (`Robot`)

* **Battery Dependency:** Tracks `battery` power ($0 \text{ to } 100$). Any robot with `battery == 0` reports `isAvailable() == false`.

* **Battery Consumption:** Robot battery levels are checked against mission minimum floors before dispatch and can be drained or recharged by simulation operations.

## Concrete Agent Capabilities

| Agent Type | Classification | Capabilities | Primary Role / Special Features | 
| ----- | ----- | ----- | ----- | 
| **`Scientist`** | Human | `SCIENTIFIC_RESEARCH` | Laboratory research & planetary analysis | 
| **`Engineer`** | Human | `REPAIR`, `FIRE_RESPONSE` | Equipment/facility repairs & fire suppression | 
| **`MedicalOfficer`** | Human | `MEDICAL_TREATMENT`, `FIRE_RESPONSE`, `RESCUE` | Patient treatment & emergency rescue operations | 
| **`RepairRobot`** | Robotic | `REPAIR`, `FACILITY_OPERATION`, `FIRE_RESPONSE` | Internal facility repairs & automated fire fighting | 
| **`ExplorationRover`** | Robotic | `SURFACE_TRAVEL`, `CARRY_EQUIPMENT` | Surface traversal & mission gear transport | 
| **`Drone`** | Robotic | `AERIAL_RECONNAISSANCE` | Aerial reconnaissance & high-altitude scouting | 

## Availability Rules Summary

For an agent to be considered **available** for a mission or emergency dispatch:

`Available` = `health > 0` and `not onMission` and `fatigue < 100` and `battery > `0
