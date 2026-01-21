package strategy;

import enums.RideType;

public class SurgePricingStrategy implements PricingStrategy {
    private final double surgeMultiplier;
    private final PricingStrategy baseStrategy;
    
    public SurgePricingStrategy(double surgeMultiplier) {
        this.surgeMultiplier = surgeMultiplier;
        this.baseStrategy = new BasePricingStrategy();
    }
    
    @Override
    public double calculateFare(double distanceKm, double durationHours, RideType rideType) {
        double baseFare = baseStrategy.calculateFare(distanceKm, durationHours, rideType);
        double surgeFare = baseFare * surgeMultiplier;
        return Math.round(surgeFare * 100.0) / 100.0;
    }
    
    public double getSurgeMultiplier() {
        return surgeMultiplier;
    }
}

