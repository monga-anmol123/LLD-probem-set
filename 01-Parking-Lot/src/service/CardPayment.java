package service;

public class CardPayment implements Payment {
    private String cardNumber;
    
    public CardPayment(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing card payment: $" + amount + " using card: " + cardNumber);
        return true;
    }
}


