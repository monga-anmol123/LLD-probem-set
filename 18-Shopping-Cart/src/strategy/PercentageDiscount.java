package strategy;

import model.ShoppingCart;

public class PercentageDiscount implements DiscountStrategy {
    private final double percentage;
    private final String description;
    
    public PercentageDiscount(double percentage) {
        this.percentage = percentage;
        this.description = String.format("%.0f%% off entire cart", percentage);
    }
    
    @Override
    public double calculateDiscount(ShoppingCart cart) {
        return cart.getSubtotal() * (percentage / 100.0);
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public double getPercentage() {
        return percentage;
    }
}
