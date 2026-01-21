package strategy;

import model.ParkingTicket;

public class FlatPricingStrategy implements PricingStrategy {
    private double flatRate;
    
    public FlatPricingStrategy(double flatRate) {
        this.flatRate = flatRate;
    }
    
    @Override
    public double calculateFee(ParkingTicket ticket) {
        return flatRate;
    }
}


