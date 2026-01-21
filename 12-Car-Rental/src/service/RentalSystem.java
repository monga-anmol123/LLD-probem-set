package service;

import model.*;
import enums.*;
import factory.VehicleFactory;
import strategy.PricingStrategy;
import strategy.StandardPricingStrategy;
import observer.Subject;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class RentalSystem {
    private static RentalSystem instance;
    private String systemName;
    private Map<String, Vehicle> vehicles;
    private Map<String, Customer> customers;
    private Map<String, Rental> rentals;
    private Map<String, Reservation> reservations;
    private Map<String, Location> locations;
    private PricingStrategy pricingStrategy;
    private int rentalIdCounter = 1;
    private int reservationIdCounter = 1;
    private int invoiceIdCounter = 1;
    private Subject notificationSubject;
    
    private RentalSystem(String systemName) {
        this.systemName = systemName;
        this.vehicles = new HashMap<>();
        this.customers = new HashMap<>();
        this.rentals = new HashMap<>();
        this.reservations = new HashMap<>();
        this.locations = new HashMap<>();
        this.pricingStrategy = new StandardPricingStrategy();
        this.notificationSubject = new Subject();
    }
    
    public static synchronized RentalSystem getInstance(String systemName) {
        if (instance == null) {
            instance = new RentalSystem(systemName);
        }
        return instance;
    }
    
    public static RentalSystem getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RentalSystem not initialized. Call getInstance(String) first.");
        }
        return instance;
    }
    
    // Location Management
    public void addLocation(Location location) {
        locations.put(location.getLocationId(), location);
        System.out.println("✓ Added location: " + location.getName());
    }
    
    // Vehicle Management
    public void addVehicle(Vehicle vehicle, String locationId) {
        vehicles.put(vehicle.getVin(), vehicle);
        Location location = locations.get(locationId);
        if (location != null) {
            location.addVehicle(vehicle);
        }
        System.out.println("✓ Added vehicle: " + vehicle);
    }
    
    public Vehicle getVehicle(String vin) {
        return vehicles.get(vin);
    }
    
    // Customer Management
    public void registerCustomer(Customer customer) {
        customers.put(customer.getCustomerId(), customer);
        notificationSubject.attach(customer);
        System.out.println("✓ Registered customer: " + customer);
    }
    
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    
    // Search Vehicles
    public List<Vehicle> searchAvailableVehicles(VehicleType type) {
        return vehicles.values().stream()
            .filter(v -> v.isAvailable() && v.getType() == type)
            .collect(Collectors.toList());
    }
    
    public List<Vehicle> searchAvailableVehicles(VehicleType type, String locationId) {
        Location location = locations.get(locationId);
        if (location != null) {
            return location.getAvailableVehiclesByType(type);
        }
        return new ArrayList<>();
    }
    
    // Reservation Management
    public Reservation createReservation(String customerId, VehicleType vehicleType,
                                        LocalDate pickupDate, LocalDate returnDate) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        if (!customer.canRent()) {
            throw new IllegalStateException("Customer has outstanding dues: $" + customer.getOutstandingDues());
        }
        
        String reservationId = "RES-" + String.format("%04d", reservationIdCounter++);
        Reservation reservation = new Reservation(reservationId, customer, vehicleType, pickupDate, returnDate);
        reservations.put(reservationId, reservation);
        
        System.out.println("✓ Created reservation: " + reservation);
        notificationSubject.notifyObservers("Reservation " + reservationId + " created for " + vehicleType);
        
        return reservation;
    }
    
    public void cancelReservation(String reservationId) {
        Reservation reservation = reservations.get(reservationId);
        if (reservation != null) {
            reservation.cancel();
            System.out.println("✓ Cancelled reservation: " + reservationId);
        }
    }
    
    // Rental Management
    public Rental createRental(String customerId, String vin, 
                              LocalDate pickupDate, LocalDate returnDate,
                              String pickupLocation, String returnLocation) {
        Customer customer = customers.get(customerId);
        if (customer == null) {
            throw new IllegalArgumentException("Customer not found: " + customerId);
        }
        
        if (!customer.canRent()) {
            throw new IllegalStateException("Customer has outstanding dues: $" + customer.getOutstandingDues());
        }
        
        Vehicle vehicle = vehicles.get(vin);
        if (vehicle == null) {
            throw new IllegalArgumentException("Vehicle not found: " + vin);
        }
        
        if (!vehicle.isAvailable()) {
            throw new IllegalStateException("Vehicle is not available: " + vehicle.getStatus());
        }
        
        // Create rental
        String rentalId = "RNT-" + String.format("%04d", rentalIdCounter++);
        Rental rental = new Rental(rentalId, vehicle, customer, pickupDate, returnDate, 
                                   pickupLocation, returnLocation);
        
        // Update vehicle status
        vehicle.rent();
        
        // Store rental
        rentals.put(rentalId, rental);
        customer.addRental(rental);
        
        System.out.println("✓ Created rental: " + rental);
        
        return rental;
    }
    
    public Invoice returnVehicle(String rentalId, LocalDate actualReturnDate) {
        Rental rental = rentals.get(rentalId);
        if (rental == null) {
            throw new IllegalArgumentException("Rental not found: " + rentalId);
        }
        
        if (rental.getStatus() != RentalStatus.ACTIVE) {
            throw new IllegalStateException("Rental is not active: " + rental.getStatus());
        }
        
        // Complete rental
        rental.completeRental(actualReturnDate);
        
        // Calculate total cost using pricing strategy
        double totalCost = pricingStrategy.calculatePrice(rental);
        rental.setTotalCost(totalCost);
        
        // Generate invoice
        String invoiceId = "INV-" + String.format("%04d", invoiceIdCounter++);
        Invoice invoice = new Invoice(invoiceId, rental);
        
        // Return vehicle to available status
        rental.getVehicle().returnVehicle();
        
        // If one-way rental, update vehicle location
        if (rental.isOneWayRental()) {
            Location oldLocation = locations.get(rental.getPickupLocation());
            Location newLocation = locations.get(rental.getReturnLocation());
            if (oldLocation != null && newLocation != null) {
                oldLocation.removeVehicle(rental.getVehicle());
                newLocation.addVehicle(rental.getVehicle());
            }
        }
        
        System.out.println("✓ Vehicle returned. Rental completed: " + rentalId);
        
        // Check for waiting reservations
        checkAndNotifyReservations(rental.getVehicle().getType());
        
        return invoice;
    }
    
    private void checkAndNotifyReservations(VehicleType type) {
        List<Reservation> activeReservations = reservations.values().stream()
            .filter(r -> r.getStatus() == ReservationStatus.ACTIVE && r.getVehicleType() == type)
            .collect(Collectors.toList());
        
        if (!activeReservations.isEmpty()) {
            Reservation reservation = activeReservations.get(0);
            notificationSubject.notifyObservers(
                "Your reserved " + type + " vehicle is now available! (Reservation: " + 
                reservation.getReservationId() + ")"
            );
        }
    }
    
    // Pricing Strategy Management
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        System.out.println("✓ Pricing strategy changed to: " + strategy.getStrategyName());
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    
    // Display Methods
    public void displayAvailableVehicles() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("              AVAILABLE VEHICLES");
        System.out.println("═══════════════════════════════════════════════════════");
        
        Map<VehicleType, List<Vehicle>> vehiclesByType = vehicles.values().stream()
            .filter(Vehicle::isAvailable)
            .collect(Collectors.groupingBy(Vehicle::getType));
        
        for (VehicleType type : VehicleType.values()) {
            List<Vehicle> typeVehicles = vehiclesByType.getOrDefault(type, new ArrayList<>());
            System.out.println("\n" + type + " (" + typeVehicles.size() + " available):");
            for (Vehicle v : typeVehicles) {
                System.out.println("  - " + v);
            }
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
    
    public void displayActiveRentals() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("              ACTIVE RENTALS");
        System.out.println("═══════════════════════════════════════════════════════");
        
        List<Rental> activeRentals = rentals.values().stream()
            .filter(r -> r.getStatus() == RentalStatus.ACTIVE)
            .collect(Collectors.toList());
        
        if (activeRentals.isEmpty()) {
            System.out.println("No active rentals.");
        } else {
            for (Rental rental : activeRentals) {
                System.out.println(rental);
            }
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
    
    public void displayLocations() {
        System.out.println("\n═══════════════════════════════════════════════════════");
        System.out.println("              RENTAL LOCATIONS");
        System.out.println("═══════════════════════════════════════════════════════");
        
        for (Location location : locations.values()) {
            System.out.println(location);
        }
        System.out.println("═══════════════════════════════════════════════════════\n");
    }
    
    // Getters
    public String getSystemName() { return systemName; }
    public Map<String, Vehicle> getVehicles() { return vehicles; }
    public Map<String, Customer> getCustomers() { return customers; }
    public Map<String, Rental> getRentals() { return rentals; }
    public Map<String, Reservation> getReservations() { return reservations; }
}


