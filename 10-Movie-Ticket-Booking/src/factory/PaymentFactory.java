package factory;

import model.Payment;
import model.Booking;

/**
 * Factory for creating different types of payments
 * Demonstrates Factory Pattern
 */
public class PaymentFactory {
    
    private static int paymentCounter = 1;

    /**
     * Create a payment with specified method
     * @param booking The booking for which payment is being made
     * @param amount Amount to be paid
     * @param method Payment method (CREDIT_CARD, DEBIT_CARD, UPI, NET_BANKING, WALLET)
     * @return Payment object
     */
    public static Payment createPayment(Booking booking, double amount, String method) {
        String paymentId = "PAY" + String.format("%05d", paymentCounter++);
        return new Payment(paymentId, booking, amount, method);
    }

    /**
     * Create a credit card payment
     */
    public static Payment createCreditCardPayment(Booking booking, double amount) {
        return createPayment(booking, amount, "CREDIT_CARD");
    }

    /**
     * Create a debit card payment
     */
    public static Payment createDebitCardPayment(Booking booking, double amount) {
        return createPayment(booking, amount, "DEBIT_CARD");
    }

    /**
     * Create a UPI payment
     */
    public static Payment createUPIPayment(Booking booking, double amount) {
        return createPayment(booking, amount, "UPI");
    }

    /**
     * Create a net banking payment
     */
    public static Payment createNetBankingPayment(Booking booking, double amount) {
        return createPayment(booking, amount, "NET_BANKING");
    }

    /**
     * Create a wallet payment
     */
    public static Payment createWalletPayment(Booking booking, double amount) {
        return createPayment(booking, amount, "WALLET");
    }
}


