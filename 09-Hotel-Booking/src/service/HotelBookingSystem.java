package service;

import model.*;
import enums.*;
import strategy.*;
import observer.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class HotelBookingSystem {
    private static HotelBookingSystem instance;
    
    private final Map<String, Room> rooms;
    private final Map<String, Guest> guests;
    private final Map<String, Booking> bookings;
    private final Map<String, List<Booking>> roomBookings; // roomNumber -> bookings
    private PricingStrategy defaultPricingStrategy;
    private long bookingCounter;
    
    private HotelBookingSystem() {
        this.rooms = new ConcurrentHashMap<>();
        this.guests = new ConcurrentHashMap<>();
        this.bookings = new ConcurrentHashMap<>();
        this.roomBookings = new ConcurrentHashMap<>();
        this.defaultPricingStrategy = new BasePricingStrategy();
        this.bookingCounter = 1000;
    }
    
    public static synchronized HotelBookingSystem getInstance() {
        if (instance == null) {
            instance = new HotelBookingSystem();
        }
        return instance;
    }
    
    public void addRoom(Room room) {
        rooms.put(room.getRoomNumber(), room);
        roomBookings.put(room.getRoomNumber(), new ArrayList<>());
    }
    
    public void registerGuest(Guest guest) {
        guests.put(guest.getGuestId(), guest);
    }
    
    public void setDefaultPricingStrategy(PricingStrategy strategy) {
        this.defaultPricingStrategy = strategy;
    }
    
    public synchronized Booking createBooking(String guestId, String roomNumber, 
                                             LocalDate checkInDate, LocalDate checkOutDate) {
        return createBooking(guestId, roomNumber, checkInDate, checkOutDate, null);
    }
    
    public synchronized Booking createBooking(String guestId, String roomNumber, 
                                             LocalDate checkInDate, LocalDate checkOutDate,
                                             PricingStrategy pricingStrategy) {
        // Validate inputs
        if (checkInDate.isAfter(checkOutDate) || checkInDate.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Invalid dates");
        }
        
        Guest guest = guests.get(guestId);
        if (guest == null) {
            throw new IllegalArgumentException("Guest not found");
        }
        
        Room room = rooms.get(roomNumber);
        if (room == null) {
            throw new IllegalArgumentException("Room not found");
        }
        
        // Check availability (prevent double booking)
        if (!isRoomAvailable(roomNumber, checkInDate, checkOutDate)) {
            throw new IllegalStateException("Room not available for selected dates");
        }
        
        // Calculate price
        PricingStrategy strategy = pricingStrategy != null ? pricingStrategy : defaultPricingStrategy;
        double totalPrice = strategy.calculatePrice(room, checkInDate, checkOutDate);
        
        // Create booking
        String bookingId = "BK" + (bookingCounter++);
        Booking booking = new Booking(bookingId, guest, room, checkInDate, checkOutDate, totalPrice);
        
        // Store booking
        bookings.put(bookingId, booking);
        roomBookings.get(roomNumber).add(booking);
        
        // Add loyalty points
        int points = (int)(totalPrice / 10);
        guest.addLoyaltyPoints(points);
        
        System.out.println("✓ Booking created: " + bookingId);
        System.out.println("  Guest: " + guest.getName());
        System.out.println("  Room: " + roomNumber + " (" + room.getRoomType() + ")");
        System.out.println("  Dates: " + checkInDate + " to " + checkOutDate);
        System.out.println("  Nights: " + booking.getNumberOfNights());
        System.out.println("  Total: $" + String.format("%.2f", totalPrice));
        System.out.println("  Pricing: " + strategy.getStrategyName());
        System.out.println("  Loyalty points earned: " + points);
        
        return booking;
    }
    
    public boolean isRoomAvailable(String roomNumber, LocalDate checkInDate, LocalDate checkOutDate) {
        List<Booking> existingBookings = roomBookings.get(roomNumber);
        if (existingBookings == null) {
            return false;
        }
        
        for (Booking booking : existingBookings) {
            if (booking.getStatus() != BookingStatus.CANCELLED && 
                booking.overlaps(checkInDate, checkOutDate)) {
                return false;
            }
        }
        
        return true;
    }
    
    public List<Room> searchAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, RoomType roomType) {
        List<Room> availableRooms = new ArrayList<>();
        
        for (Room room : rooms.values()) {
            if (roomType != null && room.getRoomType() != roomType) {
                continue;
            }
            
            if (isRoomAvailable(room.getRoomNumber(), checkInDate, checkOutDate)) {
                availableRooms.add(room);
            }
        }
        
        return availableRooms;
    }
    
    public boolean cancelBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("❌ Booking not found");
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.CANCELLED) {
            System.out.println("❌ Booking already cancelled");
            return false;
        }
        
        if (booking.getStatus() == BookingStatus.CHECKED_OUT) {
            System.out.println("❌ Cannot cancel completed booking");
            return false;
        }
        
        // Calculate refund
        double refund = calculateRefund(booking);
        
        // Update status
        booking.setStatus(BookingStatus.CANCELLED);
        booking.setPaymentStatus(PaymentStatus.REFUNDED);
        
        System.out.println("✓ Booking cancelled: " + bookingId);
        System.out.println("  Refund amount: $" + String.format("%.2f", refund));
        
        return true;
    }
    
    private double calculateRefund(Booking booking) {
        long daysUntilCheckIn = java.time.temporal.ChronoUnit.DAYS.between(
            LocalDate.now(), booking.getCheckInDate());
        
        if (daysUntilCheckIn >= 7) {
            return booking.getTotalPrice(); // Full refund
        } else if (daysUntilCheckIn >= 3) {
            return booking.getTotalPrice() * 0.5; // 50% refund
        } else {
            return 0; // No refund
        }
    }
    
    public void checkIn(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("❌ Booking not found");
            return;
        }
        
        if (booking.getStatus() != BookingStatus.CONFIRMED) {
            System.out.println("❌ Booking not in confirmed status");
            return;
        }
        
        booking.setStatus(BookingStatus.CHECKED_IN);
        System.out.println("✓ Checked in: " + booking.getGuest().getName() + 
                         " - Room " + booking.getRoom().getRoomNumber());
    }
    
    public void checkOut(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            System.out.println("❌ Booking not found");
            return;
        }
        
        if (booking.getStatus() != BookingStatus.CHECKED_IN) {
            System.out.println("❌ Booking not checked in");
            return;
        }
        
        booking.setStatus(BookingStatus.CHECKED_OUT);
        System.out.println("✓ Checked out: " + booking.getGuest().getName() + 
                         " - Room " + booking.getRoom().getRoomNumber());
    }
    
    public Booking getBooking(String bookingId) {
        return bookings.get(bookingId);
    }
    
    public List<Booking> getGuestBookings(String guestId) {
        return bookings.values().stream()
            .filter(b -> b.getGuest().getGuestId().equals(guestId))
            .collect(Collectors.toList());
    }
    
    public void displayAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate) {
        System.out.println("\n========== AVAILABLE ROOMS ==========");
        System.out.println("Dates: " + checkInDate + " to " + checkOutDate);
        
        List<Room> available = searchAvailableRooms(checkInDate, checkOutDate, null);
        if (available.isEmpty()) {
            System.out.println("No rooms available");
        } else {
            for (Room room : available) {
                System.out.println("  " + room);
            }
        }
        System.out.println("=====================================\n");
    }
    
    public void displaySystemStats() {
        System.out.println("\n========== SYSTEM STATISTICS ==========");
        System.out.println("Total Rooms: " + rooms.size());
        System.out.println("Total Guests: " + guests.size());
        System.out.println("Total Bookings: " + bookings.size());
        System.out.println("Active Bookings: " + bookings.values().stream()
            .filter(b -> b.getStatus() == BookingStatus.CONFIRMED || 
                        b.getStatus() == BookingStatus.CHECKED_IN)
            .count());
        System.out.println("Cancelled Bookings: " + bookings.values().stream()
            .filter(b -> b.getStatus() == BookingStatus.CANCELLED)
            .count());
        System.out.println("=======================================\n");
    }
}
