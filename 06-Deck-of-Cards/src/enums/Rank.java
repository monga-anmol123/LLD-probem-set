package enums;

/**
 * Represents the thirteen ranks in a standard deck of cards.
 */
public enum Rank {
    ACE("Ace", "A", 1),
    TWO("Two", "2", 2),
    THREE("Three", "3", 3),
    FOUR("Four", "4", 4),
    FIVE("Five", "5", 5),
    SIX("Six", "6", 6),
    SEVEN("Seven", "7", 7),
    EIGHT("Eight", "8", 8),
    NINE("Nine", "9", 9),
    TEN("Ten", "10", 10),
    JACK("Jack", "J", 10),
    QUEEN("Queen", "Q", 10),
    KING("King", "K", 10);
    
    private final String name;
    private final String symbol;
    private final int value;
    
    Rank(String name, String symbol, int value) {
        this.name = name;
        this.symbol = symbol;
        this.value = value;
    }
    
    public String getName() {
        return name;
    }
    
    public String getSymbol() {
        return symbol;
    }
    
    /**
     * Get the base value of this rank.
     * Note: Aces can be 1 or 11 in Blackjack, but base value is 1.
     */
    public int getValue() {
        return value;
    }
    
    /**
     * Get Blackjack value (Aces are 11, face cards are 10).
     */
    public int getBlackjackValue() {
        if (this == ACE) {
            return 11; // Can be adjusted to 1 later
        }
        return value;
    }
    
    public boolean isAce() {
        return this == ACE;
    }
    
    public boolean isFaceCard() {
        return this == JACK || this == QUEEN || this == KING;
    }
    
    @Override
    public String toString() {
        return symbol;
    }
}
