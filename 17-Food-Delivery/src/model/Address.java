package model;

/**
 * Represents a delivery address
 */
public class Address {
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private double latitude;
    private double longitude;
    
    public Address(String street, String city, String state, String zipCode, 
                   double latitude, double longitude) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    /**
     * Calculate distance to another address (simplified Haversine formula)
     */
    public double distanceTo(Address other) {
        double lat1 = Math.toRadians(this.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon2 = Math.toRadians(other.longitude);
        
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;
        
        double a = Math.pow(Math.sin(dlat / 2), 2) + 
                   Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double r = 6371; // Radius of earth in kilometers
        
        return c * r;
    }
    
    @Override
    public String toString() {
        return street + ", " + city + ", " + state + " " + zipCode;
    }
}

