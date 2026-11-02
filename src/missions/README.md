# Missions Package

## Lifecycle & State Machine

* Missions progress through five distinct states:

[ PENDING ] ──(submit accepted) ──> [ ASSIGNED ] ──(tick start)──> [ IN_PROGRESS ] ──(duration met)──> [ COMPLETED ]
│                                   │                              │
└───(validation failed)─────────────┴──────(condition failed)──────┴──> [ FAILED ]


### State Guard Rules
* **`PENDING`**: Initial unsubmitted state.
* **`ASSIGNED`**: Mission accepted; agents reserved. Resources are checked but not consumed until the first tick.
* **`IN_PROGRESS`**: Active execution across time units. Agents are relocated to the work site.
* **`COMPLETED`**: Reached required duration. Agents returned to starting locations and released.
* **`FAILED`**: Terminal failure state. Agents released immediately. Failed missions cannot be retried.

## Rules & Decisions

### 1. Single Active Mission Rule
* The colony allows **at most one** active mission (`ASSIGNED` or `IN_PROGRESS`) at any time.
* Submitting a new mission while another is active results in immediate rejection and `FAILED` status.

### 2. Dynamic Role & Capability Matching
* Missions define role requirements (`Role`) rather than checking rigid concrete types alone.
* Roles specify minimum/maximum agent counts, required base class (`CrewMember` or `Robot`), required capabilities (`Capability`), and optional crew skills.

### 3. All-or-Nothing Resource Allocation
* Resource requirements are checked at submission time without consumption.
* Actual deduction occurs atomically on the first simulation tick (`ASSIGNED` $\rightarrow$ `IN_PROGRESS`). If resources are missing at start, the mission fails with zero partial resource loss.

### 4. Environmental Sensitivity & Automatic Interruption
* `MissionManager` listens to global `Environment` changes.
* If weather changes (e.g., a `DUST_STORM` begins), active missions requiring surface access fail immediately upon notification.