
package facilities;
import common.Location;


public class Laboratory extends Facility {
    private String researchFocus;

    public Laboratory(String id, String name, Location location, int capacity, String researchFocus) {
        super(id, name, location, capacity);
        this.researchFocus = researchFocus;
    }

    public String getResearchFocus() {
        return researchFocus;
    }

    public void setResearchFocus(String researchFocus) {
        this.researchFocus = researchFocus;
    }

    @Override
    public String toString() {
        return super.toString() + " | Research Focus: " + researchFocus;
    }
}
