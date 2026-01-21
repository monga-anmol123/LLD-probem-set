package model;

import enums.RentalStatus;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Rental {
    private String rentalId;
    private Vehicle vehicle;
    private Customer customer;
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private LocalDate actualReturnDate;
    private String pickupLocation;
    private String returnLocation;
    private double totalCost;
    private RentalStatus status;
    private boolean hasInsurance;
    private boolean hasGPS;
    private double insuranceCostPerDay = 15.0;
    private double gpsCostPerDay = 10.0;
    
    public Rental(String rentalId, Vehicle vehicle, Customer customer, 
                  LocalDate pickupDate, LocalDate returnDate, 
                  String pickupLocation, String returnLocation) {
        this.rentalId = rentalId;
        this.vehicle = vehicle;
        this.customer = customer;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.pickupLocation = pickupLocation;
        this.returnLocation = returnLocation;
        this.status = RentalStatus.ACTIVE;
        this.hasInsurance = false;
        this.hasGPS = false;
    }
    
    public long getRentalDays() {
        LocalDate endDate = actualReturnDate != null ? actualReturnDate : returnDate;
        return ChronoUnit.DAYS.between(pickupDate, endDate);
    }
    
    public long getPlannedDays() {
        return ChronoUnit.DAYS.between(pickupDate, returnDate);
    }
    
    public boolean isOneWayRental() {
        return !pickupLocation.equals(returnLocation);
    }
    
    public long getLateDays() {
        if (actualReturnDate == null || !actualReturnDate.isAfter(returnDate)) {
            return 0;
        }
        return ChronoUnit.DAYS.between(returnDate, actualReturnDate);
    }
    
    public double calculateBaseCost() {
        long days = getRentalDays();
        return days * vehicle.getDailyRate();
    }
    
    public double calculateInsuranceCost() {
        if (!hasInsurance) return 0.0;
        return getRentalDays() * insuranceCostPerDay;
    }
    
    public double calculateAddOnsCost() {
        double cost = 0.0;
        if (hasGPS) {
            cost += getRentalDays() * gpsCostPerDay;
        }
        return cost;
    }
    
    public double calculateLateFee() {
        long lateDays = getLateDays();
        if (lateDays > 0) {
            return lateDays * 30.0; // $30 per late day
        }
        return 0.0;
    }
    
    public double calculateOneWayFee() {
        return isOneWayRental() ? 75.0 : 0.0;
    }
    
    public void addInsurance() {
        this.hasInsurance = true;
    }
    
    public void addGPS() {
        this.hasGPS = true;
    }
    
    public void completeRental(LocalDate actualReturnDate) {
        this.actualReturnDate = actualReturnDate;
        this.status = RentalStatus.COMPLETED;
    }
    
    public void extendRental(LocalDate newReturnDate) {
        if (newReturnDate.isAfter(this.returnDate)) {
            this.returnDate = newReturnDate;
        }
    }
    
    // Getters and Setters
    public String getRentalId() { return rentalId; }
    public Vehicle getVehicle() { return vehicle; }
    public Customer getCustomer() { return customer; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public LocalDate getActualReturnDate() { return actualReturnDate; }
    public String getPickupLocation() { return pickupLocation; }
    public String getReturnLocation() { return returnLocation; }
    public double getTotalCost() { return totalCost; }
    public void setTotalCost(double totalCost) { this.totalCost = totalCost; }
    public RentalStatus getStatus() { return status; }
    public void setStatus(RentalStatus status) { this.status = status; }
    public boolean hasInsurance() { return hasInsurance; }
    public boolean hasGPS() { return hasGPS; }
    
    @Override
    public String toString() {
        return String.format("Rental %s: %s rented by %s (%s to %s)", 
            rentalId, vehicle.getMake() + " " + vehicle.getModel(), 
            customer.getName(), pickupDate, returnDate);
    }
}


