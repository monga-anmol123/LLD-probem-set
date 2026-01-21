package state;

import model.Order;

/**
 * State interface for Order state management
 * Implements State Design Pattern
 */
public interface OrderState {
    void prepare(Order order);
    void readyForPickup(Order order);
    void outForDelivery(Order order);
    void deliver(Order order);
    void cancel(Order order);
    String getStateName();
}

