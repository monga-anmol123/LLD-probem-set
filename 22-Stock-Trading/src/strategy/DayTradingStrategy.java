package strategy;

import model.Stock;
import model.Holding;

public class DayTradingStrategy implements TradingStrategy {
    
    @Override
    public boolean shouldBuy(Stock stock, double availableBalance) {
        // Buy on any positive momentum (> 1%)
        double priceChange = stock.getPriceChangePercent();
        
        boolean canAfford = availableBalance >= stock.getCurrentPrice();
        
        return canAfford && priceChange > 1.0;
    }
    
    @Override
    public boolean shouldSell(Stock stock, Holding holding) {
        // Sell quickly on any profit (> 2%) or small loss (< -2%)
        // Day traders close positions quickly
        double profitPercent = holding.getProfitLossPercent();
        
        return profitPercent > 2.0 || profitPercent < -2.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Day Trading Strategy";
    }
}

