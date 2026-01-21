package strategy;

import model.Coffee;

public class DiscountPricingStrategy implements PricingStrategy {
    private final double discountPercentage; // 0.0 to 1.0 (e.g., 0.20 = 20% off)
    private final PricingStrategy basePricing;
    
    public DiscountPricingStrategy(double discountPercentage) {
        this.discountPercentage = discountPercentage;
        this.basePricing = new BasePricingStrategy();
    }
    
    @Override
    public double calculatePrice(Coffee coffee) {
        double basePrice = basePricing.calculatePrice(coffee);
        double discountedPrice = basePrice * (1.0 - discountPercentage);
        return Math.round(discountedPrice * 100.0) / 100.0;
    }
    
    public double getDiscountPercentage() {
        return discountPercentage * 100; // Return as percentage
    }
}


