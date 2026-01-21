package state;

import model.Money;
import model.Product;
import service.VendingMachine;

/**
 * Dispensing state - product being dispensed.
 */
public class DispensingState implements VendingMachineState {
    
    private final VendingMachine machine;
    
    public DispensingState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void insertMoney(Money money) {
        System.out.println("Please wait, dispensing product...");
    }
    
    @Override
    public void selectProduct(String productId) {
        System.out.println("Already dispensing a product!");
    }
    
    @Override
    public void dispense() {
        Product product = machine.getSelectedProduct();
        
        if (product == null) {
            System.out.println("Error: No product selected!");
            machine.setState(machine.getIdleState());
            return;
        }
        
        System.out.println("\n=== Dispensing Product ===");
        System.out.println("Product: " + product.getName());
        System.out.printf("Price: $%.2f%n", product.getPrice());
        
        // Dispense product
        product.decrementQuantity();
        System.out.println("✓ Product dispensed!");
        
        // Calculate and return change
        double change = machine.getCurrentAmount() - product.getPrice();
        if (change > 0) {
            System.out.printf("Returning change: $%.2f%n", change);
            machine.returnChange(change);
        }
        
        System.out.println("=========================\n");
        
        // Reset machine
        machine.reset();
        machine.setState(machine.getIdleState());
    }
    
    @Override
    public void refund() {
        System.out.println("Cannot refund during dispensing!");
    }
    
    @Override
    public String getStateName() {
        return "DISPENSING";
    }
}
