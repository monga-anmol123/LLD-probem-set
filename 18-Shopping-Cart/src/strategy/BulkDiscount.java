package strategy;

import model.ShoppingCart;

public class BulkDiscount implements DiscountStrategy {
    private final int minQuantity;
    private final double percentage;
    private final String description;
    
    public BulkDiscount(int minQuantity, double percentage) {
        this.minQuantity = minQuantity;
        this.percentage = percentage;
        this.description = String.format("%.0f%% off when buying %d+ items", 
            percentage, minQuantity);
    }
    
    @Override
    public double calculateDiscount(ShoppingCart cart) {
        int totalItems = cart.getTotalItemCount();
        
        if (totalItems >= minQuantity) {
            return cart.getSubtotal() * (percentage / 100.0);
        }
        
        return 0.0;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
