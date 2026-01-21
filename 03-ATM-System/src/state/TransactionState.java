package state;

import model.Card;
import service.ATM;

public class TransactionState implements ATMState {
    private ATM atm;

    public TransactionState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("❌ Transaction in progress. Please wait.");
    }

    @Override
    public void enterPIN(String pin) {
        System.out.println("❌ Transaction in progress. Please wait.");
    }

    @Override
    public void selectOperation(String operation) {
        System.out.println("❌ Transaction in progress. Please wait.");
    }

    @Override
    public void ejectCard() {
        System.out.println("❌ Cannot eject card during transaction. Please wait.");
    }

    public void completeTransaction() {
        System.out.println("✓ Transaction completed.");
        atm.setState(new PINVerifiedState(atm));
    }

    @Override
    public String getStateName() {
        return "TRANSACTION";
    }
}


