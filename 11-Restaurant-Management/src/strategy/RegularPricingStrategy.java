package strategy;

import model.MenuItem;
import java.util.List;

public class RegularPricingStrategy implements PricingStrategy {
    
    @Override
    public double calculateTotal(List<MenuItem> items) {
        double total = 0.0;
        for (MenuItem item : items) {
            total += item.getPrice();
        }
        return total;
    }
    
    @Override
    public String getStrategyName() {
        return "Regular Pricing";
    }
}


