package strategy;

import model.Seat;
import model.Show;

/**
 * Pricing strategy based on show timing
 * Matinee shows (12 PM - 3 PM): 20% discount
 * Evening shows (6 PM - 9 PM): 20% premium
 * Night shows (9 PM onwards): Base price
 * Morning shows (before 12 PM): 30% discount
 */
public class TimingBasedPricing implements PricingStrategy {
    
    @Override
    public double calculatePrice(Seat seat, Show show) {
        double basePrice = seat.getBasePrice();
        int hour = show.getStartTime().getHour();
        
        if (hour < 12) {
            // Morning show - 30% discount
            return basePrice * 0.7;
        } else if (hour >= 12 && hour < 15) {
            // Matinee show - 20% discount
            return basePrice * 0.8;
        } else if (hour >= 18 && hour < 21) {
            // Evening show - 20% premium
            return basePrice * 1.2;
        } else {
            // Night show - base price
            return basePrice;
        }
    }
}


