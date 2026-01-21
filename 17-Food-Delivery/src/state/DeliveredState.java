package state;

import model.Order;

/**
 * State when order is delivered (final state)
 */
public class DeliveredState implements OrderState {
    
    @Override
    public void prepare(Order order) {
        System.out.println("❌ Order is already delivered");
    }
    
    @Override
    public void readyForPickup(Order order) {
        System.out.println("❌ Order is already delivered");
    }
    
    @Override
    public void outForDelivery(Order order) {
        System.out.println("❌ Order is already delivered");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("❌ Order is already delivered");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("❌ Cannot cancel - order is already delivered");
    }
    
    @Override
    public String getStateName() {
        return "DELIVERED";
    }
}

