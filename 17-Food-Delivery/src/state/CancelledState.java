package state;

import model.Order;

/**
 * State when order is cancelled (final state)
 */
public class CancelledState implements OrderState {
    
    @Override
    public void prepare(Order order) {
        System.out.println("❌ Order is cancelled - cannot prepare");
    }
    
    @Override
    public void readyForPickup(Order order) {
        System.out.println("❌ Order is cancelled - cannot mark ready");
    }
    
    @Override
    public void outForDelivery(Order order) {
        System.out.println("❌ Order is cancelled - cannot deliver");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("❌ Order is cancelled - cannot deliver");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("❌ Order is already cancelled");
    }
    
    @Override
    public String getStateName() {
        return "CANCELLED";
    }
}

