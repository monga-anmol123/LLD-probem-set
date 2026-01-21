package strategy;

import enums.RideType;

public interface PricingStrategy {
    double calculateFare(double distanceKm, double durationHours, RideType rideType);
}

