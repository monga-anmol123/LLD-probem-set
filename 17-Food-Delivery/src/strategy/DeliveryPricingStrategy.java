package strategy;

import java.time.LocalTime;

/**
 * Strategy interface for calculating delivery charges
 * Implements Strategy Design Pattern
 */
public interface DeliveryPricingStrategy {
    double calculateDeliveryCharge(double distance, LocalTime orderTime);
    String getStrategyName();
}

