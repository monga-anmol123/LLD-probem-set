package model;

public class Location {
    private final double latitude;
    private final double longitude;
    private final String address;
    
    public Location(double latitude, double longitude, String address) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }
    
    // Calculate distance using Haversine formula (simplified for demo)
    public double calculateDistance(Location other) {
        double lat1 = Math.toRadians(this.latitude);
        double lat2 = Math.toRadians(other.latitude);
        double lon1 = Math.toRadians(this.longitude);
        double lon2 = Math.toRadians(other.longitude);
        
        double dLat = lat2 - lat1;
        double dLon = lon2 - lon1;
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(lat1) * Math.cos(lat2) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        double earthRadiusKm = 6371;
        return earthRadiusKm * c; // Distance in kilometers
    }
    
    public double getLatitude() {
        return latitude;
    }
    
    public double getLongitude() {
        return longitude;
    }
    
    public String getAddress() {
        return address;
    }
    
    @Override
    public String toString() {
        return address + " (" + String.format("%.4f", latitude) + ", " + 
               String.format("%.4f", longitude) + ")";
    }
}

