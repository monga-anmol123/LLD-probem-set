package model;

import enums.OrderType;
import enums.TransactionType;

public class LimitOrder extends Order {
    private final double limitPrice;
    
    public LimitOrder(Trader trader, Stock stock, int quantity, 
                      TransactionType transactionType, double limitPrice) {
        super(trader, stock, quantity, OrderType.LIMIT, transactionType);
        this.limitPrice = limitPrice;
    }
    
    @Override
    public boolean canExecute() {
        double currentPrice = stock.getCurrentPrice();
        
        if (transactionType == TransactionType.BUY) {
            // Buy limit: execute if current price <= limit price
            return currentPrice <= limitPrice;
        } else {
            // Sell limit: execute if current price >= limit price
            return currentPrice >= limitPrice;
        }
    }
    
    public double getLimitPrice() {
        return limitPrice;
    }
    
    @Override
    public String toString() {
        return String.format("LimitOrder[%s, %s %d shares of %s at $%.2f (Current: $%.2f), Status: %s]",
            orderId, transactionType, quantity, stock.getSymbol(), 
            limitPrice, stock.getCurrentPrice(), status);
    }
}

