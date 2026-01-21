package state;

import model.Card;
import service.ATM;

public class PINVerifiedState implements ATMState {
    private ATM atm;

    public PINVerifiedState(ATM atm) {
        this.atm = atm;
    }

    @Override
    public void insertCard(Card card) {
        System.out.println("❌ A card is already inserted. Please complete your transaction or eject the card.");
    }

    @Override
    public void enterPIN(String pin) {
        System.out.println("❌ PIN already verified. Please select an operation.");
    }

    @Override
    public void selectOperation(String operation) {
        System.out.println("✓ Processing operation: " + operation);
        atm.setState(new TransactionState(atm));
        
        switch (operation.toUpperCase()) {
            case "WITHDRAW":
                // Handled by ATM.withdraw()
                break;
            case "DEPOSIT":
                // Handled by ATM.deposit()
                break;
            case "BALANCE":
                // Handled by ATM.checkBalance()
                break;
            case "CHANGE_PIN":
                // Handled by ATM.changePIN()
                break;
            default:
                System.out.println("❌ Invalid operation.");
                atm.setState(new PINVerifiedState(atm));
        }
    }

    @Override
    public void ejectCard() {
        System.out.println("✓ Thank you for using our ATM. Card ejected.");
        atm.setCurrentCard(null);
        atm.resetPinAttempts();
        atm.setState(new IdleState(atm));
    }

    @Override
    public String getStateName() {
        return "PIN_VERIFIED";
    }
}


