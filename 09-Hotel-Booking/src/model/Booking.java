package model;

import enums.BookingStatus;
import enums.PaymentStatus;
import observer.Observer;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class Booking {
    private final String bookingId;
    private final Guest guest;
    private final Room room;
    private final LocalDate checkInDate;
    private final LocalDate checkOutDate;
    private final double totalPrice;
    private BookingStatus status;
    private PaymentStatus paymentStatus;
    private final List<Observer> observers;
    
    public Booking(String bookingId, Guest guest, Room room, 
                   LocalDate checkInDate, LocalDate checkOutDate, double totalPrice) {
        this.bookingId = bookingId;
        this.guest = guest;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.totalPrice = totalPrice;
        this.status = BookingStatus.CONFIRMED;
        this.paymentStatus = PaymentStatus.PENDING;
        this.observers = new ArrayList<>();
    }
    
    public String getBookingId() {
        return bookingId;
    }
    
    public Guest getGuest() {
        return guest;
    }
    
    public Room getRoom() {
        return room;
    }
    
    public LocalDate getCheckInDate() {
        return checkInDate;
    }
    
    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }
    
    public long getNumberOfNights() {
        return ChronoUnit.DAYS.between(checkInDate, checkOutDate);
    }
    
    public double getTotalPrice() {
        return totalPrice;
    }
    
    public BookingStatus getStatus() {
        return status;
    }
    
    public void setStatus(BookingStatus status) {
        this.status = status;
        notifyObservers("Booking " + bookingId + " status changed to: " + status);
    }
    
    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }
    
    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
        notifyObservers("Booking " + bookingId + " payment status: " + paymentStatus);
    }
    
    public void addObserver(Observer observer) {
        observers.add(observer);
    }
    
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }
    
    private void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(this, message);
        }
    }
    
    public boolean overlaps(LocalDate startDate, LocalDate endDate) {
        return !checkOutDate.isBefore(startDate) && !checkInDate.isAfter(endDate);
    }
    
    @Override
    public String toString() {
        return String.format("Booking[%s]: %s - Room %s (%s to %s) - $%.2f - Status: %s", 
            bookingId, guest.getName(), room.getRoomNumber(), 
            checkInDate, checkOutDate, totalPrice, status);
    }
}
