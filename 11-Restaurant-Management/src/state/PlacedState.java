package state;

import model.Order;
import enums.OrderStatus;

public class PlacedState implements OrderState {
    
    @Override
    public void next(Order order) {
        order.setState(new PreparingState());
        order.setStatus(OrderStatus.PREPARING);
    }
    
    @Override
    public void printStatus() {
        System.out.println("Order has been placed and sent to kitchen.");
    }
    
    @Override
    public String getStateName() {
        return "PLACED";
    }
}


