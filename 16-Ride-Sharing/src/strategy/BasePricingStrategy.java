package strategy;

import enums.RideType;

public class BasePricingStrategy implements PricingStrategy {
    private static final double BASE_FARE = 2.50;
    private static final double PER_KM_RATE = 1.50;
    private static final double PER_MINUTE_RATE = 0.25;
    
    @Override
    public double calculateFare(double distanceKm, double durationHours, RideType rideType) {
        double durationMinutes = durationHours * 60;
        
        // Base fare + distance cost + time cost
        double fare = BASE_FARE;
        fare += distanceKm * PER_KM_RATE;
        fare += durationMinutes * PER_MINUTE_RATE;
        
        // Apply ride type multiplier
        fare *= rideType.getPriceMultiplier();
        
        // Minimum fare
        fare = Math.max(fare, 5.0);
        
        return Math.round(fare * 100.0) / 100.0;
    }
}

