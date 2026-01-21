package state;

import model.Card;
import service.ATM;

public class BlockedState implements ATMState {
    private ATM atm;

    public BlockedState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("❌ ATM is temporarily blocked. Please try again later.");
    }

    @Override
    public void enterPIN(String pin) {
        System.out.println("❌ Card is blocked. Please contact your bank.");
    }

    @Override
    public void selectOperation(String operation) {
        System.out.println("❌ Card is blocked. Please contact your bank.");
    }

    @Override
    public void ejectCard() {
        System.out.println("✓ Card ejected.");
        atm.setCurrentCard(null);
        atm.resetPinAttempts();
        atm.setState(new IdleState(atm));
    }

    @Override
    public String getStateName() {
        return "BLOCKED";
    }
}


