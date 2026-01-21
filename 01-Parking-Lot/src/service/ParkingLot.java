package service;

import model.*;
import strategy.PricingStrategy;
import strategy.HourlyPricingStrategy;
import enums.SpotType;
import java.util.*;

public class ParkingLot {
    private static ParkingLot instance;
    private String name;
    private List<ParkingFloor> floors;
    private Map<String, ParkingTicket> activeTickets;
    private PricingStrategy pricingStrategy;
    private int ticketCounter;
    
    private ParkingLot(String name) {
        this.name = name;
        this.floors = new ArrayList<>();
        this.activeTickets = new HashMap<>();
        this.pricingStrategy = new HourlyPricingStrategy();
        this.ticketCounter = 1;
    }
    
    public static synchronized ParkingLot getInstance(String name) {
        if (instance == null) {
            instance = new ParkingLot(name);
        }
        return instance;
    }
    
    public void addFloor(ParkingFloor floor) {
        floors.add(floor);
    }
    
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
    }
    
    public ParkingTicket parkVehicle(Vehicle vehicle) {
        for (ParkingFloor floor : floors) {
            ParkingSpot spot = floor.findAvailableSpot(vehicle);
            if (spot != null) {
                spot.parkVehicle(vehicle);
                String ticketId = "TICKET-" + ticketCounter++;
                ParkingTicket ticket = new ParkingTicket(ticketId, vehicle, spot);
                activeTickets.put(ticketId, ticket);
                System.out.println("Vehicle " + vehicle.getLicensePlate() + 
                                   " parked at floor " + spot.getFloor() + 
                                   ", spot " + spot.getSpotId());
                return ticket;
            }
        }
        throw new RuntimeException("No available spots for vehicle type: " + vehicle.getType());
    }
    
    public double unparkVehicle(String ticketId, Payment payment) {
        ParkingTicket ticket = activeTickets.get(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Invalid ticket ID: " + ticketId);
        }
        
        ticket.markExit();
        double fee = pricingStrategy.calculateFee(ticket);
        ticket.setFee(fee);
        
        if (payment.processPayment(fee)) {
            ParkingSpot spot = ticket.getSpot();
            spot.removeVehicle();
            activeTickets.remove(ticketId);
            System.out.println("Vehicle " + ticket.getVehicle().getLicensePlate() + 
                               " exited. Fee: $" + fee);
            return fee;
        } else {
            throw new RuntimeException("Payment failed");
        }
    }
    
    public void displayAvailability() {
        System.out.println("\n=== Parking Lot Availability ===");
        for (ParkingFloor floor : floors) {
            System.out.println("Floor " + floor.getFloorNumber() + ":");
            for (SpotType type : SpotType.values()) {
                int available = floor.getAvailableSpotCount(type);
                System.out.println("  " + type + ": " + available + " spots");
            }
        }
    }
}


