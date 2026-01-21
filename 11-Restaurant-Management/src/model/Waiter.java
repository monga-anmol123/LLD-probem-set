package model;

import enums.StaffRole;
import observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class Waiter extends Staff implements Observer {
    private List<Table> assignedTables;
    private int ordersServed;
    private double tipsEarned;
    
    public Waiter(String staffId, String name, String contactInfo) {
        super(staffId, name, StaffRole.WAITER, contactInfo);
        this.assignedTables = new ArrayList<>();
        this.ordersServed = 0;
        this.tipsEarned = 0.0;
    }
    
    @Override
    public void performDuty() {
        System.out.println("Waiter " + name + " is serving tables.");
    }
    
    @Override
    public void update(Order order, String message) {
        System.out.println("[NOTIFICATION] Waiter " + name + " received: " + message);
    }
    
    public void assignTable(Table table) {
        assignedTables.add(table);
        System.out.println("Table " + table.getTableId() + " assigned to waiter " + name);
    }
    
    public void serveOrder(Order order) {
        ordersServed++;
        System.out.println("Waiter " + name + " served order " + order.getOrderId());
    }
    
    public void receiveTip(double amount) {
        tipsEarned += amount;
        System.out.println("Waiter " + name + " received tip: $" + String.format("%.2f", amount));
    }
    
    // Getters
    public List<Table> getAssignedTables() {
        return assignedTables;
    }
    
    public int getOrdersServed() {
        return ordersServed;
    }
    
    public double getTipsEarned() {
        return tipsEarned;
    }
}


