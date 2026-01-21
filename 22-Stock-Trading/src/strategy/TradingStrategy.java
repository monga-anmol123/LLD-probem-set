package strategy;

import model.Stock;
import model.Holding;

public interface TradingStrategy {
    boolean shouldBuy(Stock stock, double availableBalance);
    boolean shouldSell(Stock stock, Holding holding);
    String getStrategyName();
}

