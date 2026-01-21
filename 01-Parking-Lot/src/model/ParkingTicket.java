package model;

import java.time.LocalDateTime;
import java.time.Duration;

public class ParkingTicket {
    private String ticketId;
    private Vehicle vehicle;
    private ParkingSpot spot;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    private double fee;
    
    public ParkingTicket(String ticketId, Vehicle vehicle, ParkingSpot spot) {
        this.ticketId = ticketId;
        this.vehicle = vehicle;
        this.spot = spot;
        this.entryTime = LocalDateTime.now();
    }
    
    public void markExit() {
        this.exitTime = LocalDateTime.now();
    }
    
    public long getParkingDurationInMinutes() {
        LocalDateTime end = (exitTime != null) ? exitTime : LocalDateTime.now();
        return Duration.between(entryTime, end).toMinutes();
    }
    
    public String getTicketId() { return ticketId; }
    public Vehicle getVehicle() { return vehicle; }
    public ParkingSpot getSpot() { return spot; }
    public LocalDateTime getEntryTime() { return entryTime; }
    public LocalDateTime getExitTime() { return exitTime; }
    public double getFee() { return fee; }
    public void setFee(double fee) { this.fee = fee; }
}


