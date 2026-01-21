package model;

import enums.ExpenseCategory;
import enums.SplitType;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Expense {
    private String expenseId;
    private String description;
    private double amount;
    private User paidBy;
    private List<Split> splits;
    private SplitType splitType;
    private ExpenseCategory category;
    private LocalDateTime timestamp;
    private String notes;
    
    public Expense(String expenseId, String description, double amount, User paidBy, 
                   List<Split> splits, SplitType splitType, ExpenseCategory category) {
        this.expenseId = expenseId;
        this.description = description;
        this.amount = amount;
        this.paidBy = paidBy;
        this.splits = splits;
        this.splitType = splitType;
        this.category = category;
        this.timestamp = LocalDateTime.now();
        this.notes = "";
    }
    
    public void updateBalances() {
        for (Split split : splits) {
            User user = split.getUser();
            double splitAmount = split.getAmount();
            
            if (!user.equals(paidBy)) {
                // User owes paidBy the split amount
                // From user's perspective: negative balance (they owe)
                // From paidBy's perspective: positive balance (they are owed)
                user.addBalance(paidBy, -splitAmount);
            }
        }
    }
    
    public boolean validate() {
        // Check if total splits equal expense amount
        double totalSplit = splits.stream().mapToDouble(Split::getAmount).sum();
        return Math.abs(totalSplit - amount) < 0.01;
    }
    
    // Getters and Setters
    public String getExpenseId() { return expenseId; }
    public String getDescription() { return description; }
    public double getAmount() { return amount; }
    public User getPaidBy() { return paidBy; }
    public List<Split> getSplits() { return new ArrayList<>(splits); }
    public SplitType getSplitType() { return splitType; }
    public ExpenseCategory getCategory() { return category; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    @Override
    public String toString() {
        return String.format("Expense[%s]: %s - $%.2f paid by %s (%s split)", 
            expenseId, description, amount, paidBy.getName(), splitType);
    }
}
