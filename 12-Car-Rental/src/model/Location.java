package model;

import enums.VehicleType;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Location {
    private String locationId;
    private String name;
    private String address;
    private List<Vehicle> vehicles;
    
    public Location(String locationId, String name, String address) {
        this.locationId = locationId;
        this.name = name;
        this.address = address;
        this.vehicles = new ArrayList<>();
    }
    
    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setLocationId(locationId);
    }
    
    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
    }
    
    public List<Vehicle> getAvailableVehicles() {
        return vehicles.stream()
            .filter(Vehicle::isAvailable)
            .collect(Collectors.toList());
    }
    
    public List<Vehicle> getAvailableVehiclesByType(VehicleType type) {
        return vehicles.stream()
            .filter(v -> v.isAvailable() && v.getType() == type)
            .collect(Collectors.toList());
    }
    
    public int getAvailableCount() {
        return (int) vehicles.stream().filter(Vehicle::isAvailable).count();
    }
    
    // Getters
    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public List<Vehicle> getVehicles() { return vehicles; }
    
    @Override
    public String toString() {
        return String.format("Location: %s (%s) - %d vehicles available", 
            name, locationId, getAvailableCount());
    }
}


