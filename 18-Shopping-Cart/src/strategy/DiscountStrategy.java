package strategy;

import model.ShoppingCart;

public interface DiscountStrategy {
    double calculateDiscount(ShoppingCart cart);
    String getDescription();
}
