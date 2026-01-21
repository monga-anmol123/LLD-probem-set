package enums;

public enum Size {
    SMALL(0.8, "8 oz"),
    MEDIUM(1.0, "12 oz"),
    LARGE(1.5, "16 oz");
    
    private final double multiplier; // For both ingredients and price
    private final String volume;
    
    Size(double multiplier, String volume) {
        this.multiplier = multiplier;
        this.volume = volume;
    }
    
    public double getMultiplier() {
        return multiplier;
    }
    
    public String getVolume() {
        return volume;
    }
    
    public double getPriceMultiplier() {
        return multiplier;
    }
    
    public int getIngredientMultiplier() {
        return (int) Math.ceil(multiplier);
    }
}


