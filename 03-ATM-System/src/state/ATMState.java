package state;

import model.Card;

public interface ATMState {
    void insertCard(Card card);
    void enterPIN(String pin);
    void selectOperation(String operation);
    void ejectCard();
    String getStateName();
}


