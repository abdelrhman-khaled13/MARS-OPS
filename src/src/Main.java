import agents.Drone;
import agents.Engineer;
import agents.ExplorationRover;
import agents.MedicalOfficer;
import agents.RepairRobot;
import agents.Scientist;
import colony.MarsColony;
import colony.Simulation;
import common.Location;
import common.OperationResult;
import common.ResourceRequest;
import common.ResourceType;
import emergencies.OxygenLeakEmergency;
import environment.Environment;
import environment.EnvironmentalCondition;
import facilities.Equipment;
import facilities.Habitat;
import facilities.Laboratory;
import facilities.CommunicationStation;
import java.util.Arrays;
import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        Environment environment = new Environment();
        MarsColony colony = new MarsColony(environment);

        Habitat habitat = new Habitat("H1", "Habitat One", Location.inFacility("H1"), 8);
        habitat.addResource(ResourceType.OXYGEN, 300);
        habitat.addResource(ResourceType.FUEL, 20);
        habitat.addResource(ResourceType.SPARE_PARTS, 10);
        habitat.addResource(ResourceType.WATER, 20);
        habitat.addResource(ResourceType.BATTERY_POWER, 20);
        habitat.addResource(ResourceType.MEDICAL_SUPPLIES, 10);
        Laboratory laboratory = new Laboratory("L1", "Research Lab", Location.inFacility("L1"), 6, "Geology");
        laboratory.addResource(ResourceType.WATER, 10);
        CommunicationStation communications = new CommunicationStation(
                "C1", "Communications", Location.inFacility("C1"), 2);
        colony.addFacility(habitat);
        colony.addFacility(laboratory);
        colony.addFacility(communications);

        Scientist scientist = new Scientist(1, "Ada", Location.inFacility("H1"));
        Engineer engineer = new Engineer(2, "Cy", Location.inFacility("H1"));
        MedicalOfficer medic = new MedicalOfficer(3, "Mina", Location.inFacility("H1"));
        RepairRobot repairRobot = new RepairRobot(4, "R-4", Location.inFacility("H1"), 80);
        ExplorationRover rover = new ExplorationRover(5, "Pathfinder", Location.inFacility("H1"), 80);
        Drone drone = new Drone(6, "Scout", Location.inFacility("H1"), 90);
        colony.addAgent(scientist);
        colony.addAgent(engineer);
        colony.addAgent(medic);
        colony.addAgent(repairRobot);
        colony.addAgent(rover);
        colony.addAgent(drone);

        Equipment drill = new Equipment("EQ-1", "Rock Drill");
        laboratory.addEquipment(drill);
        Simulation simulation = new Simulation(colony);

        System.out.println("=== 1. Successful exploration ===");
        System.out.println("Before:\n" + colony.generateStatusReport());
        missions.ExplorationMission exploration = new missions.ExplorationMission("North Ridge",
                Arrays.asList(rover, scientist), Collections.singletonList(drill),
                Collections.singletonList(new ResourceRequest(ResourceType.FUEL, 3)));
        OperationResult submitted = colony.submitMission(exploration);
        System.out.println(submitted);
        simulation.advanceTime(exploration.getDuration());
        System.out.println("After:\n" + colony.generateStatusReport());

        System.out.println("=== 2. Immediate rejection for an unsuitable agent ===");
        missions.ExplorationMission unsuitable = new missions.ExplorationMission("South Ridge",
                Arrays.asList(drone, scientist), Collections.<Equipment>emptyList(),
                Collections.<ResourceRequest>emptyList());
        System.out.println(colony.submitMission(unsuitable));
        System.out.println("State: " + unsuitable.getState() + "; active mission: "
                + colony.getMissionManager().hasActiveMission());

        System.out.println("=== 3. Resource shortage with no partial consumption ===");
        int oxygenBefore = colony.getResourceTotal(ResourceType.OXYGEN);
        missions.ExplorationMission shortage = new missions.ExplorationMission("Far Ridge",
                Arrays.asList(rover, scientist), Collections.<Equipment>emptyList(),
                Collections.singletonList(new ResourceRequest(ResourceType.OXYGEN, 999)));
        System.out.println("Oxygen before: " + oxygenBefore);
        System.out.println(colony.submitMission(shortage));
        System.out.println("Oxygen after: " + colony.getResourceTotal(ResourceType.OXYGEN));
        System.out.println("State: " + shortage.getState());

        System.out.println("=== 4. Oxygen leak response ===");
        int oxygenBeforeLeak = habitat.getResource(ResourceType.OXYGEN);
        colony.triggerEmergency(new OxygenLeakEmergency("EM-O2-1", "H1", 50, 2));
        System.out.println("Habitat oxygen: " + oxygenBeforeLeak + " -> "
                + habitat.getResource(ResourceType.OXYGEN));

        System.out.println("=== 5. Dust storm blocks surface exploration ===");
        environment.setCondition(EnvironmentalCondition.DUST_STORM);
        missions.ExplorationMission stormMission = new missions.ExplorationMission("Storm Ridge",
                Arrays.asList(rover, scientist), Collections.<Equipment>emptyList(),
                Collections.<ResourceRequest>emptyList());
        System.out.println(colony.submitMission(stormMission));
        System.out.println("State: " + stormMission.getState());
        environment.clearCondition();
    }
}
