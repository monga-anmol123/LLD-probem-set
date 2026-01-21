package model;

import java.time.LocalDateTime;

public class Settlement {
    private String settlementId;
    private User payer;
    private User receiver;
    private double amount;
    private LocalDateTime timestamp;
    
    public Settlement(String settlementId, User payer, User receiver, double amount) {
        this.settlementId = settlementId;
        this.payer = payer;
        this.receiver = receiver;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }
    
    public void settle() {
        // Update balances: payer settles debt with receiver
        // From payer's perspective: they owed receiver (negative), now paying back (add positive)
        payer.addBalance(receiver, amount);
    }
    
    // Getters
    public String getSettlementId() { return settlementId; }
    public User getPayer() { return payer; }
    public User getReceiver() { return receiver; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    
    @Override
    public String toString() {
        return String.format("Settlement[%s]: %s paid %s $%.2f", 
            settlementId, payer.getName(), receiver.getName(), amount);
    }
}
