package model;

import enums.BookingStatus;
import state.BookingState;
import state.PendingState;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a booking made by a user
 */
public class Booking {
    private String bookingId;
    private User user;
    private Show show;
    private List<Seat> seats;
    private double totalAmount;
    private BookingStatus status;
    private LocalDateTime bookingTime;
    private BookingState state;
    private Payment payment;

    public Booking(String bookingId, User user, Show show, List<Seat> seats, double totalAmount) {
        this.bookingId = bookingId;
        this.user = user;
        this.show = show;
        this.seats = new ArrayList<>(seats);
        this.totalAmount = totalAmount;
        this.status = BookingStatus.PENDING;
        this.bookingTime = LocalDateTime.now();
        this.state = new PendingState();
    }

    public void confirm() {
        state.confirm(this);
    }

    public void cancel() {
        state.cancel(this);
    }

    public void setState(BookingState state) {
        this.state = state;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void applyDiscount(double discountPercentage) {
        totalAmount = totalAmount * (1 - discountPercentage / 100.0);
    }

    public List<String> getSeatIds() {
        List<String> seatIds = new ArrayList<>();
        for (Seat seat : seats) {
            seatIds.add(seat.getSeatId());
        }
        return seatIds;
    }

    public String getBookingId() {
        return bookingId;
    }

    public User getUser() {
        return user;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return new ArrayList<>(seats);
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public LocalDateTime getBookingTime() {
        return bookingTime;
    }

    public Payment getPayment() {
        return payment;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "bookingId='" + bookingId + '\'' +
                ", user=" + user.getName() +
                ", show=" + show.getMovie().getTitle() +
                ", seats=" + seats.size() +
                ", totalAmount=$" + String.format("%.2f", totalAmount) +
                ", status=" + status +
                ", bookingTime=" + bookingTime +
                '}';
    }
}


