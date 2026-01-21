package strategy;

import model.Rental;

public interface PricingStrategy {
    double calculatePrice(Rental rental);
    String getStrategyName();
}


