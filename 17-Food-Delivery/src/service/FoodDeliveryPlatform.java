package service;

import model.*;
import enums.*;
import strategy.DeliveryPricingStrategy;
import strategy.DistanceBasedPricing;
import observer.*;
import factory.UserFactory;

import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Main service class for Food Delivery Platform
 * Implements Singleton Pattern
 */
public class FoodDeliveryPlatform {
    private static FoodDeliveryPlatform instance;
    
    private String platformName;
    private Map<String, Restaurant> restaurants;
    private Map<String, Customer> customers;
    private Map<String, DeliveryPartner> deliveryPartners;
    private Map<String, Order> orders;
    private DeliveryPricingStrategy pricingStrategy;
    
    // Observers
    private OrderObserver customerNotifier;
    private OrderObserver restaurantNotifier;
    private OrderObserver deliveryPartnerNotifier;
    
    private FoodDeliveryPlatform(String platformName) {
        this.platformName = platformName;
        this.restaurants = new HashMap<>();
        this.customers = new HashMap<>();
        this.deliveryPartners = new HashMap<>();
        this.orders = new HashMap<>();
        this.pricingStrategy = new DistanceBasedPricing(); // Default strategy
        
        // Initialize observers
        this.customerNotifier = new CustomerNotifier();
        this.restaurantNotifier = new RestaurantNotifier();
        this.deliveryPartnerNotifier = new DeliveryPartnerNotifier();
    }
    
    public static synchronized FoodDeliveryPlatform getInstance(String platformName) {
        if (instance == null) {
            instance = new FoodDeliveryPlatform(platformName);
        }
        return instance;
    }
    
    public String getPlatformName() {
        return platformName;
    }
    
    // Strategy Pattern - Set pricing strategy
    public void setPricingStrategy(DeliveryPricingStrategy strategy) {
        this.pricingStrategy = strategy;
        System.out.println("✓ Pricing strategy updated: " + strategy.getStrategyName());
    }
    
    public DeliveryPricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }
    
    // Restaurant Management
    public void addRestaurant(Restaurant restaurant) {
        restaurants.put(restaurant.getRestaurantId(), restaurant);
        System.out.println("✓ Restaurant added: " + restaurant.getName());
    }
    
    public Restaurant getRestaurant(String restaurantId) {
        return restaurants.get(restaurantId);
    }
    
    public List<Restaurant> searchRestaurants(CuisineType cuisine, double minRating) {
        return restaurants.values().stream()
            .filter(r -> r.getCuisineType() == cuisine)
            .filter(r -> r.getRating() >= minRating)
            .filter(r -> r.isOpen())
            .collect(Collectors.toList());
    }
    
    public List<Restaurant> getAllRestaurants() {
        return new ArrayList<>(restaurants.values());
    }
    
    // Customer Management
    public Customer registerCustomer(String name, String phone, String email) {
        Customer customer = UserFactory.createCustomer(name, phone, email);
        customers.put(customer.getCustomerId(), customer);
        System.out.println("✓ Customer registered: " + customer.getName() + " (ID: " + customer.getCustomerId() + ")");
        return customer;
    }
    
    public Customer getCustomer(String customerId) {
        return customers.get(customerId);
    }
    
    // Delivery Partner Management
    public DeliveryPartner registerDeliveryPartner(String name, String phone, Address location) {
        DeliveryPartner partner = UserFactory.createDeliveryPartner(name, phone, location);
        deliveryPartners.put(partner.getPartnerId(), partner);
        System.out.println("✓ Delivery partner registered: " + partner.getName() + " (ID: " + partner.getPartnerId() + ")");
        return partner;
    }
    
    public DeliveryPartner getDeliveryPartner(String partnerId) {
        return deliveryPartners.get(partnerId);
    }
    
    public List<DeliveryPartner> getAvailableDeliveryPartners() {
        return deliveryPartners.values().stream()
            .filter(DeliveryPartner::isAvailable)
            .collect(Collectors.toList());
    }
    
    // Order Management
    public Order placeOrder(Customer customer, Restaurant restaurant, Address deliveryAddress, 
                           PaymentMethod paymentMethod) {
        // Validate
        if (customer.getCart().isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }
        
        if (!restaurant.isOpen()) {
            throw new IllegalStateException("Restaurant is closed");
        }
        
        // Create order
        Order order = new Order(customer, restaurant, customer.getCart().getItems(), 
                               deliveryAddress, paymentMethod);
        
        // Calculate delivery charge using strategy pattern
        double distance = restaurant.getLocation().distanceTo(deliveryAddress);
        double deliveryCharge = pricingStrategy.calculateDeliveryCharge(distance, LocalTime.now());
        order.setDeliveryCharge(deliveryCharge);
        
        // Add observers (Observer Pattern)
        order.addObserver(customerNotifier);
        order.addObserver(restaurantNotifier);
        order.addObserver(deliveryPartnerNotifier);
        
        // Store order
        orders.put(order.getOrderId(), order);
        
        // Clear cart
        customer.getCart().clear();
        
        System.out.println("\n✓ Order placed successfully!");
        System.out.println("  Order ID: " + order.getOrderId());
        System.out.println("  Total Amount: $" + order.getTotalAmount());
        System.out.println("  Distance: " + String.format("%.2f", distance) + " km");
        System.out.println("  Delivery Charge: $" + deliveryCharge);
        
        // Notify observers
        order.notifyObservers();
        
        return order;
    }
    
    public Order getOrder(String orderId) {
        return orders.get(orderId);
    }
    
    public List<Order> getAllOrders() {
        return new ArrayList<>(orders.values());
    }
    
    // Delivery Partner Assignment
    public void assignDeliveryPartner(Order order) {
        if (order.getDeliveryPartner() != null) {
            System.out.println("❌ Delivery partner already assigned");
            return;
        }
        
        // Find nearest available delivery partner
        DeliveryPartner nearestPartner = findNearestDeliveryPartner(order.getRestaurant().getLocation());
        
        if (nearestPartner == null) {
            System.out.println("❌ No delivery partners available");
            return;
        }
        
        order.assignDeliveryPartner(nearestPartner);
        nearestPartner.acceptOrder();
        
        System.out.println("✓ Delivery partner assigned: " + nearestPartner.getName());
    }
    
    private DeliveryPartner findNearestDeliveryPartner(Address restaurantLocation) {
        return deliveryPartners.values().stream()
            .filter(DeliveryPartner::isAvailable)
            .min(Comparator.comparingDouble(p -> 
                p.getCurrentLocation().distanceTo(restaurantLocation)))
            .orElse(null);
    }
    
    // Display Methods
    public void displayAllRestaurants() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  ALL RESTAURANTS ON " + platformName.toUpperCase());
        System.out.println(repeat("=", 70));
        
        if (restaurants.isEmpty()) {
            System.out.println("No restaurants available");
        } else {
            for (Restaurant restaurant : restaurants.values()) {
                System.out.println("  " + restaurant);
            }
        }
        
        System.out.println(repeat("=", 70));
    }
    
    public void displayAvailableDeliveryPartners() {
        System.out.println("\n" + repeat("=", 70));
        System.out.println("  AVAILABLE DELIVERY PARTNERS");
        System.out.println(repeat("=", 70));
        
        List<DeliveryPartner> available = getAvailableDeliveryPartners();
        
        if (available.isEmpty()) {
            System.out.println("No delivery partners available");
        } else {
            for (DeliveryPartner partner : available) {
                System.out.println("  " + partner);
            }
        }
        
        System.out.println(repeat("=", 70));
    }
    
    // Helper method for Java 8 compatibility
    private static String repeat(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

