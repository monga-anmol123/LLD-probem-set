package model;

import enums.OrderStatus;
import enums.PaymentMethod;
import state.OrderState;
import state.PlacedState;
import observer.OrderObserver;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Represents an order in the food delivery system
 * Uses State Pattern for order state management
 * Uses Observer Pattern for notifications
 */
public class Order {
    private static int orderCounter = 1;
    
    private String orderId;
    private Customer customer;
    private Restaurant restaurant;
    private Map<MenuItem, Integer> items;
    private double subtotal;
    private double deliveryCharge;
    private double tax;
    private double totalAmount;
    private Address deliveryAddress;
    private PaymentMethod paymentMethod;
    private LocalDateTime orderTime;
    private OrderState currentState;
    private OrderStatus status;
    private DeliveryPartner deliveryPartner;
    private List<OrderObserver> observers;
    
    public Order(Customer customer, Restaurant restaurant, Map<MenuItem, Integer> items,
                 Address deliveryAddress, PaymentMethod paymentMethod) {
        this.orderId = "ORD" + String.format("%06d", orderCounter++);
        this.customer = customer;
        this.restaurant = restaurant;
        this.items = new HashMap<>(items);
        this.deliveryAddress = deliveryAddress;
        this.paymentMethod = paymentMethod;
        this.orderTime = LocalDateTime.now();
        this.currentState = new PlacedState();
        this.status = OrderStatus.PLACED;
        this.observers = new ArrayList<>();
        
        calculateAmounts();
    }
    
    private void calculateAmounts() {
        this.subtotal = items.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getPrice() * entry.getValue())
            .sum();
        this.tax = subtotal * 0.05; // 5% tax
        // Delivery charge will be set by pricing strategy
    }
    
    public String getOrderId() {
        return orderId;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public Restaurant getRestaurant() {
        return restaurant;
    }
    
    public Map<MenuItem, Integer> getItems() {
        return new HashMap<>(items);
    }
    
    public double getSubtotal() {
        return subtotal;
    }
    
    public double getDeliveryCharge() {
        return deliveryCharge;
    }
    
    public void setDeliveryCharge(double deliveryCharge) {
        this.deliveryCharge = deliveryCharge;
        this.totalAmount = subtotal + tax + deliveryCharge;
    }
    
    public double getTax() {
        return tax;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public Address getDeliveryAddress() {
        return deliveryAddress;
    }
    
    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public OrderState getCurrentState() {
        return currentState;
    }
    
    public void setState(OrderState state) {
        this.currentState = state;
    }
    
    public DeliveryPartner getDeliveryPartner() {
        return deliveryPartner;
    }
    
    public void assignDeliveryPartner(DeliveryPartner partner) {
        this.deliveryPartner = partner;
    }
    
    // Observer Pattern methods
    public void addObserver(OrderObserver observer) {
        observers.add(observer);
    }
    
    public void removeObserver(OrderObserver observer) {
        observers.remove(observer);
    }
    
    public void notifyObservers() {
        for (OrderObserver observer : observers) {
            observer.onOrderUpdate(this);
        }
    }
    
    // State Pattern methods - delegate to current state
    public void prepare() {
        currentState.prepare(this);
    }
    
    public void readyForPickup() {
        currentState.readyForPickup(this);
    }
    
    public void outForDelivery() {
        currentState.outForDelivery(this);
    }
    
    public void deliver() {
        currentState.deliver(this);
    }
    
    public void cancel() {
        currentState.cancel(this);
    }
    
    public void displayOrderSummary() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        System.out.println("\n╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║                       ORDER SUMMARY                            ║");
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Order ID          : %-42s ║%n", orderId);
        System.out.printf("║ Status            : %-42s ║%n", status);
        System.out.printf("║ Order Time        : %-42s ║%n", orderTime.format(formatter));
        System.out.printf("║ Customer          : %-42s ║%n", customer.getName());
        System.out.printf("║ Restaurant        : %-42s ║%n", restaurant.getName());
        System.out.printf("║ Delivery Address  : %-42s ║%n", deliveryAddress.toString());
        System.out.printf("║ Payment Method    : %-42s ║%n", paymentMethod);
        
        if (deliveryPartner != null) {
            System.out.printf("║ Delivery Partner  : %-42s ║%n", deliveryPartner.getName());
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.println("║ ITEMS:                                                         ║");
        
        for (Map.Entry<MenuItem, Integer> entry : items.entrySet()) {
            MenuItem item = entry.getKey();
            int quantity = entry.getValue();
            double itemTotal = item.getPrice() * quantity;
            System.out.printf("║   %-40s x%-3d $%-10.2f ║%n", 
                item.getName(), quantity, itemTotal);
        }
        
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ Subtotal          : %43s $%.2f ║%n", "", subtotal);
        System.out.printf("║ Tax (5%%)          : %43s $%.2f ║%n", "", tax);
        System.out.printf("║ Delivery Charge   : %43s $%.2f ║%n", "", deliveryCharge);
        System.out.println("╠════════════════════════════════════════════════════════════════╣");
        System.out.printf("║ TOTAL AMOUNT      : %43s $%.2f ║%n", "", totalAmount);
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
    }
    
    @Override
    public String toString() {
        return String.format("Order %s - %s - %s - $%.2f", 
            orderId, restaurant.getName(), status, totalAmount);
    }
}

