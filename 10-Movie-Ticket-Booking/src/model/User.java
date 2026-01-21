package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a user in the booking system
 */
public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private List<Booking> bookingHistory;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.bookingHistory = new ArrayList<>();
    }

    public void addBooking(Booking booking) {
        bookingHistory.add(booking);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public List<Booking> getBookingHistory() {
        return new ArrayList<>(bookingHistory);
    }

    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                '}';
    }
}


