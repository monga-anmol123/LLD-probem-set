package state;

import model.Order;
import enums.OrderStatus;

public class ReadyState implements OrderState {
    
    @Override
    public void next(Order order) {
        order.setState(new ServedState());
        order.setStatus(OrderStatus.SERVED);
    }
    
    @Override
    public void printStatus() {
        System.out.println("Order is ready for pickup.");
    }
    
    @Override
    public String getStateName() {
        return "READY";
    }
}


