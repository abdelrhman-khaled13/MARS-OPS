# Emergency Package

## Decisions

* All types (`OXYGEN_LEAK`, `FIRE`, `ROVER_BREAKDOWN`, `MEDICAL_EMERGENCY`) and statuses (`PENDING`, `RESOLVED`, `UNRESOLVED`) use `public static final String` constants in `Emergency.java`.
* Responders are queried via `agent.canHandleEmergency(String type)`.
* `ColonyContext` interface handles all facility states, health, damage, and resource deductions.
* Emergencies do not count against the active mission limit and never interrupt active missions.
* If required resources or responders are missing, zero response resources are spent.

---

## Emergency Rules

### 1. Oxygen Leak (`OxygenLeakEmergency`)
* **Target:** Habitat
* **Required:** 1 Human + 1 Robot + `SPARE_PARTS`
* **Oxygen Deduction Logic:**
  * Deducts `oxygenLostAmount` from habitat immediately when triggered.
  * **Resolved:** Sealing succeeds. Total cost = $1 \times \text{loss}$.
  * **Unresolved:** Applies an additional `oxygenLostAmount` penalty deduction. Total cost = $2 \times \text{loss}$.
  * Oxygen levels never fall below `0.0`.

### 2. Fire (`FireEmergency`)
* **Target:** Laboratory
* **Required:** 1 Fire Personnel + 1 Medical Officer + `WATER`
* **Logic:** Sets lab operational status to `false` on trigger. Restores lab to `true` upon successful resolution.

### 3. Rover Breakdown (`RoverBreakdownEmergency`)
* **Target:** Surface location
* **Required:** 1 Repair Agent + `SPARE_PARTS` + `BATTERY`
* **Logic:** Sets rover state to damaged at breakdown location. Clears damage state upon repair.

### 4. Medical Emergency (`MedicalEmergency`)
* **Target:** Medical Facility
* **Required:** Operational facility + 1 Medical Officer + `MEDICAL_SUPPLIES`
* **Logic:** Increases patient health (max `100.0`) upon successful treatment.

---

## Agent Requirements (`Responder`)

To qualify as a responder, an agent must satisfy:
1. `isAvailable() == true` $\rightarrow$ `!isOnActiveMission() && isUsable()`
2. `canHandleEmergency(emergencyType) == true`