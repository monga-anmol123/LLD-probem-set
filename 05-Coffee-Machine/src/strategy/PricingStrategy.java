package strategy;

import model.Coffee;

public interface PricingStrategy {
    double calculatePrice(Coffee coffee);
}


