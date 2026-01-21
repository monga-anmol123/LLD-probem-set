package strategy;

import model.ShoppingCart;

public class FixedAmountDiscount implements DiscountStrategy {
    private final double amount;
    private final String description;
    
    public FixedAmountDiscount(double amount) {
        this.amount = amount;
        this.description = String.format("$%.2f off", amount);
    }
    
    @Override
    public double calculateDiscount(ShoppingCart cart) {
        // Don't exceed cart subtotal
        return Math.min(amount, cart.getSubtotal());
    }
    
    @Override
    public String getDescription() {
        return description;
    }
    
    public double getAmount() {
        return amount;
    }
}
