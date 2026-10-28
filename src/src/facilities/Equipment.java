
package facilities;


public class Equipment {
    private final String id;
    private final String name;
    private boolean operational;

    public Equipment(String id, String name) {
        this.id = id;
        this.name = name;
        this.operational = true;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isOperational() {
        return operational;
    }

   
    public void damaged() {
        this.operational = false;
    }

    
    public void repair() {
        this.operational = true;
    }

    @Override
    public String toString() {
        return name + " [ID: " + id + ", Operational: " + operational + "]";
    }
}
