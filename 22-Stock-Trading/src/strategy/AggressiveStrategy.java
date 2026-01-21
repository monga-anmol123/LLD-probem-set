package strategy;

import model.Stock;
import model.Holding;

public class AggressiveStrategy implements TradingStrategy {
    
    @Override
    public boolean shouldBuy(Stock stock, double availableBalance) {
        // Buy if price increased by 2% or more (momentum trading)
        double priceChange = stock.getPriceChangePercent();
        
        // Also check if we have enough balance for at least 1 share
        boolean canAfford = availableBalance >= stock.getCurrentPrice();
        
        return canAfford && priceChange > 2.0;
    }
    
    @Override
    public boolean shouldSell(Stock stock, Holding holding) {
        // Sell if profit > 5% OR loss > 3% (quick exits)
        double profitPercent = holding.getProfitLossPercent();
        
        return profitPercent > 5.0 || profitPercent < -3.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Aggressive Strategy";
    }
}

