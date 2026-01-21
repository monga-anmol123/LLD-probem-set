package strategy;

import model.Seat;
import model.Show;
import java.time.DayOfWeek;

/**
 * Pricing strategy based on day of week
 * Weekdays (Mon-Thu): Base price
 * Weekend (Fri-Sun): 25% premium
 */
public class WeekendPricing implements PricingStrategy {
    
    @Override
    public double calculatePrice(Seat seat, Show show) {
        double basePrice = seat.getBasePrice();
        DayOfWeek dayOfWeek = show.getStartTime().getDayOfWeek();
        
        // Friday, Saturday, Sunday - weekend premium
        if (dayOfWeek == DayOfWeek.FRIDAY || 
            dayOfWeek == DayOfWeek.SATURDAY || 
            dayOfWeek == DayOfWeek.SUNDAY) {
            return basePrice * 1.25;
        }
        
        // Monday to Thursday - base price
        return basePrice;
    }
}


