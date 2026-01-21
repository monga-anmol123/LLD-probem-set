package state;

import model.Card;
import service.ATM;

public class IdleState implements ATMState {
    private ATM atm;

    public IdleState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        if (card.isBlocked()) {
            System.out.println("❌ This card is blocked. Please contact your bank.");
            return;
        }
        
        System.out.println("✓ Card inserted: " + card.maskCardNumber());
        System.out.println("Please enter your 4-digit PIN:");
        atm.setCurrentCard(card);
        atm.setState(new CardInsertedState(atm));
    }

    @Override
    public void enterPIN(String pin) {
        System.out.println("❌ Please insert your card first.");
    }

    @Override
    public void selectOperation(String operation) {
        System.out.println("❌ Please insert your card first.");
    }

    @Override
    public void ejectCard() {
        System.out.println("❌ No card to eject.");
    }

    @Override
    public String getStateName() {
        return "IDLE";
    }
}


