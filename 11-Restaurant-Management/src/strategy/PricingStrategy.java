package strategy;

import model.MenuItem;
import java.util.List;

public interface PricingStrategy {
    double calculateTotal(List<MenuItem> items);
    String getStrategyName();
}


