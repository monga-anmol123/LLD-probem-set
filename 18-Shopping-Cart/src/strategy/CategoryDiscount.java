package strategy;

import model.ShoppingCart;
import model.CartItem;
import enums.ProductCategory;

public class CategoryDiscount implements DiscountStrategy {
    private final ProductCategory category;
    private final double percentage;
    private final String description;
    
    public CategoryDiscount(ProductCategory category, double percentage) {
        this.category = category;
        this.percentage = percentage;
        this.description = String.format("%.0f%% off %s", percentage, category.getDisplayName());
    }
    
    @Override
    public double calculateDiscount(ShoppingCart cart) {
        double discount = 0.0;
        
        for (CartItem item : cart.getItems()) {
            if (item.getProduct().getCategory() == category) {
                discount += item.getSubtotal() * (percentage / 100.0);
            }
        }
        
        return discount;
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
