# CSE 321 - Programming II
Part of Lab 3 submission: An autonomous Mars colony management and simulator system designed in Java.

## Project Structure

* **`src/`**
  * **`agents/`** - Personnel (human astronauts) and robotic units hierarchy ([details](src/agents/README.md)).
  * **`colony/`** - Colony aggregation facade (`MarsColony`) and time simulation engine (`Simulation`).
  * **`common/`** - Shared enumerations (`Capability`, `ResourceType`, `EmergencyType`), value objects (`Location`, `OperationResult`), and interfaces ([details](src/common/README.md)).
  * **`emergencies/`** - Crisis event handling module (`OxygenLeak`, `Fire`, `RoverBreakdown`, `MedicalEmergency`) ([details](src/emergencies/README.md)).
  * **`environment/`** - Weather condition regulation engine (`Normal`, `DustStorm`, `CommunicationDisruption`) ([details](src/environment/README.md)).
  * **`facilities/`** - Colony structural units, life support systems, and local resource inventories ([details](src/facilities/README.md)).
  * **`missions/`** - Mission lifecycle state machine, role matching engine, and execution manager ([details](src/missions/README.md)).
  * **`resources/`** - Transactional resource aggregation and atomic allocation policy manager ([details](src/resources/README.md)).
  * **`Main.java`** - Demonstration driver executing scenarios (successful missions, rejections, resource shortages, emergency handling, and weather effects).
* **`Docs/`** - System architecture UML diagrams (Class, Object, State Machine, Sequence, and Use Case diagrams).

---

The simulator manages colony personnel, robotic units, facilities, resources, active missions, and emergency handling workflows across simulated time steps.

---

## How to Compile & Run

### Prerequisites
* **Java Development Kit (JDK) 8 or higher** installed.

### 1. Compile the Project
Open a terminal inside the project root directory and navigate to the source root:

```bash
cd src
```

Compile all Java files into an output directory (`out`):

```bash
javac -d ../../out $(find . -name "*.java")
```

*(On Windows PowerShell, use: `javac -d ..\..\out (Get-ChildItem -Recurse -Filter *.java).FullName`)*

### 2. Run the Application
Navigate back to the project root directory and run the `Main` class:

```bash
cd ../..
java -cp out Main
```
