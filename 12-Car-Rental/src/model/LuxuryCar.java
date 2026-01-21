package model;

import enums.VehicleType;

public class LuxuryCar extends Vehicle {
    public LuxuryCar(String vin, String make, String model, int year) {
        super(vin, make, model, year, VehicleType.LUXURY, 200.0); // $200/day base rate
    }
}


