package model;

import enums.PaymentStatus;
import java.time.LocalDateTime;

/**
 * Represents a payment for a booking
 */
public class Payment {
    private String paymentId;
    private Booking booking;
    private double amount;
    private String method;
    private PaymentStatus status;
    private LocalDateTime paymentTime;

    public Payment(String paymentId, Booking booking, double amount, String method) {
        this.paymentId = paymentId;
        this.booking = booking;
        this.amount = amount;
        this.method = method;
        this.status = PaymentStatus.PENDING;
        this.paymentTime = null;
    }

    public boolean process() {
        // Simulate payment processing
        try {
            // In real system, this would call payment gateway
            Thread.sleep(100); // Simulate network delay
            this.status = PaymentStatus.SUCCESS;
            this.paymentTime = LocalDateTime.now();
            return true;
        } catch (InterruptedException e) {
            this.status = PaymentStatus.FAILED;
            return false;
        }
    }

    public boolean refund() {
        if (status == PaymentStatus.SUCCESS) {
            // Simulate refund processing
            this.status = PaymentStatus.REFUNDED;
            return true;
        }
        return false;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Booking getBooking() {
        return booking;
    }

    public double getAmount() {
        return amount;
    }

    public String getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getPaymentTime() {
        return paymentTime;
    }

    @Override
    public String toString() {
        return "Payment{" +
                "paymentId='" + paymentId + '\'' +
                ", amount=$" + String.format("%.2f", amount) +
                ", method='" + method + '\'' +
                ", status=" + status +
                ", paymentTime=" + paymentTime +
                '}';
    }
}


