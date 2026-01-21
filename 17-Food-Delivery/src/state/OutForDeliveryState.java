package state;

import model.Order;
import enums.OrderStatus;

/**
 * State when order is out for delivery
 */
public class OutForDeliveryState implements OrderState {
    
    @Override
    public void prepare(Order order) {
        System.out.println("❌ Order is already out for delivery");
    }
    
    @Override
    public void readyForPickup(Order order) {
        System.out.println("❌ Order is already out for delivery");
    }
    
    @Override
    public void outForDelivery(Order order) {
        System.out.println("❌ Order is already out for delivery");
    }
    
    @Override
    public void deliver(Order order) {
        System.out.println("✓ Order " + order.getOrderId() + " has been delivered successfully!");
        order.setState(new DeliveredState());
        order.setStatus(OrderStatus.DELIVERED);
        
        // Mark delivery partner as available
        if (order.getDeliveryPartner() != null) {
            order.getDeliveryPartner().completeDelivery();
        }
        
        order.notifyObservers();
    }
    
    @Override
    public void cancel(Order order) {
        System.out.println("❌ Cannot cancel - order is already out for delivery");
    }
    
    @Override
    public String getStateName() {
        return "OUT_FOR_DELIVERY";
    }
}

