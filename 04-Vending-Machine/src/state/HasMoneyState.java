package state;

import model.Money;
import model.Product;
import service.VendingMachine;

/**
 * HasMoney state - money inserted, waiting for product selection.
 */
public class HasMoneyState implements VendingMachineState {
    
    private final VendingMachine machine;
    
    public HasMoneyState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void insertMoney(Money money) {
        System.out.println("Additional money inserted: " + money);
        machine.addMoney(money);
    }
    
    @Override
    public void selectProduct(String productId) {
        Product product = machine.getInventory().getProduct(productId);
        
        if (product == null) {
            System.out.println("Invalid product selection!");
            return;
        }
        
        if (!product.isAvailable()) {
            System.out.println("Product out of stock!");
            return;
        }
        
        if (machine.getCurrentAmount() < product.getPrice()) {
            double needed = product.getPrice() - machine.getCurrentAmount();
            System.out.printf("Insufficient payment! Need $%.2f more.%n", needed);
            return;
        }
        
        machine.setSelectedProduct(product);
        machine.setState(machine.getDispensingState());
        machine.getDispensingState().dispense();
    }
    
    @Override
    public void dispense() {
        System.out.println("Please select a product first!");
    }
    
    @Override
    public void refund() {
        System.out.println("Refunding money...");
        machine.returnMoney();
        machine.setState(machine.getIdleState());
    }
    
    @Override
    public String getStateName() {
        return "HAS_MONEY";
    }
}
