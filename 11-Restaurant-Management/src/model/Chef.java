package model;

import enums.StaffRole;
import observer.Observer;

public class Chef extends Staff implements Observer {
    private String specialty;
    private int dishesCooked;
    
    public Chef(String staffId, String name, String contactInfo, String specialty) {
        super(staffId, name, StaffRole.CHEF, contactInfo);
        this.specialty = specialty;
        this.dishesCooked = 0;
    }
    
    @Override
    public void performDuty() {
        System.out.println("Chef " + name + " is preparing dishes.");
    }
    
    @Override
    public void update(Order order, String message) {
        System.out.println("[KITCHEN] Chef " + name + " received: " + message);
    }
    
    public void prepareOrder(Order order) {
        System.out.println("Chef " + name + " is preparing order " + order.getOrderId());
        dishesCooked += order.getItems().size();
    }
    
    public void completeOrder(Order order) {
        System.out.println("Chef " + name + " completed order " + order.getOrderId());
    }
    
    // Getters
    public String getSpecialty() {
        return specialty;
    }
    
    public int getDishesCooked() {
        return dishesCooked;
    }
}


