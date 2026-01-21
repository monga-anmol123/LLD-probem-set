package enums;

public enum MilkType {
    NONE(0.0, "No milk"),
    WHOLE_MILK(0.0, "Whole milk"),
    SKIM_MILK(0.0, "Skim milk"),
    ALMOND_MILK(0.50, "Almond milk"),
    SOY_MILK(0.50, "Soy milk"),
    OAT_MILK(0.60, "Oat milk");
    
    private final double additionalCost; // Extra charge for premium milk
    private final String displayName;
    
    MilkType(double additionalCost, String displayName) {
        this.additionalCost = additionalCost;
        this.displayName = displayName;
    }
    
    public double getAdditionalCost() {
        return additionalCost;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}


