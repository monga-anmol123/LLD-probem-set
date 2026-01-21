package observer;

import model.Stock;
import model.Order;
import java.util.ArrayList;
import java.util.List;

public abstract class Subject {
    private final List<Observer> observers = new ArrayList<>();
    
    public void attach(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }
    
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    protected void notifyPriceUpdate(Stock stock, double oldPrice, double newPrice) {
        for (Observer observer : observers) {
            observer.onPriceUpdate(stock, oldPrice, newPrice);
        }
    }
    
    protected void notifyOrderExecuted(Order order) {
        for (Observer observer : observers) {
            observer.onOrderExecuted(order);
        }
    }
    
    protected void notifyStopLossTriggered(Stock stock, double triggerPrice) {
        for (Observer observer : observers) {
            observer.onStopLossTriggered(stock, triggerPrice);
        }
    }
}

