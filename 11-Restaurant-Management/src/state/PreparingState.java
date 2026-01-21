package state;

import model.Order;
import enums.OrderStatus;

public class PreparingState implements OrderState {
    
    @Override
    public void next(Order order) {
        order.setState(new ReadyState());
        order.setStatus(OrderStatus.READY);
    }
    
    @Override
    public void printStatus() {
        System.out.println("Order is being prepared in the kitchen.");
    }
    
    @Override
    public String getStateName() {
        return "PREPARING";
    }
}


