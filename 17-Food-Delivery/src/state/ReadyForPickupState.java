package state;

import model.Order;
import enums.OrderStatus;

/**
 * State when order is ready for pickup
 */
public class ReadyForPickupState implements OrderState {
    
    @Override
    public void prepare(Order order) {
        System.out.println("❌ Order is already prepared");
    }
    
    @Override
    public void readyForPickup(Order order) {
        System.out.println("❌ Order is already ready for pickup");
    }
    
    @Override
    public void outForDelivery(Order order) {
        if (order.getDeliveryPartner() == null) {
            System.out.println("❌ Cannot mark out for delivery - no delivery partner assigned");
            return;
        }
        System.out.println("✓ Order " + order.getOrderId() + " is out for delivery");
        order.setState(new OutForDeliveryState());
        order.setStatus(OrderStatus.OUT_FOR_DELIVERY);
        order.notifyObservers();
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
        return "READY_FOR_PICKUP";
    }
}

