package strategy;

import model.Stock;
import model.Holding;

public class ConservativeStrategy implements TradingStrategy {
    
    @Override
    public boolean shouldBuy(Stock stock, double availableBalance) {
        // Buy if price decreased by 3% or more (buy the dip)
        double priceChange = stock.getPriceChangePercent();
        
        // Check if we have enough balance
        boolean canAfford = availableBalance >= stock.getCurrentPrice();
        
        return canAfford && priceChange < -3.0;
    }
    
    @Override
    public boolean shouldSell(Stock stock, Holding holding) {
        // Sell only if profit > 10% (hold for bigger gains)
        // Or if loss > 5% (cut losses)
        double profitPercent = holding.getProfitLossPercent();
        
        return profitPercent > 10.0 || profitPercent < -5.0;
    }
    
    @Override
    public String getStrategyName() {
        return "Conservative Strategy";
    }
}

