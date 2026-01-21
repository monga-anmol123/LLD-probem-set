package enums;

public enum RideStatus {
    REQUESTED("Ride requested, waiting for driver"),
    ACCEPTED("Driver accepted, on the way"),
    ARRIVED("Driver arrived at pickup location"),
    STARTED("Ride in progress"),
    COMPLETED("Ride completed successfully"),
    CANCELLED("Ride cancelled");
    
    private final String description;
    
    RideStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}

