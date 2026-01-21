package model;

import enums.SpotType;
import java.util.ArrayList;
import java.util.List;

public class ParkingFloor {
    private int floorNumber;
    private List<ParkingSpot> spots;
    
    public ParkingFloor(int floorNumber) {
        this.floorNumber = floorNumber;
        this.spots = new ArrayList<>();
    }
    
    public void addSpot(ParkingSpot spot) {
        spots.add(spot);
    }
    
    public ParkingSpot findAvailableSpot(Vehicle vehicle) {
        return spots.stream()
            .filter(spot -> spot.canFitVehicle(vehicle))
            .findFirst()
            .orElse(null);
    }
    
    public int getAvailableSpotCount(SpotType type) {
        return (int) spots.stream()
            .filter(spot -> spot.getType() == type && spot.isAvailable())
            .count();
    }
    
    public int getFloorNumber() { return floorNumber; }
}


