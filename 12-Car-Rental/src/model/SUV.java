package model;

import enums.VehicleType;

public class SUV extends Vehicle {
    public SUV(String vin, String make, String model, int year) {
        super(vin, make, model, year, VehicleType.SUV, 100.0); // $100/day base rate
    }
}


