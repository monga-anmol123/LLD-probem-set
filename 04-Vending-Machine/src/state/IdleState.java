package state;

import model.Money;
import service.VendingMachine;

/**
 * Idle state - waiting for money insertion.
 */
public class IdleState implements VendingMachineState {
    
    private final VendingMachine machine;
    
    public IdleState(VendingMachine machine) {
        this.machine = machine;
    }
    
    @Override
    public void insertMoney(Money money) {
        System.out.println("Money inserted: " + money);
        machine.addMoney(money);
        machine.setState(machine.getHasMoneyState());
    }
    
    @Override
    public void selectProduct(String productId) {
        System.out.println("Please insert money first!");
    }
    
    @Override
    public void dispense() {
        System.out.println("Please insert money and select product first!");
    }
    
    @Override
    public void refund() {
        System.out.println("No money to refund!");
    }
    
    @Override
    public String getStateName() {
        return "IDLE";
    }
}
