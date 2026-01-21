package model;

import enums.Rank;
import enums.Suit;

/**
 * Immutable Card class representing a playing card.
 * Thread-safe by design.
 */
public final class Card {
    private final Rank rank;
    private final Suit suit;
    
    public Card(Rank rank, Suit suit) {
        if (rank == null || suit == null) {
            throw new IllegalArgumentException("Rank and Suit cannot be null");
        }
        this.rank = rank;
        this.suit = suit;
    }
    
    public Rank getRank() {
        return rank;
    }
    
    public Suit getSuit() {
        return suit;
    }
    
    /**
     * Get the base value of this card.
     */
    public int getValue() {
        return rank.getValue();
    }
    
    /**
     * Get the Blackjack value of this card.
     */
    public int getBlackjackValue() {
        return rank.getBlackjackValue();
    }
    
    /**
     * Check if this card is an Ace.
     */
    public boolean isAce() {
        return rank.isAce();
    }
    
    /**
     * Check if this card is a face card (J, Q, K).
     */
    public boolean isFaceCard() {
        return rank.isFaceCard();
    }
    
    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }
    
    /**
     * Detailed string representation.
     */
    public String toDetailedString() {
        return rank.getName() + " of " + suit.getName();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Card card = (Card) obj;
        return rank == card.rank && suit == card.suit;
    }
    
    @Override
    public int hashCode() {
        return 31 * rank.hashCode() + suit.hashCode();
    }
}
