package model;

/**
 * Represents a player in a card game.
 */
public class Player {
    private final String name;
    private final Hand hand;
    private int chips;
    
    public Player(String name) {
        this(name, 1000); // Default 1000 chips
    }
    
    public Player(String name, int chips) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Player name cannot be empty");
        }
        this.name = name;
        this.hand = new Hand();
        this.chips = chips;
    }
    
    public String getName() {
        return name;
    }
    
    public Hand getHand() {
        return hand;
    }
    
    public int getChips() {
        return chips;
    }
    
    public void addChips(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot add negative chips");
        }
        this.chips += amount;
    }
    
    public void removeChips(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Cannot remove negative chips");
        }
        if (amount > chips) {
            throw new IllegalStateException("Not enough chips");
        }
        this.chips -= amount;
    }
    
    public boolean hasEnoughChips(int amount) {
        return chips >= amount;
    }
    
    public void clearHand() {
        hand.clear();
    }
    
    @Override
    public String toString() {
        return String.format("%s (Chips: %d)", name, chips);
    }
}
