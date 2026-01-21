package service;

import model.Inventory;
import model.Money;
import model.Product;
import state.*;

import java.util.ArrayList;
import java.util.List;

/**
 * VendingMachine - main service class using State pattern.
 */
public class VendingMachine {
    
    private static VendingMachine instance;
    
    private final Inventory inventory;
    private VendingMachineState currentState;
    private double currentAmount;
    private Product selectedProduct;
    
    // States
    private final VendingMachineState idleState;
    private final VendingMachineState hasMoneyState;
    private final VendingMachineState dispensingState;
    
    private VendingMachine() {
        this.inventory = new Inventory();
        this.currentAmount = 0.0;
        
        // Initialize states
        this.idleState = new IdleState(this);
        this.hasMoneyState = new HasMoneyState(this);
        this.dispensingState = new DispensingState(this);
        
        // Start in idle state
        this.currentState = idleState;
    }
    
    /**
     * Get singleton instance.
     */
    public static synchronized VendingMachine getInstance() {
        if (instance == null) {
            instance = new VendingMachine();
        }
        return instance;
    }
    
    /**
     * Insert money into the machine.
     */
    public void insertMoney(Money money) {
        currentState.insertMoney(money);
    }
    
    /**
     * Select a product.
     */
    public void selectProduct(String productId) {
        currentState.selectProduct(productId);
    }
    
    /**
     * Request refund.
     */
    public void refund() {
        currentState.refund();
    }
    
    /**
     * Add money to current amount (called by state).
     */
    public void addMoney(Money money) {
        currentAmount += money.getValue();
        inventory.addChange(money, 1);
        System.out.printf("Current balance: $%.2f%n", currentAmount);
    }
    
    /**
     * Return all money to user.
     */
    public void returnMoney() {
        if (currentAmount > 0) {
            System.out.printf("Returned: $%.2f%n", currentAmount);
            currentAmount = 0.0;
        }
    }
    
    /**
     * Return change to user.
     */
    public void returnChange(double amount) {
        if (amount <= 0) return;
        
        List<Money> change = calculateChange(amount);
        System.out.println("Change breakdown:");
        for (Money money : change) {
            System.out.println("  " + money);
        }
    }
    
    /**
     * Calculate change using greedy algorithm.
     */
    private List<Money> calculateChange(double amount) {
        List<Money> change = new ArrayList<>();
        double remaining = Math.round(amount * 100.0) / 100.0;
        
        Money[] denominations = {
            Money.TWENTY_DOLLAR, Money.TEN_DOLLAR, Money.FIVE_DOLLAR, 
            Money.ONE_DOLLAR, Money.QUARTER, Money.DIME, Money.NICKEL, Money.PENNY
        };
        
        for (Money denom : denominations) {
            while (remaining >= denom.getValue() - 0.001 && inventory.getChangeCount(denom) > 0) {
                change.add(denom);
                inventory.deductChange(denom, 1);
                remaining = Math.round((remaining - denom.getValue()) * 100.0) / 100.0;
            }
        }
        
        return change;
    }
    
    /**
     * Reset machine state.
     */
    public void reset() {
        currentAmount = 0.0;
        selectedProduct = null;
    }
    
    /**
     * Display available products.
     */
    public void displayProducts() {
        System.out.println("\n=== Available Products ===");
        for (Product product : inventory.getAllProducts().values()) {
            System.out.println(product);
        }
        System.out.println("==========================\n");
    }
    
    /**
     * Display current status.
     */
    public void displayStatus() {
        System.out.println("\n--- Vending Machine Status ---");
        System.out.println("State: " + currentState.getStateName());
        System.out.printf("Current Balance: $%.2f%n", currentAmount);
        System.out.println("------------------------------\n");
    }
    
    // Getters and setters
    public Inventory getInventory() {
        return inventory;
    }
    
    public double getCurrentAmount() {
        return currentAmount;
    }
    
    public Product getSelectedProduct() {
        return selectedProduct;
    }
    
    public void setSelectedProduct(Product product) {
        this.selectedProduct = product;
    }
    
    public void setState(VendingMachineState state) {
        this.currentState = state;
    }
    
    public VendingMachineState getIdleState() {
        return idleState;
    }
    
    public VendingMachineState getHasMoneyState() {
        return hasMoneyState;
    }
    
    public VendingMachineState getDispensingState() {
        return dispensingState;
    }
}
