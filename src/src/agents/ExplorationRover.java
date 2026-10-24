package agents;

public class ExplorationRover extends Robot {

    public ExplorationRover(int id, String name, String location, int battery) {
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

    public void breakDown() {
        setHealth(0);
    }
}