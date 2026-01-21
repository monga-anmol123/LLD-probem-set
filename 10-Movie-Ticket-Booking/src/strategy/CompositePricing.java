package strategy;

import model.Seat;
import model.Show;
import java.util.ArrayList;
import java.util.List;

/**
 * Composite pricing strategy that combines multiple strategies
 * Applies all strategies and returns the average or combined price
 */
public class CompositePricing implements PricingStrategy {
    
    private List<PricingStrategy> strategies;
    
    public CompositePricing() {
        this.strategies = new ArrayList<>();
    }
    
    public void addStrategy(PricingStrategy strategy) {
        strategies.add(strategy);
    }
    
    @Override
    public double calculatePrice(Seat seat, Show show) {
        if (strategies.isEmpty()) {
            return seat.getBasePrice();
        }
        
        // Apply all strategies and take the average
        double totalPrice = 0;
        for (PricingStrategy strategy : strategies) {
            totalPrice += strategy.calculatePrice(seat, show);
        }
        
        return totalPrice / strategies.size();
    }
}


