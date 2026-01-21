package factory;

import model.*;
import enums.VehicleType;

public class VehicleFactory {
    
    public static Vehicle createVehicle(VehicleType type, String vin, String make, String model, int year) {
        switch (type) {
            case ECONOMY:
                return new EconomyCar(vin, make, model, year);
            case SEDAN:
                return new Sedan(vin, make, model, year);
            case SUV:
                return new SUV(vin, make, model, year);
            case LUXURY:
                return new LuxuryCar(vin, make, model, year);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
    
    // Convenience method with default values for testing
    public static Vehicle createVehicle(VehicleType type, String vin) {
        String make = getDefaultMake(type);
        String model = getDefaultModel(type);
        int year = 2023;
        return createVehicle(type, vin, make, model, year);
    }
    
    private static String getDefaultMake(VehicleType type) {
        switch (type) {
            case ECONOMY:
                return "Toyota";
            case SEDAN:
                return "Honda";
            case SUV:
                return "Ford";
            case LUXURY:
                return "BMW";
            default:
                return "Generic";
        }
    }
    
    private static String getDefaultModel(VehicleType type) {
        switch (type) {
            case ECONOMY:
                return "Corolla";
            case SEDAN:
                return "Accord";
            case SUV:
                return "Explorer";
            case LUXURY:
                return "7 Series";
            default:
                return "Model";
        }
    }
}


