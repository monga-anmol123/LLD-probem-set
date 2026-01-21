package model;

import enums.SpotType;
import enums.ParkingSpotStatus;

public class ParkingSpot {
    private String spotId;
    private SpotType type;
    private ParkingSpotStatus status;
    private Vehicle parkedVehicle;
    private int floor;
    
    public ParkingSpot(String spotId, SpotType type, int floor) {
        this.spotId = spotId;
        this.type = type;
        this.floor = floor;
        this.status = ParkingSpotStatus.AVAILABLE;
    }
    
    public boolean canFitVehicle(Vehicle vehicle) {
        if (status != ParkingSpotStatus.AVAILABLE) {
            return false;
        }
        
        switch (vehicle.getType()) {
            case BIKE:
                return type == SpotType.BIKE;
            case CAR:
                return type == SpotType.COMPACT || type == SpotType.LARGE || type == SpotType.HANDICAPPED;
            case TRUCK:
                return type == SpotType.LARGE;
            default:
                return false;
        }
    }
    
    public void parkVehicle(Vehicle vehicle) {
        if (!canFitVehicle(vehicle)) {
            throw new IllegalStateException("Cannot park vehicle in this spot");
        }
        this.parkedVehicle = vehicle;
        this.status = ParkingSpotStatus.OCCUPIED;
    }
    
    public void removeVehicle() {
        this.parkedVehicle = null;
        this.status = ParkingSpotStatus.AVAILABLE;
    }
    
    public boolean isAvailable() {
        return status == ParkingSpotStatus.AVAILABLE;
    }
    
    public String getSpotId() { return spotId; }
    public SpotType getType() { return type; }
    public int getFloor() { return floor; }
    public Vehicle getParkedVehicle() { return parkedVehicle; }
}


