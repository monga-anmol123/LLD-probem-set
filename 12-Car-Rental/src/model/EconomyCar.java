package model;

import enums.VehicleType;

public class EconomyCar extends Vehicle {
    public EconomyCar(String vin, String make, String model, int year) {
        super(vin, make, model, year, VehicleType.ECONOMY, 50.0); // $50/day base rate
    }
}


