package agents;

public class RepairRobot extends Robot {

    public RepairRobot(int id, String name, String location, int battery) {
        super(id, name, location, battery);
    }

    @Override
    public boolean canPerform(String missionType) {
        return missionType.equals("REPAIR");
    }

    @Override
    public boolean canHandle(String emergencyType) {
        return emergencyType.equals("OXYGEN_LEAK")
                || emergencyType.equals("FIRE")
                || emergencyType.equals("ROVER_BREAKDOWN");
    }
}