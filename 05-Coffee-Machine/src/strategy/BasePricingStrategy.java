package strategy;

import model.Coffee;

public class BasePricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(Coffee coffee) {
        // Start with base price for coffee type
        double price = coffee.getType().getBasePrice();
        
        // Apply size multiplier
        price *= coffee.getSize().getPriceMultiplier();
        
        // Add premium milk cost
        price += coffee.getMilkType().getAdditionalCost();
        
        // Add whipped cream cost
        if (coffee.hasWhippedCream()) {
            price += 0.75;
        }
        
        // Add extra shot cost
        if (coffee.hasExtraShot()) {
            price += 1.00;
        }
        
        // Sugar is free!
        
        return Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
    }
}


