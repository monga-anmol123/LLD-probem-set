package observer;

import model.Order;

/**
 * Notifies restaurants about order updates
 */
public class RestaurantNotifier implements OrderObserver {
    
    @Override
    public void onOrderUpdate(Order order) {
        System.out.println("🏪 [Restaurant " + order.getRestaurant().getName() + "] " +
            "Order " + order.getOrderId() + " status: " + order.getStatus());
    }
}

