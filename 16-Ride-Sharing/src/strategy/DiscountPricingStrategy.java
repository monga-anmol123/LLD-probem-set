package strategy;

import enums.RideType;

public class DiscountPricingStrategy implements PricingStrategy {
    private final double discountPercentage; // 0.0 to 1.0
    private final PricingStrategy baseStrategy;
    
    public DiscountPricingStrategy(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        this.baseStrategy = new BasePricingStrategy();
    }
    
    @Override
    public double calculateFare(double distanceKm, double durationHours, RideType rideType) {
        double baseFare = baseStrategy.calculateFare(distanceKm, durationHours, rideType);
        double discountedFare = baseFare * (1.0 - discountPercentage);
        
        // Minimum fare even with discount
        discountedFare = Math.max(discountedFare, 3.0);
        
        return Math.round(discountedFare * 100.0) / 100.0;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage * 100;
    }
}

