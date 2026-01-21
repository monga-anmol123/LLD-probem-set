package observer;

import model.Stock;
import model.Order;

public interface Observer {
    void onPriceUpdate(Stock stock, double oldPrice, double newPrice);
    void onOrderExecuted(Order order);
    void onStopLossTriggered(Stock stock, double triggerPrice);
}

