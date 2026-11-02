# Facilities Package

## Decisions & Rules

### 1. Composition Relationships
* **`Habitat` $\rightarrow$ `LifeSupportSystem`**: Each `Habitat` owns a `LifeSupportSystem` instance initialized upon construction. If a habitat is destroyed, its life support system is destroyed with it.
* **`Facility` $\rightarrow$ `ResourceInventory`**: Every facility owns a `ResourceInventory` instance managing local stock.

### 2. Operational Impact on Resources
* Only **operational** facilities (`isOperational() == true`) contribute to colony-wide resource availability totals.
* When a facility becomes damaged (`damaged()`), its stored resources remain in its local inventory but are excluded from global calculations by the `ResourceManager` until repaired (`repair()`).

### 3. Equipment Mechanics
* Equipment can be assigned to facilities (`addEquipment()`).
* Equipment maintains an `operational` flag. Missions or emergencies targeting specific equipment inspect `isOperational()`.

### 4. Zero-Floor Resource Protection
* Local inventory deductions via `deductAndClamp()` (e.g., during oxygen leaks) ensure that facility resource quantities never drop below `0`.