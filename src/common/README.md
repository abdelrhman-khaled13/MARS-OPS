# Common Package

## Purpose
Provides global domain enumerations, core value objects, and abstraction interfaces shared across all subsystems in the MARS-OPS colony simulator.

## Key Components

### 1. Enumerations
* **`Capability`**: Operational skills and attributes assigned to agents (e.g., `SURFACE_TRAVEL`, `REPAIR`, `FIRE_RESPONSE`).
* **`ResourceType`**: Consumable colony inventory types (`OXYGEN`, `WATER`, `FOOD`, `FUEL`, `BATTERY_POWER`, `MEDICAL_SUPPLIES`, `SPARE_PARTS`).
* **`EmergencyType`**: Maps crises to required responder capabilities.

### 2. Value Objects
* **`Location`**: Immutable object representing either the Martian surface (`Location.surface()`) or a specific facility (`Location.inFacility(id)`).
* **`OperationResult`**: Standardized outcome wrapper returning a boolean `success` state and descriptive message.
* **`ResourceRequest`**: Immutable pair defining a `ResourceType` and requested integer quantity.
* **`MissionSpec`**: Defines target capability prerequisites for mission roles.

### 3. Core Interfaces
* **`Responder`**: Contract implemented by agents for emergency qualification and availability queries.
* **`ColonyContext`**: Interface allowing emergencies and missions to inspect and mutate colony state (facilities, resources, agent health) without direct coupling to `MarsColony`.