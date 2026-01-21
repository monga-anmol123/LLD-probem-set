package service;

import model.Transaction;
import model.User;
import java.util.*;

public class DebtSimplifier {
    
    /**
     * CRITICAL: Debt Simplification Algorithm
     * 
     * Problem: Given a set of users with balances (who owes whom), minimize the number
     * of transactions needed to settle all debts.
     * 
     * Algorithm: Greedy approach using priority queues
     * 1. Calculate net balance for each user
     * 2. Separate into creditors (positive balance) and debtors (negative balance)
     * 3. Use max-heap for creditors and min-heap for debtors
     * 4. Match largest creditor with largest debtor iteratively
     * 5. Settle as much as possible in each transaction
     * 
     * Time Complexity: O(n log n) where n = number of users
     * Space Complexity: O(n)
     * 
     * This reduces transactions from O(n²) to O(n) in most cases.
     */
    public List<Transaction> simplifyDebts(List<User> users) {
        // Step 1: Calculate net balances for all users
        Map<User, Double> netBalances = calculateNetBalances(users);
        
        // Step 2: Separate creditors and debtors
        // Creditors: users with positive balance (others owe them)
        // Debtors: users with negative balance (they owe others)
        PriorityQueue<UserBalance> creditors = new PriorityQueue<>((a, b) -> 
            Double.compare(b.amount, a.amount)); // Max heap
        
        PriorityQueue<UserBalance> debtors = new PriorityQueue<>((a, b) -> 
            Double.compare(a.amount, b.amount)); // Min heap (most negative first)
        
        for (Map.Entry<User, Double> entry : netBalances.entrySet()) {
            double balance = entry.getValue();
            if (balance > 0.01) {
                creditors.add(new UserBalance(entry.getKey(), balance));
            } else if (balance < -0.01) {
                debtors.add(new UserBalance(entry.getKey(), balance));
            }
        }
        
        // Step 3: Match creditors with debtors to minimize transactions
        List<Transaction> simplifiedTransactions = new ArrayList<>();
        
        while (!creditors.isEmpty() && !debtors.isEmpty()) {
            UserBalance creditor = creditors.poll();
            UserBalance debtor = debtors.poll();
            
            // Settle as much as possible
            double settleAmount = Math.min(creditor.amount, Math.abs(debtor.amount));
            
            // Create transaction: debtor pays creditor
            Transaction transaction = new Transaction(debtor.user, creditor.user, settleAmount);
            transaction.setDescription("Simplified debt settlement");
            simplifiedTransactions.add(transaction);
            
            // Update remaining balances
            creditor.amount -= settleAmount;
            debtor.amount += settleAmount;
            
            // Re-add to queues if not fully settled
            if (creditor.amount > 0.01) {
                creditors.add(creditor);
            }
            if (debtor.amount < -0.01) {
                debtors.add(debtor);
            }
        }
        
        return simplifiedTransactions;
    }
    
    /**
     * Calculate net balance for each user
     * Net balance = Sum of all balances with other users
     * Positive = others owe them
     * Negative = they owe others
     */
    private Map<User, Double> calculateNetBalances(List<User> users) {
        Map<User, Double> netBalances = new HashMap<>();
        
        for (User user : users) {
            double netBalance = user.getTotalBalance();
            netBalances.put(user, netBalance);
        }
        
        return netBalances;
    }
    
    /**
     * Get all pairwise transactions (before simplification)
     * This shows the direct debts between users
     */
    public List<Transaction> getAllTransactions(List<User> users) {
        List<Transaction> transactions = new ArrayList<>();
        Set<String> processed = new HashSet<>();
        
        for (User user : users) {
            Map<User, Double> balances = user.getBalances();
            
            for (Map.Entry<User, Double> entry : balances.entrySet()) {
                User other = entry.getKey();
                double amount = entry.getValue();
                
                // Create unique key for this pair
                String key = getTransactionKey(user, other);
                
                if (!processed.contains(key) && Math.abs(amount) > 0.01) {
                    if (amount < 0) {
                        // user owes other
                        transactions.add(new Transaction(user, other, Math.abs(amount)));
                    }
                    processed.add(key);
                }
            }
        }
        
        return transactions;
    }
    
    private String getTransactionKey(User u1, User u2) {
        // Create unique key regardless of order
        String id1 = u1.getUserId();
        String id2 = u2.getUserId();
        return id1.compareTo(id2) < 0 ? id1 + "-" + id2 : id2 + "-" + id1;
    }
    
    /**
     * Helper class to store user and their balance
     */
    private static class UserBalance {
        User user;
        double amount;
        
        UserBalance(User user, double amount) {
            this.user = user;
            this.amount = amount;
        }
    }
    
    /**
     * Validate that debt simplification is correct
     * Sum of all balances should be zero
     */
    public boolean validateBalances(List<User> users) {
        double totalBalance = users.stream()
            .mapToDouble(User::getTotalBalance)
            .sum();
        
        return Math.abs(totalBalance) < 0.01;
    }
    
    /**
     * Display comparison between original and simplified transactions
     */
    public String compareTransactions(List<Transaction> original, List<Transaction> simplified) {
        StringBuilder sb = new StringBuilder();
        
        sb.append("\n╔════════════════════════════════════════════════════════════╗\n");
        sb.append("║           DEBT SIMPLIFICATION COMPARISON                   ║\n");
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        
        sb.append(String.format("║  Original Transactions:  %-33d ║\n", original.size()));
        sb.append(String.format("║  Simplified Transactions: %-32d ║\n", simplified.size()));
        sb.append(String.format("║  Reduction: %-46d ║\n", original.size() - simplified.size()));
        
        double reductionPercent = original.size() > 0 ? 
            (original.size() - simplified.size()) * 100.0 / original.size() : 0;
        sb.append(String.format("║  Reduction Percentage: %.1f%%                              ║\n", reductionPercent));
        
        sb.append("╚════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
}
