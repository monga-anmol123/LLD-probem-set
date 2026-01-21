package enums;

/**
 * Represents the four suits in a standard deck of cards.
 */
public enum Suit {
    SPADES("Spades", "♠"),
    HEARTS("Hearts", "♥"),
    DIAMONDS("Diamonds", "♦"),
    CLUBS("Clubs", "♣");
    
    private final String name;
    private final String symbol;
    
    Suit(String name, String symbol) {
        this.name = name;
        this.symbol = symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}
