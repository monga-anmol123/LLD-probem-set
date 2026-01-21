package enums;

public enum RoomType {
    SINGLE(100.0, 1),
    DOUBLE(150.0, 2),
    SUITE(300.0, 4),
    DELUXE(500.0, 4);
    
    private final double basePrice;
    private final int capacity;
    
    RoomType(double basePrice, int capacity) {
        this.basePrice = basePrice;
        this.capacity = capacity;
    }
    
    public double getBasePrice() {
        return basePrice;
    }
    
    public int getCapacity() {
        return capacity;
    }
}
