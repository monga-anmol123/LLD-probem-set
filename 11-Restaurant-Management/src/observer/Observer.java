package observer;

import model.Order;

public interface Observer {
    void update(Order order, String message);
}


