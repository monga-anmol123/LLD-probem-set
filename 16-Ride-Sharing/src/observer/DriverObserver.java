package observer;

import model.Ride;
import model.Driver;

public class DriverObserver implements Observer {
    private final Driver driver;
    
    public DriverObserver(Driver driver) {
        this.driver = driver;
    }
    
    @Override
    public void update(Ride ride, String message) {
        System.out.println("📱 [Driver " + driver.getName() + "] " + message);
    }
    
    public Driver getDriver() {
        return driver;
    }
}

