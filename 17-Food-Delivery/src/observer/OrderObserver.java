package observer;

import model.Order;

/**
 * Observer interface for order updates
 * Implements Observer Design Pattern
 */
public interface OrderObserver {
    void onOrderUpdate(Order order);
}

