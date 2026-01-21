package state;

import model.Card;
import service.ATM;

public class CardInsertedState implements ATMState {
    private ATM atm;

    public CardInsertedState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("❌ A card is already inserted. Please complete your transaction or eject the card.");
    }

    @Override
    public void enterPIN(String pin) {
        Card currentCard = atm.getCurrentCard();
        
        if (currentCard.validatePIN(pin)) {
            System.out.println("✓ PIN verified successfully.");
            atm.resetPinAttempts();
            atm.setState(new PINVerifiedState(atm));
            atm.displayMenu();
        } else {
            atm.incrementPinAttempts();
            int remainingAttempts = atm.getRemainingPinAttempts();
            
            if (remainingAttempts > 0) {
                System.out.println("❌ Incorrect PIN. " + remainingAttempts + " attempt(s) remaining.");
                System.out.println("Please try again:");
            } else {
                System.out.println("❌ Card blocked due to 3 failed PIN attempts. Please contact your bank.");
                currentCard.block();
                atm.setState(new BlockedState(atm));
                ejectCard();
            }
        }
    }

    @Override
    public void selectOperation(String operation) {
        System.out.println("❌ Please enter your PIN first.");
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
        return "CARD_INSERTED";
    }
}


