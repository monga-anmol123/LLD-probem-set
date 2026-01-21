package observer;

import model.Product;
import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private final List<CartObserver> observers = new ArrayList<>();
    
    public void attach(CartObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void detach(CartObserver observer) {
        observers.remove(observer);
    }
    
    protected void notifyItemAdded(Product product, int quantity) {
        for (CartObserver observer : observers) {
            observer.onItemAdded(product, quantity);
        }
    }
    
    protected void notifyItemRemoved(Product product) {
        for (CartObserver observer : observers) {
            observer.onItemRemoved(product);
        }
    }
    
    protected void notifyQuantityUpdated(Product product, int oldQuantity, int newQuantity) {
        for (CartObserver observer : observers) {
            observer.onQuantityUpdated(product, oldQuantity, newQuantity);
        }
    }
    
    protected void notifyCartCleared() {
        for (CartObserver observer : observers) {
            observer.onCartCleared();
        }
    }
    
    protected void notifyPriceChanged(double oldTotal, double newTotal) {
        for (CartObserver observer : observers) {
            observer.onPriceChanged(oldTotal, newTotal);
        }
    }
}
