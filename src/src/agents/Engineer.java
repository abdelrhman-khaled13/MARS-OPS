package agents;

public class Engineer extends CrewMember {

    public Engineer(int id, String name, String location) {
        super(id, name, location);
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