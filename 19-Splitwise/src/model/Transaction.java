package model;

import enums.TransactionType;
import java.time.LocalDateTime;

public class Transaction {
    private String transactionId;
    private User from;
    private User to;
    private double amount;
    private TransactionType type;
    private LocalDateTime timestamp;
    private String description;
    
    public Transaction(String transactionId, User from, User to, double amount, TransactionType type) {
        this.transactionId = transactionId;
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.type = type;
        this.timestamp = LocalDateTime.now();
        this.description = "";
    }
    
    public Transaction(User from, User to, double amount) {
        this("TXN-" + System.currentTimeMillis(), from, to, amount, TransactionType.EXPENSE);
    }
    
    // Getters
    public String getTransactionId() { return transactionId; }
    public User getFrom() { return from; }
    public User getTo() { return to; }
    public double getAmount() { return amount; }
    public TransactionType getType() { return type; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    @Override
    public String toString() {
        return String.format("%s pays %s: $%.2f", from.getName(), to.getName(), amount);
    }
}
