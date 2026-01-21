package state;

import model.Money;

/**
 * State interface for vending machine states.
 */
public interface VendingMachineState {
    
    /**
     * Handle money insertion.
     */
    void insertMoney(Money money);
    
    /**
     * Handle product selection.
     */
    void selectProduct(String productId);
    
    /**
     * Handle product dispensing.
     */
    void dispense();
    
    /**
     * Handle refund request.
     */
    void refund();
    
    /**
     * Get state name.
     */
    String getStateName();
}
