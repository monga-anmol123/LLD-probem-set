package model;

import enums.OrderType;
import enums.OrderStatus;
import enums.TransactionType;
import java.time.LocalDateTime;

public abstract class Order {
    private static int orderCounter = 1;
    
    protected final String orderId;
    protected final Trader trader;
    protected final Stock stock;
    protected final int quantity;
    protected final OrderType orderType;
    protected final TransactionType transactionType;
    protected OrderStatus status;
    protected final LocalDateTime timestamp;
    protected double executionPrice;
    
    public Order(Trader trader, Stock stock, int quantity, OrderType orderType, 
                 TransactionType transactionType) {
        this.orderId = "ORD-" + String.format("%06d", orderCounter++);
        this.trader = trader;
        this.stock = stock;
        this.quantity = quantity;
        this.orderType = orderType;
        this.transactionType = transactionType;
        this.status = OrderStatus.PENDING;
        this.timestamp = LocalDateTime.now();
        this.executionPrice = 0;
    }
    
    public abstract boolean canExecute();
    
    public void execute(double price) {
        this.executionPrice = price;
        this.status = OrderStatus.EXECUTED;
    }
    
    public void cancel() {
        this.status = OrderStatus.CANCELLED;
    }
    
    public void reject() {
        this.status = OrderStatus.REJECTED;
    }
    
    // Getters
    public String getOrderId() {
        return orderId;
    }
    
    public Trader getTrader() {
        return trader;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public OrderType getOrderType() {
        return orderType;
    }
    
    public TransactionType getTransactionType() {
        return transactionType;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public double getExecutionPrice() {
        return executionPrice;
    }
    
    @Override
    public String toString() {
        return String.format("Order[%s, %s, %s %d shares of %s, Status: %s]",
            orderId, orderType, transactionType, quantity, stock.getSymbol(), status);
    }
}

