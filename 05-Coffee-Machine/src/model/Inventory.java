package model;

public class Inventory {
    private int coffeeBeans; // grams
    private int milk; // ml
    private int water; // ml
    private int sugar; // grams
    
    public Inventory(int coffeeBeans, int milk, int water, int sugar) {
        this.coffeeBeans = coffeeBeans;
        this.milk = milk;
        this.water = water;
        this.sugar = sugar;
    }
    
    // Check if ingredients are available for a coffee
    public boolean checkAvailability(Coffee coffee) {
        return coffeeBeans >= coffee.getCoffeeBeansNeeded() &&
               milk >= coffee.getMilkNeeded() &&
               water >= coffee.getWaterNeeded() &&
               sugar >= coffee.getSugarNeeded();
    }
    
    // Deduct ingredients after making coffee
    public void deductIngredients(Coffee coffee) {
        if (!checkAvailability(coffee)) {
            throw new IllegalStateException("Insufficient ingredients");
        }
        
        coffeeBeans -= coffee.getCoffeeBeansNeeded();
        milk -= coffee.getMilkNeeded();
        water -= coffee.getWaterNeeded();
        sugar -= coffee.getSugarNeeded();
    }
    
    // Refill ingredients
    public void refill(int coffeeBeans, int milk, int water, int sugar) {
        this.coffeeBeans += coffeeBeans;
        this.milk += milk;
        this.water += water;
        this.sugar += sugar;
    }
    
    // Display current inventory
    public void displayInventory() {
        System.out.println("=== Current Inventory ===");
        System.out.println("Coffee Beans: " + coffeeBeans + "g");
        System.out.println("Milk: " + milk + "ml");
        System.out.println("Water: " + water + "ml");
        System.out.println("Sugar: " + sugar + "g");
    }
    
    // Getters
    public int getCoffeeBeans() {
        return coffeeBeans;
    }
    
    public int getMilk() {
        return milk;
    }
    
    public int getWater() {
        return water;
    }
    
    public int getSugar() {
        return sugar;
    }
}


