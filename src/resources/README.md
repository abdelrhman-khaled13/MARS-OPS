# Resources Package

## Rules & Decisions

### 1. Colony-Wide Total Calculation
* Total available quantity of any resource is calculated dynamically as the sum across all **operational** facilities (`isOperational() == true`).
* Resources stored in non-operational or damaged facilities are excluded from totals and cannot be drawn by operations.

### 2. Multi-Facility Resource Allocation
* An operation can draw required resources from multiple facilities if no single facility holds the entire requested amount.

### 3. All-or-Nothing Consumption
* Resource consumption is strictly atomic.
* Pre-flight validation checks every requested resource type against total operational availability before any inventories are modified.
* If any single resource requirement cannot be met, zero resources are deducted across all facilities, and an explicit failure result is returned.

### 4. Deterministic Facility Order Policy
* When drawing resources from multiple facilities, `ResourceManager` sorts operational facilities alphabetically by `facilityId`.
* Facilities with lower IDs (e.g., `C1`, `H1`) supply resources first before higher-ID facilities (e.g., `L1`, `S1`).