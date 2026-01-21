package strategy;

import java.time.LocalTime;

/**
 * Distance-based delivery pricing strategy
 * Base fare + per km charge
 */
public class DistanceBasedPricing implements DeliveryPricingStrategy {
    private static final double BASE_FARE = 20.0;
    private static final double PER_KM_CHARGE = 8.0;
    
    @Override
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        double charge = BASE_FARE + (distance * PER_KM_CHARGE);
        return Math.round(charge * 100.0) / 100.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Distance-Based Pricing (Base: $" + BASE_FARE + " + $" + PER_KM_CHARGE + "/km)";
    }
}

