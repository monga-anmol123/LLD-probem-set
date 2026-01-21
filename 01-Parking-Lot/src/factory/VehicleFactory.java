package factory;

import model.Vehicle;
import model.Car;
import model.Bike;
import model.Truck;
import enums.VehicleType;

public class VehicleFactory {
    public static Vehicle createVehicle(VehicleType type, String licensePlate) {
        switch (type) {
            case CAR:
                return new Car(licensePlate);
            case BIKE:
                return new Bike(licensePlate);
            case TRUCK:
                return new Truck(licensePlate);
            default:
                throw new IllegalArgumentException("Unknown vehicle type: " + type);
        }
    }
}


