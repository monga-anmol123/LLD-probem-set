package strategy;

import model.Seat;
import model.Show;
import enums.SeatType;

/**
 * Pricing strategy based on seat type
 * REGULAR: Base price
 * PREMIUM: +50% on base price
 * VIP: +100% on base price
 * WHEELCHAIR: Base price
 */
public class SeatTypePricing implements PricingStrategy {
    
    @Override
    public double calculatePrice(Seat seat, Show show) {
        double basePrice = seat.getBasePrice();
        SeatType type = seat.getType();
        
        switch (type) {
            case REGULAR:
            case WHEELCHAIR:
                return basePrice;
            case PREMIUM:
                return basePrice * 1.5;
            case VIP:
                return basePrice * 2.0;
            default:
                return basePrice;
        }
    }
}


