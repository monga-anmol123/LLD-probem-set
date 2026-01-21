package service;

import model.Order;
import model.Chef;
import enums.OrderStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Comparator;

public class Kitchen {
    private List<Chef> chefs;
    private PriorityQueue<Order> orderQueue;
    
    public Kitchen() {
        this.chefs = new ArrayList<>();
        // VIP orders get priority
        this.orderQueue = new PriorityQueue<>(new Comparator<Order>() {
            @Override
            public int compare(Order o1, Order o2) {
                if (o1.isVIP() && !o2.isVIP()) {
                    return -1;
                } else if (!o1.isVIP() && o2.isVIP()) {
                    return 1;
                } else {
                    return o1.getOrderTime().compareTo(o2.getOrderTime());
                }
            }
        });
    }
    
    public void addChef(Chef chef) {
        chefs.add(chef);
        System.out.println("Chef " + chef.getName() + " added to kitchen.");
    }
    
    public void receiveOrder(Order order) {
        orderQueue.add(order);
        System.out.println("\n[KITCHEN] Received order " + order.getOrderId() + 
                         (order.isVIP() ? " (VIP - HIGH PRIORITY)" : ""));
        notifyChefs(order);
    }
    
    public void processNextOrder() {
        if (orderQueue.isEmpty()) {
            System.out.println("[KITCHEN] No orders to process.");
            return;
        }
        
        Order order = orderQueue.poll();
        if (!chefs.isEmpty()) {
            Chef chef = chefs.get(0); // Simple assignment to first chef
            chef.prepareOrder(order);
            order.nextState(); // PLACED -> PREPARING
        }
    }
    
    public void completeOrder(Order order) {
        if (!chefs.isEmpty()) {
            Chef chef = chefs.get(0);
            chef.completeOrder(order);
            order.nextState(); // PREPARING -> READY
        }
    }
    
    private void notifyChefs(Order order) {
        for (Chef chef : chefs) {
            chef.update(order, "New order #" + order.getOrderId() + " received in kitchen!");
        }
    }
    
    public int getPendingOrdersCount() {
        return orderQueue.size();
    }
    
    public List<Chef> getChefs() {
        return chefs;
    }
}


