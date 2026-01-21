package observer;

import model.Order;

/**
 * Notifies delivery partners about order updates
 */
public class DeliveryPartnerNotifier implements OrderObserver {
    
    @Override
    public void onOrderUpdate(Order order) {
        if (order.getDeliveryPartner() != null) {
            System.out.println("🛵 [Delivery Partner " + order.getDeliveryPartner().getName() + "] " +
                "Order " + order.getOrderId() + " status: " + order.getStatus());
        }
    }
}

