
package facilities;


public class LifeSupportSystem {
    private boolean operational;
    private int damageLevel;             
    private double filtrationEfficiency; 

    public LifeSupportSystem() {
        this.operational = true;
        this.damageLevel = 0;
        this.filtrationEfficiency = 0.98;
    }

    public boolean isOperational() {
        return operational;
    }

    public int getDamageLevel() {
        return damageLevel;
    }

    public double getFiltrationEfficiency() {
        return filtrationEfficiency;
    }

    public void setFiltrationEfficiency(double filtrationEfficiency) {
        this.filtrationEfficiency = Math.max(0.0, Math.min(1.0, filtrationEfficiency));
    }

    
    public void damaged(int severity) {
        this.damageLevel = Math.min(100, this.damageLevel + severity);
        if (this.damageLevel >= 100) {
            this.operational = false;
        }
    }

    
    public void repair() {
        this.damageLevel = 0;
        this.operational = true;
    }

    @Override
    public String toString() {
        return "LifeSupportSystem [Operational: " + operational
                + ", Damage: " + damageLevel
                + ", Efficiency: " + (filtrationEfficiency * 100) + "%]";
    }
}
