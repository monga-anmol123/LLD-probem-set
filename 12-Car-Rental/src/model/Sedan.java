package model;

import enums.VehicleType;

public class Sedan extends Vehicle {
    public Sedan(String vin, String make, String model, int year) {
        super(vin, make, model, year, VehicleType.SEDAN, 70.0); // $70/day base rate
    }
}


