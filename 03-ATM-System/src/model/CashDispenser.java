package model;

import strategy.CashDispensingStrategy;
import java.util.HashMap;
import java.util.Map;

public class CashDispenser {
    private Map<Integer, Integer> denominations; // denomination -> count
    private CashDispensingStrategy strategy;

    public CashDispenser(CashDispensingStrategy strategy) {
        this.denominations = new HashMap<>();
        this.strategy = strategy;
        initializeDenominations();
    }

    private void initializeDenominations() {
        denominations.put(2000, 10);
        denominations.put(1000, 20);
        denominations.put(500, 30);
        denominations.put(100, 50);
    }

    public boolean canDispense(double amount) {
        if (amount <= 0 || amount % 100 != 0) {
            return false;
        }
        
        if (getTotalCash() < amount) {
            return false;
        }
        
        Map<Integer, Integer> result = strategy.dispenseCash((int) amount, new HashMap<>(denominations));
        return result != null;
    }

    public Map<Integer, Integer> dispenseCash(double amount) {
        if (!canDispense(amount)) {
            return null;
        }

        Map<Integer, Integer> dispensed = strategy.dispenseCash((int) amount, new HashMap<>(denominations));
        
        if (dispensed != null) {
            // Update available denominations
            for (Map.Entry<Integer, Integer> entry : dispensed.entrySet()) {
                int denom = entry.getKey();
                int count = entry.getValue();
                denominations.put(denom, denominations.get(denom) - count);
            }
        }
        
        return dispensed;
    }

    public void addCash(int denomination, int count) {
        if (denominations.containsKey(denomination)) {
            denominations.put(denomination, denominations.get(denomination) + count);
        } else {
            denominations.put(denomination, count);
        }
    }

    public double getTotalCash() {
        double total = 0;
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            total += entry.getKey() * entry.getValue();
        }
        return total;
    }

    public void displayCashStatus() {
        System.out.println("\n--- ATM Cash Status ---");
        System.out.println("Denomination | Count | Total");
        System.out.println("-------------|-------|-------");
        for (Map.Entry<Integer, Integer> entry : denominations.entrySet()) {
            int denom = entry.getKey();
            int count = entry.getValue();
            System.out.printf("$%-11d | %-5d | $%d%n", denom, count, denom * count);
        }
        System.out.printf("Total Cash Available: $%.0f%n", getTotalCash());
        System.out.println("----------------------\n");
    }

    public void setStrategy(CashDispensingStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<Integer, Integer> getDenominations() {
        return new HashMap<>(denominations);
    }
}


