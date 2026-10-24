package agents;

public class Drone extends Robot {

    public Drone(int id, String name, String location, int battery) {
        super(id, name, location, battery);
    }

    @Override
    public boolean canPerform(String missionType) {
        return missionType.equals("EXPLORATION") || missionType.equals("RESCUE");
    }

    @Override
    public boolean canHandle(String emergencyType) {
        return false;
    }
}