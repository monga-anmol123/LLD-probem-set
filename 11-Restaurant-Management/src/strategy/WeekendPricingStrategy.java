package strategy;

import model.MenuItem;
import java.util.List;

public class WeekendPricingStrategy implements PricingStrategy {
    
    private static final double WEEKEND_SURCHARGE = 0.10; // 10% extra
    
    @Override
    public double calculateTotal(List<MenuItem> items) {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total * (1 + WEEKEND_SURCHARGE);
    }
    
    @Override
    public String getStrategyName() {
        return "Weekend Pricing (10% surcharge)";
    }
}


