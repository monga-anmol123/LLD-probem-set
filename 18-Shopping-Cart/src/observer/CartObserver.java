package observer;

import model.Product;

public interface CartObserver {
    void onItemAdded(Product product, int quantity);
    void onItemRemoved(Product product);
    void onQuantityUpdated(Product product, int oldQuantity, int newQuantity);
    void onCartCleared();
    void onPriceChanged(double oldTotal, double newTotal);
}
