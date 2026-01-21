package state;

import model.Order;

public class ServedState implements OrderState {
    
    @Override
    public void next(Order order) {
        System.out.println("Order has been served. No further state transitions.");
    }
    
    @Override
    public void printStatus() {
        System.out.println("Order has been served to the customer.");
    }
    
    @Override
    public String getStateName() {
        return "SERVED";
    }
}


