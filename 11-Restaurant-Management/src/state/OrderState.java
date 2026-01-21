package state;

import model.Order;

public interface OrderState {
    void next(Order order);
    void printStatus();
    String getStateName();
}


