package strategy;

import java.time.LocalTime;

/**
 * Surge pricing strategy for peak hours
 * Applies multiplier during lunch (12-2 PM) and dinner (7-10 PM)
 */
public class SurgePricing implements DeliveryPricingStrategy {
    private static final double BASE_FARE = 20.0;
    private static final double PER_KM_CHARGE = 8.0;
    private static final double SURGE_MULTIPLIER = 1.5;
    
    @Override
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        double baseCharge = BASE_FARE + (distance * PER_KM_CHARGE);
        
        // Check if it's peak hours
        if (isPeakHour(orderTime)) {
            baseCharge *= SURGE_MULTIPLIER;
        }
        
        return Math.round(baseCharge * 100.0) / 100.0;
    }
    
    private boolean isPeakHour(LocalTime time) {
        int hour = time.getHour();
        // Lunch: 12-2 PM, Dinner: 7-10 PM
        return (hour >= 12 && hour < 14) || (hour >= 19 && hour < 22);
    }
    
    @Override
    public String getStrategyName() {
        return "Surge Pricing (" + SURGE_MULTIPLIER + "x during peak hours: 12-2 PM, 7-10 PM)";
    }
}

