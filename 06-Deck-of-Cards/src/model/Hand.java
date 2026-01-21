package model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a hand of cards held by a player.
 */
public class Hand {
    private final List<Card> cards;
    
    public Hand() {
        this.cards = new ArrayList<>();
    }
    
    /**
     * Add a card to the hand.
     */
    public void addCard(Card card) {
        if (card == null) {
            throw new IllegalArgumentException("Card cannot be null");
        }
        cards.add(card);
    }
    
    /**
     * Add multiple cards to the hand.
     */
    public void addCards(List<Card> cards) {
        if (cards == null) {
            throw new IllegalArgumentException("Cards cannot be null");
        }
        this.cards.addAll(cards);
    }
    
    /**
     * Get all cards in the hand.
     */
    public List<Card> getCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Get the number of cards in the hand.
     */
    public int size() {
        return cards.size();
    }
    
    /**
     * Check if the hand is empty.
     */
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    
    /**
     * Clear all cards from the hand.
     */
    public void clear() {
        cards.clear();
    }
    
    /**
     * Calculate the Blackjack value of the hand.
     * Aces are counted as 11, but adjusted to 1 if hand would bust.
     */
    public int getBlackjackValue() {
        int sum = 0;
        int aces = 0;
        
        // First pass: count all cards, aces as 11
        for (Card card : cards) {
            sum += card.getBlackjackValue();
            if (card.isAce()) {
                aces++;
            }
        }
        
        // Adjust aces from 11 to 1 if needed to avoid bust
        while (sum > 21 && aces > 0) {
            sum -= 10; // Change ace from 11 to 1
            aces--;
        }
        
        return sum;
    }
    
    /**
     * Check if this is a Blackjack (21 with exactly 2 cards).
     */
    public boolean isBlackjack() {
        return size() == 2 && getBlackjackValue() == 21;
    }
    
    /**
     * Check if the hand is bust (over 21).
     */
    public boolean isBust() {
        return getBlackjackValue() > 21;
    }
    
    /**
     * Get a string representation showing all cards.
     */
    public String toCardsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cards.size(); i++) {
            if (i > 0) sb.append(" ");
            sb.append(cards.get(i));
        }
        return sb.toString();
    }
    
    @Override
    public String toString() {
        return String.format("[%s] = %d", toCardsString(), getBlackjackValue());
    }
}
