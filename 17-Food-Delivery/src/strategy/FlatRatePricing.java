package strategy;

import java.time.LocalTime;

/**
 * Flat rate delivery pricing strategy
 * Fixed delivery charge regardless of distance
 */
public class FlatRatePricing implements DeliveryPricingStrategy {
    private static final double FLAT_RATE = 40.0;
    
    @Override
    public double calculateDeliveryCharge(double distance, LocalTime orderTime) {
        return FLAT_RATE;
    }
    
    @Override
    public String getStrategyName() {
        return "Flat Rate Pricing ($" + FLAT_RATE + " for all deliveries)";
    }
}

