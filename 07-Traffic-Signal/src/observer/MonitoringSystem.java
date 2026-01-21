package observer;

import model.TrafficSignal;
import enums.SignalLight;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Concrete Observer: Central traffic monitoring system
 * Logs all signal changes for analytics and debugging
 */
public class MonitoringSystem implements TrafficObserver {
    private String systemId;
    private int changeCount;
    private DateTimeFormatter formatter;
    
    public MonitoringSystem(String systemId) {
        this.systemId = systemId;
        this.changeCount = 0;
        this.formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    }
    
    @Override
    public void update(TrafficSignal signal, SignalLight newLight) {
        changeCount++;
        String timestamp = LocalDateTime.now().format(formatter);
        
        System.out.println("  [Monitoring System " + systemId + "] " + 
                         timestamp + " - " + 
                         signal.getDirection() + " -> " + newLight + 
                         " (Change #" + changeCount + ")");
        
        // In real system, would log to database/file
        logToDatabase(signal, newLight, timestamp);
    }
    
    private void logToDatabase(TrafficSignal signal, SignalLight light, String timestamp) {
        // Simulate database logging
        // In production: INSERT INTO signal_logs (timestamp, direction, light) VALUES (...)
    }
    
    public int getChangeCount() {
        return changeCount;
    }
    
    public String getSystemId() {
        return systemId;
    }
}


