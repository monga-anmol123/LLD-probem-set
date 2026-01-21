package enums;

public enum VehicleType {
    SEDAN(RideType.ECONOMY, "Sedan"),
    SUV(RideType.PREMIUM, "SUV"),
    LUXURY_SEDAN(RideType.LUXURY, "Luxury Sedan"),
    LUXURY_SUV(RideType.LUXURY, "Luxury SUV");
    
    private final RideType rideType;
    private final String displayName;
    
    VehicleType(RideType rideType, String displayName) {
        this.rideType = rideType;
        this.displayName = displayName;
    }
    
    public RideType getRideType() {
        return rideType;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}

