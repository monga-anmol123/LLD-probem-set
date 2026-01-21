package model;

import enums.OrderType;
import enums.TransactionType;

public class StopLossOrder extends Order {
    private final double stopPrice;
    
    public StopLossOrder(Trader trader, Stock stock, int quantity, double stopPrice) {
        super(trader, stock, quantity, OrderType.STOP_LOSS, TransactionType.SELL);
        this.stopPrice = stopPrice;
    }
    
    @Override
    public boolean canExecute() {
        // Stop-loss triggers when price drops to or below stop price
        return stock.getCurrentPrice() <= stopPrice;
    }
    
    public double getStopPrice() {
        return stopPrice;
    }
    
    @Override
    public String toString() {
        return String.format("StopLossOrder[%s, SELL %d shares of %s when price <= $%.2f (Current: $%.2f), Status: %s]",
            orderId, quantity, stock.getSymbol(), stopPrice, stock.getCurrentPrice(), status);
    }
}

