package strategy;

import model.ShoppingCart;
import model.CartItem;

public class BOGODiscount implements DiscountStrategy {
    private final String targetSKU;
    private final int buy;
    private final int getFree;
    private final String description;
    
    public BOGODiscount(String targetSKU, int buy, int getFree) {
        this.targetSKU = targetSKU;
        this.buy = buy;
        this.getFree = getFree;
        this.description = String.format("Buy %d Get %d Free on %s", buy, getFree, targetSKU);
    }
    
    @Override
    public double calculateDiscount(ShoppingCart cart) {
        CartItem item = cart.getItemBySKU(targetSKU);
        if (item == null) {
            return 0.0;
        }
        
        int quantity = item.getQuantity();
        int sets = quantity / (buy + getFree);
        int freeItems = sets * getFree;
        
        return freeItems * item.getProduct().getPrice();
    }
    
    @Override
    public String getDescription() {
        return description;
    }
}
