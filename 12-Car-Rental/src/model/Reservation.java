package model;

import enums.ReservationStatus;
import enums.VehicleType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class Reservation {
    private String reservationId;
    private Customer customer;
    private VehicleType vehicleType;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private ReservationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiryTime;
    private Vehicle assignedVehicle;
    
    public Reservation(String reservationId, Customer customer, VehicleType vehicleType,
                      LocalDate pickupDate, LocalDate returnDate) {
        this.reservationId = reservationId;
        this.customer = customer;
        this.vehicleType = vehicleType;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.status = ReservationStatus.ACTIVE;
        this.createdAt = LocalDateTime.now();
        this.expiryTime = LocalDateTime.now().plusHours(24); // 24 hour expiry
    }
    
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryTime) && status == ReservationStatus.ACTIVE;
    }
    
    public void confirm(Vehicle vehicle) {
        this.assignedVehicle = vehicle;
        this.status = ReservationStatus.CONFIRMED;
    }
    
    public void cancel() {
        this.status = ReservationStatus.CANCELLED;
    }
    
    public void fulfill() {
        this.status = ReservationStatus.FULFILLED;
    }
    
    public void expire() {
        this.status = ReservationStatus.EXPIRED;
    }
    
    // Getters
    public String getReservationId() { return reservationId; }
    public Customer getCustomer() { return customer; }
    public VehicleType getVehicleType() { return vehicleType; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public ReservationStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getExpiryTime() { return expiryTime; }
    public Vehicle getAssignedVehicle() { return assignedVehicle; }
    
    @Override
    public String toString() {
        return String.format("Reservation %s: %s for %s (%s to %s) - Status: %s", 
            reservationId, vehicleType, customer.getName(), pickupDate, returnDate, status);
    }
}


