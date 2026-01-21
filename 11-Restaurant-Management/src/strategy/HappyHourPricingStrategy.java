package strategy;

import model.MenuItem;
import enums.MenuCategory;
import java.util.List;

public class HappyHourPricingStrategy implements PricingStrategy {
    
    private static final double BEVERAGE_DISCOUNT = 0.20; // 20% off
    
    @Override
    public double calculateTotal(List<MenuItem> items) {
        double total = 0.0;
        for (MenuItem item : items) {
            if (item.getCategory() == MenuCategory.BEVERAGE) {
                total += item.getPrice() * (1 - BEVERAGE_DISCOUNT);
            } else {
                total += item.getPrice();
            }
        }
        return total;
    }
    
    @Override
    public String getStrategyName() {
        return "Happy Hour Pricing (20% off beverages)";
    }
}


