package service;

import model.*;
import factory.PaymentFactory;
import observer.BookingObserver;
import strategy.PricingStrategy;
import strategy.TimingBasedPricing;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Main booking system service (Singleton)
 * Coordinates all booking operations
 */
public class BookingSystem {
    private static BookingSystem instance;
    
    private Map<String, Theater> theaters;
    private Map<String, Movie> movies;
    private Map<String, Show> shows;
    private Map<String, Booking> bookings;
    private Map<String, User> users;
    private List<BookingObserver> observers;
    private PricingStrategy pricingStrategy;
    
    private int bookingCounter = 1;
    private int showCounter = 1;

    private BookingSystem() {
        this.theaters = new HashMap<>();
        this.movies = new HashMap<>();
        this.shows = new HashMap<>();
        this.bookings = new HashMap<>();
        this.users = new HashMap<>();
        this.observers = new ArrayList<>();
        this.pricingStrategy = new TimingBasedPricing(); // Default strategy
    }

    public static synchronized BookingSystem getInstance() {
        if (instance == null) {
            instance = new BookingSystem();
        }
        return instance;
    }

    // Observer management
    public void addObserver(BookingObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BookingObserver observer) {
        observers.remove(observer);
    }

    private void notifyBookingConfirmed(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingConfirmed(booking);
        }
    }

    private void notifyBookingCancelled(Booking booking) {
        for (BookingObserver observer : observers) {
            observer.onBookingCancelled(booking);
        }
    }

    // Pricing strategy management
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }

    // Theater management
    public void addTheater(Theater theater) {
        theaters.put(theater.getTheaterId(), theater);
    }

    public Theater getTheater(String theaterId) {
        return theaters.get(theaterId);
    }

    public List<Theater> getAllTheaters() {
        return new ArrayList<>(theaters.values());
    }

    // Movie management
    public void addMovie(Movie movie) {
        movies.put(movie.getMovieId(), movie);
    }

    public Movie getMovie(String movieId) {
        return movies.get(movieId);
    }

    public List<Movie> searchMovies(String keyword) {
        List<Movie> results = new ArrayList<>();
        for (Movie movie : movies.values()) {
            if (movie.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                movie.getGenre().toLowerCase().contains(keyword.toLowerCase())) {
                results.add(movie);
            }
        }
        return results;
    }

    // Show management
    public String createShow(Movie movie, Screen screen, java.time.LocalDateTime startTime) {
        String showId = "SHOW" + String.format("%05d", showCounter++);
        Show show = new Show(showId, movie, screen, startTime);
        shows.put(showId, show);
        screen.addShow(show);
        return showId;
    }

    public Show getShow(String showId) {
        return shows.get(showId);
    }

    public List<Show> getShowsForMovie(String movieId) {
        List<Show> movieShows = new ArrayList<>();
        for (Show show : shows.values()) {
            if (show.getMovie().getMovieId().equals(movieId)) {
                movieShows.add(show);
            }
        }
        return movieShows;
    }

    // User management
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User getUser(String userId) {
        return users.get(userId);
    }

    // Booking operations
    public Booking createBooking(User user, Show show, List<String> seatIds) {
        // Lock seats first
        if (!show.lockSeats(seatIds, user.getUserId(), 10)) {
            System.out.println("Failed to lock seats. Some seats may be unavailable.");
            return null;
        }

        // Calculate total amount
        double totalAmount = 0;
        List<Seat> bookedSeats = new ArrayList<>();
        for (String seatId : seatIds) {
            Seat seat = show.getSeat(seatId);
            if (seat != null) {
                double price = pricingStrategy.calculatePrice(seat, show);
                totalAmount += price;
                bookedSeats.add(seat);
            }
        }

        // Create booking
        String bookingId = "BKG" + String.format("%05d", bookingCounter++);
        Booking booking = new Booking(bookingId, user, show, bookedSeats, totalAmount);
        bookings.put(bookingId, booking);
        user.addBooking(booking);

        return booking;
    }

    public boolean confirmBooking(String bookingId, String paymentMethod) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return false;
        }

        // Process payment
        Payment payment = PaymentFactory.createPayment(
            booking, 
            booking.getTotalAmount(), 
            paymentMethod
        );
        
        if (!payment.process()) {
            System.out.println("Payment failed for booking: " + bookingId);
            booking.cancel(); // Release seats
            return false;
        }

        booking.setPayment(payment);
        booking.confirm();
        
        // Notify observers
        notifyBookingConfirmed(booking);
        
        return true;
    }

    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return false;
        }

        booking.cancel();
        
        // Notify observers
        notifyBookingCancelled(booking);
        
        return true;
    }

    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }

    public List<Booking> getUserBookings(String userId) {
        User user = users.get(userId);
        if (user != null) {
            return user.getBookingHistory();
        }
        return new ArrayList<>();
    }

    // Utility methods
    public void displayShowAvailability(String showId) {
        Show show = shows.get(showId);
        if (show == null) {
            System.out.println("Show not found: " + showId);
            return;
        }

        System.out.println("\n=== Show Availability ===");
        System.out.println("Show: " + show.getMovie().getTitle());
        System.out.println("Time: " + show.getStartTime());
        System.out.println("Screen: " + show.getScreen().getName());
        System.out.println("Available Seats: " + show.getAvailableSeatsCount() + 
                         "/" + show.getAllSeats().size());
        
        System.out.println("\nSeat Layout:");
        Map<String, List<Seat>> seatsByRow = new HashMap<>();
        for (Seat seat : show.getAllSeats()) {
            seatsByRow.computeIfAbsent(seat.getRow(), k -> new ArrayList<>()).add(seat);
        }
        
        for (String row : seatsByRow.keySet()) {
            System.out.print("Row " + row + ": ");
            for (Seat seat : seatsByRow.get(row)) {
                String status = "";
                switch (seat.getStatus()) {
                    case AVAILABLE: status = "✓"; break;
                    case LOCKED: status = "⏳"; break;
                    case BOOKED: status = "✗"; break;
                    case BLOCKED: status = "⊗"; break;
                }
                System.out.print(seat.getColumn() + status + " ");
            }
            System.out.println();
        }
    }

    public void displayBookingSummary(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("Booking not found: " + bookingId);
            return;
        }

        System.out.println("\n=== Booking Summary ===");
        System.out.println("Booking ID: " + booking.getBookingId());
        System.out.println("Status: " + booking.getStatus());
        System.out.println("User: " + booking.getUser().getName());
        System.out.println("Movie: " + booking.getShow().getMovie().getTitle());
        System.out.println("Show Time: " + booking.getShow().getStartTime());
        System.out.println("Theater: " + booking.getShow().getScreen().getTheater().getName());
        System.out.println("Screen: " + booking.getShow().getScreen().getName());
        System.out.println("Seats: " + booking.getSeats().size());
        for (Seat seat : booking.getSeats()) {
            System.out.println("  - " + seat);
        }
        System.out.println("Total Amount: $" + String.format("%.2f", booking.getTotalAmount()));
        if (booking.getPayment() != null) {
            System.out.println("Payment: " + booking.getPayment().getMethod() + 
                             " - " + booking.getPayment().getStatus());
        }
    }
}


