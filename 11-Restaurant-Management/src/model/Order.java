package model;

import enums.OrderStatus;
import observer.Observer;
import observer.Subject;
import state.OrderState;
import state.PlacedState;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order implements Subject {
    private String orderId;
    private Table table;
    private List<MenuItem> items;
    private OrderStatus status;
    private Waiter waiter;
    private LocalDateTime orderTime;
    private double totalAmount;
    private List<Observer> observers;
    private OrderState state;
    private boolean isVIP;
    
    public Order(String orderId, Table table, Waiter waiter) {
        this.orderId = orderId;
        this.table = table;
        this.waiter = waiter;
        this.items = new ArrayList<>();
        this.status = OrderStatus.PLACED;
        this.orderTime = LocalDateTime.now();
        this.totalAmount = 0.0;
        this.observers = new ArrayList<>();
        this.state = new PlacedState();
        this.isVIP = table.getType().name().equals("VIP");
    }
    
    public void addItem(MenuItem item) {
        if (item.isAvailable()) {
            items.add(item);
            System.out.println("Added: " + item.getName() + " to order " + orderId);
        } else {
            System.out.println("Item " + item.getName() + " is not available!");
        }
    }
    
    public void removeItem(MenuItem item) {
        items.remove(item);
        System.out.println("Removed: " + item.getName() + " from order " + orderId);
    }
    
    public void updateStatus(OrderStatus newStatus) {
        this.status = newStatus;
        notifyObservers("Order #" + orderId + " status updated to: " + newStatus);
    }
    
    public void nextState() {
        state.next(this);
        notifyObservers("Order #" + orderId + " moved to: " + state.getStateName());
    }
    
    public void calculateTotal(double subtotal) {
        this.totalAmount = subtotal;
    }
    
    // Observer Pattern Methods
    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }
    
    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }
    
    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(this, message);
        }
    }
    
    // Getters and Setters
    public String getOrderId() {
        return orderId;
    }
    
    public Table getTable() {
        return table;
    }
    
    public List<MenuItem> getItems() {
        return items;
    }
    
    public OrderStatus getStatus() {
        return status;
    }
    
    public void setStatus(OrderStatus status) {
        this.status = status;
    }
    
    public Waiter getWaiter() {
        return waiter;
    }
    
    public LocalDateTime getOrderTime() {
        return orderTime;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public OrderState getState() {
        return state;
    }
    
    public void setState(OrderState state) {
        this.state = state;
    }
    
    public boolean isVIP() {
        return isVIP;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id='" + orderId + '\'' +
                ", table=" + table.getTableId() +
                ", items=" + items.size() +
                ", status=" + status +
                ", total=$" + String.format("%.2f", totalAmount) +
                ", VIP=" + isVIP +
                '}';
    }
}


