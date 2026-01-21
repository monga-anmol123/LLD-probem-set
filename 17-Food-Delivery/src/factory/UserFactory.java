package factory;

import model.*;

/**
 * Factory class for creating different types of users
 * Implements Factory Design Pattern
 */
public class UserFactory {
    private static int customerCounter = 1;
    private static int partnerCounter = 1;
    
    public static Customer createCustomer(String name, String phone, String email) {
        String customerId = "CUST" + String.format("%04d", customerCounter++);
        return new Customer(customerId, name, phone, email);
    }
    
    public static DeliveryPartner createDeliveryPartner(String name, String phone, Address location) {
        String partnerId = "DP" + String.format("%04d", partnerCounter++);
        return new DeliveryPartner(partnerId, name, phone, location);
    }
}

