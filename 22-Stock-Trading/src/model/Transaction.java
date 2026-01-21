package model;

import enums.TransactionType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private static int transactionCounter = 1;
    
    private final String transactionId;
    private final TransactionType type;
    private final Stock stock;
    private final int quantity;
    private final double price;
    private final double totalAmount;
    private final LocalDateTime timestamp;
    
    public Transaction(TransactionType type, Stock stock, int quantity, double price) {
        this.transactionId = "TXN-" + String.format("%06d", transactionCounter++);
        this.type = type;
        this.stock = stock;
        this.quantity = quantity;
        this.price = price;
        this.totalAmount = quantity * price;
        this.timestamp = LocalDateTime.now();
    }
    
    // Getters
    public String getTransactionId() {
        return transactionId;
    }
    
    public TransactionType getType() {
        return type;
    }
    
    public Stock getStock() {
        return stock;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public double getPrice() {
        return price;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return String.format("[%s] %s %d shares of %s @ $%.2f (Total: $%.2f)",
            timestamp.format(formatter), type, quantity, stock.getSymbol(), 
            price, totalAmount);
    }
}

