package model;

import enums.VehicleType;

public class Vehicle {
    private final String vehicleId;
    private final VehicleType type;
    private final String licensePlate;
    private final String model;
    private final String color;
    
    public Vehicle(String vehicleId, VehicleType type, String licensePlate, 
                   String model, String color) {
        this.vehicleId = vehicleId;
        this.type = type;
        this.licensePlate = licensePlate;
        this.model = model;
        this.color = color;
    }
    
    public String getVehicleId() {
        return vehicleId;
    }
    
    public VehicleType getType() {
        return type;
    }
    
    public String getLicensePlate() {
        return licensePlate;
    }
    
    public String getModel() {
        return model;
    }
    
    public String getColor() {
        return color;
    }
    
    @Override
    public String toString() {
        return color + " " + model + " (" + licensePlate + ")";
    }
}

