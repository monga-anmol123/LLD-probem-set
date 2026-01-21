package model;

import java.util.*;

/**
 * Represents a customer in the food delivery system
 */
public class Customer {
    private String customerId;
    private String name;
    private String phone;
    private String email;
    private List<Address> addresses;
    private Cart cart;
    
    public Customer(String customerId, String name, String phone, String email) {
        this.customerId = customerId;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.addresses = new ArrayList<>();
        this.cart = new Cart();
    }
    
    public String getCustomerId() {
        return customerId;
    }
    
    public String getName() {
        return name;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public String getEmail() {
        return email;
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public void addAddress(Address address) {
        addresses.add(address);
    }
    
    public List<Address> getAddresses() {
        return new ArrayList<>(addresses);
    }
    
    public Address getPrimaryAddress() {
        return addresses.isEmpty() ? null : addresses.get(0);
    }
    
    @Override
    public String toString() {
        return String.format("Customer: %s (ID: %s, Phone: %s)", name, customerId, phone);
    }
}

