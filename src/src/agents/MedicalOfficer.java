package agents;

public class MedicalOfficer extends CrewMember {

    public MedicalOfficer(int id, String name, String location) {
        super(id, name, location);
    }

    @Override
    public boolean canPerform(String missionType) {
        return missionType.equals("RESCUE");
    }

    @Override
    public boolean canHandle(String emergencyType) {
        return emergencyType.equals("MEDICAL_EMERGENCY")
                || emergencyType.equals("FIRE");
    }

    public void treat(CrewMember patient, int healAmount) {
        patient.setHealth(patient.getHealth() + healAmount);
    }
}