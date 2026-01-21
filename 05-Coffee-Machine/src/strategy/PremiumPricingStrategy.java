package strategy;

import model.Coffee;

public class PremiumPricingStrategy implements PricingStrategy {
    private static final double PREMIUM_MULTIPLIER = 1.25; // 25% markup
    
    @Override
    public double calculatePrice(Coffee coffee) {
        // Start with base price for coffee type
        double price = coffee.getType().getBasePrice();
        
        // Apply size multiplier
        price *= coffee.getSize().getPriceMultiplier();
        
        // Add premium milk cost (higher for premium pricing)
        price += coffee.getMilkType().getAdditionalCost() * 1.5;
        
        // Add whipped cream cost
        if (coffee.hasWhippedCream()) {
            price += 1.00; // More expensive than base pricing
        }
        
        // Add extra shot cost
        if (coffee.hasExtraShot()) {
            price += 1.50; // More expensive than base pricing
        }
        
        // Apply premium multiplier
        price *= PREMIUM_MULTIPLIER;
        
        return Math.round(price * 100.0) / 100.0; // Round to 2 decimal places
    }
}


