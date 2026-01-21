package strategy;

import model.Room;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;

public class SeasonalPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculatePrice(Room room, LocalDate checkInDate, LocalDate checkOutDate) {
        long nights = ChronoUnit.DAYS.between(checkInDate, checkOutDate);
        double basePrice = room.getRoomType().getBasePrice();
        double totalPrice = 0;
        
        LocalDate currentDate = checkInDate;
        for (int i = 0; i < nights; i++) {
            double multiplier = getSeasonalMultiplier(currentDate);
            totalPrice += basePrice * multiplier;
            currentDate = currentDate.plusDays(1);
        }
        
        return totalPrice;
    }
    
    private double getSeasonalMultiplier(LocalDate date) {
        Month month = date.getMonth();
        
        // Peak season: June, July, August, December
        if (month == Month.JUNE || month == Month.JULY || 
            month == Month.AUGUST || month == Month.DECEMBER) {
            return 2.0; // 2x price
        }
        
        // High season: April, May, September, October
        if (month == Month.APRIL || month == Month.MAY || 
            month == Month.SEPTEMBER || month == Month.OCTOBER) {
            return 1.5; // 1.5x price
        }
        
        // Low season: January, February, March, November
        return 1.0; // Base price
    }
    
    @Override
    public String getStrategyName() {
        return "Seasonal Pricing (Peak: 2x, High: 1.5x, Low: 1x)";
    }
}
