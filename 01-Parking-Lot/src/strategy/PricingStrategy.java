package strategy;

import model.ParkingTicket;

public interface PricingStrategy {
    double calculateFee(ParkingTicket ticket);
}


