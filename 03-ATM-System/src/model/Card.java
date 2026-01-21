package model;

public class Card {
    private String cardNumber;
    private String accountNumber;
    private String pin;
    private boolean isBlocked;

    public Card(String cardNumber, String accountNumber, String pin) {
        this.cardNumber = cardNumber;
        this.accountNumber = accountNumber;
        this.pin = pin;
        this.isBlocked = false;
    }

    public boolean validatePIN(String inputPin) {
        return this.pin.equals(inputPin);
    }

    public void changePIN(String oldPin, String newPin) {
        if (validatePIN(oldPin)) {
            this.pin = newPin;
        } else {
            throw new IllegalArgumentException("Old PIN is incorrect");
        }
    }

    public void block() {
        this.isBlocked = true;
    }

    public void unblock() {
        this.isBlocked = false;
    }

    // Getters
    public String getCardNumber() {
        return cardNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public String maskCardNumber() {
        if (cardNumber.length() < 4) {
            return "****";
        }
        return "****-****-****-" + cardNumber.substring(cardNumber.length() - 4);
    }
}


