package agents;

public class Scientist extends CrewMember {

    public Scientist(int id, String name, String location) {
        super(id, name, location);
    }

    @Override
    public boolean canPerform(String missionType) {
        return missionType.equals("RESEARCH") || missionType.equals("EXPLORATION");
    }

    @Override
    public boolean canHandle(String emergencyType) {
        return false;
    }
}