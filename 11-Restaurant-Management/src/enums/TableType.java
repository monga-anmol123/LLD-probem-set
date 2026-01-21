package enums;

public enum TableType {
    SMALL(2),
    MEDIUM(4),
    LARGE(6),
    VIP(8);

    private final int capacity;

    TableType(int capacity) {
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }
}


