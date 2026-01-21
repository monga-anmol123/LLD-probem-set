package observer;

import model.Product;

public class CartNotificationObserver implements CartObserver {
    private final String observerName;
    
    public CartNotificationObserver(String observerName) {
        this.observerName = observerName;
    }
    
    @Override
    public void onItemAdded(Product product, int quantity) {
        System.out.printf("🔔 [%s] Added %d x %s to cart\n", 
            observerName, quantity, product.getName());
    }
    
    @Override
    public void onItemRemoved(Product product) {
        System.out.printf("🔔 [%s] Removed %s from cart\n", 
            observerName, product.getName());
    }
    
    @Override
    public void onQuantityUpdated(Product product, int oldQuantity, int newQuantity) {
        System.out.printf("🔔 [%s] Updated %s quantity: %d → %d\n",
            observerName, product.getName(), oldQuantity, newQuantity);
    }
    
    @Override
    public void onCartCleared() {
        System.out.printf("🔔 [%s] Cart cleared\n", observerName);
    }
    
    @Override
    public void onPriceChanged(double oldTotal, double newTotal) {
        System.out.printf("🔔 [%s] Cart total changed: $%.2f → $%.2f\n",
            observerName, oldTotal, newTotal);
    }
}
