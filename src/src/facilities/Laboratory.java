/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package facilities;

/**
 *
 * @author Lenovo - LOQ
 */
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
}
