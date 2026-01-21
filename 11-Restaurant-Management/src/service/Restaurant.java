package service;

import model.*;
import enums.*;
import strategy.PricingStrategy;
import strategy.RegularPricingStrategy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Restaurant {
    private static Restaurant instance;
    private String name;
    private List<Table> tables;
    private Map<String, MenuItem> menu;
    private List<Order> activeOrders;
    private List<Reservation> reservations;
    private PricingStrategy pricingStrategy;
    private Kitchen kitchen;
    private int orderCounter;
    private int reservationCounter;
    private int billCounter;
    
    private Restaurant(String name) {
        this.name = name;
        this.tables = new ArrayList<>();
        this.menu = new HashMap<>();
        this.activeOrders = new ArrayList<>();
        this.reservations = new ArrayList<>();
        this.pricingStrategy = new RegularPricingStrategy();
        this.kitchen = new Kitchen();
        this.orderCounter = 1;
        this.reservationCounter = 1;
        this.billCounter = 1;
    }
    
    public static synchronized Restaurant getInstance(String name) {
        if (instance == null) {
            instance = new Restaurant(name);
        }
        return instance;
    }
    
    // Table Management
    public void addTable(Table table) {
        tables.add(table);
    }
    
    public Table findAvailableTable(int partySize) {
        // Try exact match first
        for (Table table : tables) {
            if (table.getCapacity() == partySize && table.isAvailable()) {
                return table;
            }
        }
        
        // Find smallest table that fits
        Table bestFit = null;
        for (Table table : tables) {
            if (table.getCapacity() >= partySize && table.isAvailable()) {
                if (bestFit == null || table.getCapacity() < bestFit.getCapacity()) {
                    bestFit = table;
                }
            }
        }
        return bestFit;
    }
    
    public void displayAvailability() {
        System.out.println("\n========================================");
        System.out.println("  TABLE AVAILABILITY");
        System.out.println("========================================");
        Map<TableType, Integer> availableCount = new HashMap<>();
        Map<TableType, Integer> totalCount = new HashMap<>();
        
        for (TableType type : TableType.values()) {
            availableCount.put(type, 0);
            totalCount.put(type, 0);
        }
        
        for (Table table : tables) {
            TableType type = table.getType();
            totalCount.put(type, totalCount.get(type) + 1);
            if (table.isAvailable()) {
                availableCount.put(type, availableCount.get(type) + 1);
            }
        }
        
        for (TableType type : TableType.values()) {
            System.out.println(type + " (" + type.getCapacity() + " seats): " + 
                             availableCount.get(type) + "/" + totalCount.get(type) + " available");
        }
        System.out.println("========================================\n");
    }
    
    // Reservation Management
    public Reservation createReservation(String customerName, String phone, 
                                        int partySize, LocalDateTime dateTime) {
        Table table = findAvailableTable(partySize);
        
        if (table == null) {
            System.out.println("No available tables for party of " + partySize + 
                             ". Please try another time.");
            return null;
        }
        
        String resId = "RES-" + String.format("%03d", reservationCounter++);
        Reservation reservation = new Reservation(resId, customerName, phone, partySize, dateTime);
        reservation.confirm(table);
        table.reserve(reservation);
        reservations.add(reservation);
        
        System.out.println("\n✓ Reservation created: " + reservation);
        return reservation;
    }
    
    public void seatReservation(Reservation reservation) {
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.seat();
            reservation.getAssignedTable().occupy(reservation);
            System.out.println("✓ Reservation " + reservation.getReservationId() + " seated.");
        }
    }
    
    // Menu Management
    public void addMenuItem(MenuItem item) {
        menu.put(item.getItemId(), item);
    }
    
    public MenuItem getMenuItem(String itemId) {
        return menu.get(itemId);
    }
    
    public void displayMenu() {
        System.out.println("\n========================================");
        System.out.println("  MENU");
        System.out.println("========================================");
        for (MenuCategory category : MenuCategory.values()) {
            System.out.println("\n" + category + ":");
            for (MenuItem item : menu.values()) {
                if (item.getCategory() == category && item.isAvailable()) {
                    System.out.println("  " + item.getItemId() + ". " + item.getName() + 
                                     " - $" + String.format("%.2f", item.getPrice()));
                }
            }
        }
        System.out.println("========================================\n");
    }
    
    // Order Management
    public Order placeOrder(Table table, Waiter waiter, List<String> itemIds) {
        String orderId = "ORD-" + String.format("%03d", orderCounter++);
        Order order = new Order(orderId, table, waiter);
        
        // Attach waiter as observer
        order.attach(waiter);
        
        // Add items to order
        for (String itemId : itemIds) {
            MenuItem item = menu.get(itemId);
            if (item != null) {
                order.addItem(item);
            }
        }
        
        // Calculate total using pricing strategy
        double subtotal = pricingStrategy.calculateTotal(order.getItems());
        order.calculateTotal(subtotal);
        
        activeOrders.add(order);
        
        // Send to kitchen
        kitchen.receiveOrder(order);
        
        System.out.println("\n✓ Order placed: " + order);
        System.out.println("  Using: " + pricingStrategy.getStrategyName());
        System.out.println("  Subtotal: $" + String.format("%.2f", subtotal));
        
        return order;
    }
    
    public void processOrderInKitchen(Order order) {
        kitchen.processNextOrder();
    }
    
    public void completeOrderInKitchen(Order order) {
        kitchen.completeOrder(order);
    }
    
    public void serveOrder(Order order) {
        order.nextState(); // READY -> SERVED
        order.getWaiter().serveOrder(order);
    }
    
    // Billing
    public Bill generateBill(Order order) {
        String billId = "BILL-" + String.format("%03d", billCounter++);
        Bill bill = new Bill(billId, order, order.getTotalAmount());
        order.updateStatus(OrderStatus.PAID);
        return bill;
    }
    
    public void releaseTable(Table table) {
        table.release();
        System.out.println("✓ Table " + table.getTableId() + " released and marked for cleaning.");
    }
    
    // Strategy Management
    public void setPricingStrategy(PricingStrategy strategy) {
        this.pricingStrategy = strategy;
        System.out.println("\n✓ Pricing strategy changed to: " + strategy.getStrategyName());
    }
    
    // Kitchen Management
    public Kitchen getKitchen() {
        return kitchen;
    }
    
    // Getters
    public String getName() {
        return name;
    }
    
    public List<Table> getTables() {
        return tables;
    }
    
    public List<Order> getActiveOrders() {
        return activeOrders;
    }
    
    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
}


