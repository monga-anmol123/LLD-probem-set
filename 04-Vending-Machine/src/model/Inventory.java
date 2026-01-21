package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Inventory manages products and change in the vending machine.
 */
public class Inventory {
    private final Map<String, Product> products;
    private final Map<Money, Integer> changeInventory;

    public Inventory() {
        this.products = new HashMap<>();
        this.changeInventory = new HashMap<>();
        initializeChangeInventory();
    }

    private void initializeChangeInventory() {
        // Initialize with some change
        changeInventory.put(Money.QUARTER, 20);
        changeInventory.put(Money.DIME, 20);
        changeInventory.put(Money.NICKEL, 20);
        changeInventory.put(Money.PENNY, 50);
        changeInventory.put(Money.ONE_DOLLAR, 10);
        changeInventory.put(Money.FIVE_DOLLAR, 5);
    }

    public void addProduct(Product product) {
        products.put(product.getId(), product);
    }

    public Product getProduct(String productId) {
        return products.get(productId);
    }

    public boolean hasProduct(String productId) {
        return products.containsKey(productId);
    }

    public Map<String, Product> getAllProducts() {
        return new HashMap<>(products);
    }

    public void addChange(Money money, int count) {
        changeInventory.put(money, changeInventory.getOrDefault(money, 0) + count);
    }

    public boolean hasChange(double amount) {
        // Simple check - in production would verify exact denominations
        double totalChange = 0;
        for (Map.Entry<Money, Integer> entry : changeInventory.entrySet()) {
            totalChange += entry.getKey().getValue() * entry.getValue();
        }
        return totalChange >= amount;
    }

    public void deductChange(Money money, int count) {
        int current = changeInventory.getOrDefault(money, 0);
        if (current >= count) {
            changeInventory.put(money, current - count);
        }
    }

    public int getChangeCount(Money money) {
        return changeInventory.getOrDefault(money, 0);
    }
}
