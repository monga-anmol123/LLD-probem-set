package model;

import java.util.HashMap;
import java.util.Map;

public class BalanceSheet {
    private User user;
    private Map<User, Double> balances;
    
    public BalanceSheet(User user) {
        this.user = user;
        this.balances = user.getBalances();
    }
    
    public double getTotalOwed() {
        // Positive balances = others owe me
        return balances.values().stream()
            .filter(amount -> amount > 0.01)
            .mapToDouble(Double::doubleValue)
            .sum();
    }
    
    public double getTotalOwing() {
        // Negative balances = I owe others
        return Math.abs(balances.values().stream()
            .filter(amount -> amount < -0.01)
            .mapToDouble(Double::doubleValue)
            .sum());
    }
    
    public double getNetBalance() {
        return getTotalOwed() - getTotalOwing();
    }
    
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n╔════════════════════════════════════════════════════════════╗\n");
        sb.append(String.format("║  BALANCE SHEET: %-42s ║\n", user.getName()));
        sb.append("╠════════════════════════════════════════════════════════════╣\n");
        
        // People who owe me
        Map<User, Double> positiveBalances = user.getPositiveBalances();
        if (!positiveBalances.isEmpty()) {
            sb.append("║  YOU ARE OWED:                                             ║\n");
            for (Map.Entry<User, Double> entry : positiveBalances.entrySet()) {
                sb.append(String.format("║    %-40s $%10.2f ║\n", 
                    entry.getKey().getName(), entry.getValue()));
            }
            sb.append(String.format("║  Total Owed to You:                            $%10.2f ║\n", 
                getTotalOwed()));
            sb.append("║                                                            ║\n");
        }
        
        // People I owe
        Map<User, Double> negativeBalances = user.getNegativeBalances();
        if (!negativeBalances.isEmpty()) {
            sb.append("║  YOU OWE:                                                  ║\n");
            for (Map.Entry<User, Double> entry : negativeBalances.entrySet()) {
                sb.append(String.format("║    %-40s $%10.2f ║\n", 
                    entry.getKey().getName(), Math.abs(entry.getValue())));
            }
            sb.append(String.format("║  Total You Owe:                                $%10.2f ║\n", 
                getTotalOwing()));
            sb.append("║                                                            ║\n");
        }
        
        if (positiveBalances.isEmpty() && negativeBalances.isEmpty()) {
            sb.append("║  All settled up! No outstanding balances.                  ║\n");
            sb.append("║                                                            ║\n");
        }
        
        double netBalance = getNetBalance();
        String netStatus = netBalance > 0.01 ? "gets back" : (netBalance < -0.01 ? "owes" : "settled");
        sb.append(String.format("║  NET BALANCE: %-33s $%10.2f ║\n", 
            netStatus, Math.abs(netBalance)));
        sb.append("╚════════════════════════════════════════════════════════════╝\n");
        
        return sb.toString();
    }
    
    // Getters
    public User getUser() { return user; }
    public Map<User, Double> getBalances() { return new HashMap<>(balances); }
}
