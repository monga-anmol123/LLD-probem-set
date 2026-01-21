package model;

import enums.VehicleStatus;
import enums.VehicleType;

public abstract class Vehicle {
    protected String vin;
    protected String make;
    protected String model;
    protected int year;
    protected VehicleType type;
    protected VehicleStatus status;
    protected double dailyRate;
    protected int mileage;
    protected String locationId;
    
    public Vehicle(String vin, String make, String model, int year, VehicleType type, double dailyRate) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.year = year;
        this.type = type;
        this.dailyRate = dailyRate;
        this.status = VehicleStatus.AVAILABLE;
        this.mileage = 0;
        this.locationId = "LOC-001"; // Default location
    }
    
    public boolean isAvailable() {
        return status == VehicleStatus.AVAILABLE;
    }
    
    public void rent() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.RENTED;
        } else {
            throw new IllegalStateException("Vehicle is not available for rent");
        }
    }
    
    public void returnVehicle() {
        if (status == VehicleStatus.RENTED) {
            status = VehicleStatus.AVAILABLE;
        }
    }
    
    public void reserve() {
        if (status == VehicleStatus.AVAILABLE) {
            status = VehicleStatus.RESERVED;
        } else {
            throw new IllegalStateException("Vehicle is not available for reservation");
        }
    }
    
    public void setUnderMaintenance() {
        status = VehicleStatus.UNDER_MAINTENANCE;
    }
    
    // Getters and Setters
    public String getVin() { return vin; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public VehicleType getType() { return type; }
    public VehicleStatus getStatus() { return status; }
    public void setStatus(VehicleStatus status) { this.status = status; }
    public double getDailyRate() { return dailyRate; }
    public int getMileage() { return mileage; }
    public void setMileage(int mileage) { this.mileage = mileage; }
    public String getLocationId() { return locationId; }
    public void setLocationId(String locationId) { this.locationId = locationId; }
    
    @Override
    public String toString() {
        return String.format("%s %s %s (%d) - VIN: %s, Rate: $%.2f/day, Status: %s", 
            type, make, model, year, vin, dailyRate, status);
    }
}


