package enums;

public enum RideType {
    ECONOMY(1.0, "Economy Sedan", 4),
    PREMIUM(1.5, "Premium SUV", 6),
    LUXURY(2.5, "Luxury Car", 4);
    
    private final double priceMultiplier;
    private final String description;
    private final int capacity;
    
    RideType(double priceMultiplier, String description, int capacity) {
        this.priceMultiplier = priceMultiplier;
        this.description = description;
        this.capacity = capacity;
    }
    
    public double getPriceMultiplier() {
        return priceMultiplier;
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getCapacity() {
        return capacity;
    }
}

