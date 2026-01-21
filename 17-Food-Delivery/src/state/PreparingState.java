package state;

import model.Order;
import enums.OrderStatus;

/**
 * State when order is being prepared
 */
public class PreparingState implements OrderState {
    
    @Override
    public void prepare(Order order) {
        System.out.println("❌ Order is already being prepared");
    }
    
    @Override
    public void readyForPickup(Order order) {
        System.out.println("✓ Order " + order.getOrderId() + " is ready for pickup");
        order.setState(new ReadyForPickupState());
        order.setStatus(OrderStatus.READY_FOR_PICKUP);
        order.notifyObservers();
    }
    
    @Override
    public void outForDelivery(Order order) {
        System.out.println("❌ Cannot mark out for delivery - order is not ready yet");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("❌ Cannot deliver - order is not out for delivery yet");
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("✓ Order " + order.getOrderId() + " has been cancelled");
        order.setState(new CancelledState());
        order.setStatus(OrderStatus.CANCELLED);
        order.notifyObservers();
    }
    
    @Override
    public String getStateName() {
        return "PREPARING";
    }
}

