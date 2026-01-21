package enums;

public enum CoffeeType {
    ESPRESSO(18, 0, 30, 3.50, "Strong and concentrated coffee"),
    LATTE(18, 150, 30, 4.50, "Espresso with steamed milk"),
    CAPPUCCINO(18, 100, 30, 4.25, "Espresso with foamed milk"),
    AMERICANO(18, 0, 150, 3.75, "Espresso diluted with hot water");
    
    private final int coffeeBeans; // grams
    private final int milk; // ml
    private final int water; // ml
    private final double basePrice; // dollars
    private final String description;
    
    CoffeeType(int coffeeBeans, int milk, int water, double basePrice, String description) {
        this.coffeeBeans = coffeeBeans;
        this.milk = milk;
        this.water = water;
        this.basePrice = basePrice;
        this.description = description;
    }
    
    public int getCoffeeBeans() {
        return coffeeBeans;
    }
    
    public int getMilk() {
        return milk;
    }
    
    public int getWater() {
        return water;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public String getDescription() {
        return description;
    }
}


