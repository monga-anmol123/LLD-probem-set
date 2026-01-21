package observer;

import model.Stock;
import model.Order;
import model.Trader;

public class TraderObserver implements Observer {
    private final Trader trader;
    
    public TraderObserver(Trader trader) {
        this.trader = trader;
    }
    
    @Override
    public void onPriceUpdate(Stock stock, double oldPrice, double newPrice) {
        double changePercent = ((newPrice - oldPrice) / oldPrice) * 100;
        System.out.printf("📱 [%s] %s price updated: $%.2f → $%.2f (%.2f%%)\n", 
            trader.getName(), stock.getSymbol(), oldPrice, newPrice, changePercent);
    }
    
    @Override
    public void onOrderExecuted(Order order) {
        System.out.printf("📱 [%s] Order #%s EXECUTED: %s %d shares of %s at $%.2f\n",
            trader.getName(), order.getOrderId(), 
            order.getTransactionType(), order.getQuantity(), 
            order.getStock().getSymbol(), order.getExecutionPrice());
    }
    
    @Override
    public void onStopLossTriggered(Stock stock, double triggerPrice) {
        System.out.printf("⚠️  [%s] STOP-LOSS TRIGGERED for %s at $%.2f\n",
            trader.getName(), stock.getSymbol(), triggerPrice);
    }
    
    public Trader getTrader() {
        return trader;
    }
}

