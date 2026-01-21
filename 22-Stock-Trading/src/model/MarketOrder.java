package model;

import enums.OrderType;
import enums.TransactionType;

public class MarketOrder extends Order {
    
    public MarketOrder(Trader trader, Stock stock, int quantity, TransactionType transactionType) {
        super(trader, stock, quantity, OrderType.MARKET, transactionType);
    }
    
    @Override
    public boolean canExecute() {
        // Market orders always execute immediately at current price
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("MarketOrder[%s, %s %d shares of %s at MARKET PRICE, Status: %s]",
            orderId, transactionType, quantity, stock.getSymbol(), status);
    }
}

