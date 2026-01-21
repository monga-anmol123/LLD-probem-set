package observer;

import model.Order;

/**
 * Notifies customers about order updates
 */
public class CustomerNotifier implements OrderObserver {
    
    @Override
    public void onOrderUpdate(Order order) {
        System.out.println("📱 [SMS to " + order.getCustomer().getPhone() + "] " +
            "Your order " + order.getOrderId() + " is now " + order.getStatus());
        
        System.out.println("📧 [Email to " + order.getCustomer().getEmail() + "] " +
            "Order Update: " + order.getStatus());
    }
}

