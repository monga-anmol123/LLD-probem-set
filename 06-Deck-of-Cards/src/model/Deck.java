package model;

import enums.Rank;
import enums.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a deck of playing cards with shuffle, deal, and reset operations.
 */
public class Deck {
    private final List<Card> cards;
    private final List<Card> originalCards;
    private int currentIndex;
    private final Random random;
    
    /**
     * Create a standard 52-card deck.
     */
    public Deck() {
        this.cards = new ArrayList<>();
        this.random = new Random();
        this.currentIndex = 0;
        
        // Create all 52 cards
        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        
        // Store original order for reset
        this.originalCards = new ArrayList<>(cards);
    }
    
    /**
     * Create a deck with specified cards (for custom decks or shoes).
     */
    public Deck(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.originalCards = new ArrayList<>(cards);
        this.random = new Random();
        this.currentIndex = 0;
    }
    
    /**
     * Shuffle the deck using Fisher-Yates algorithm.
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    public void shuffle() {
        currentIndex = 0;
        
        // Fisher-Yates shuffle
        for (int i = cards.size() - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            // Swap cards[i] and cards[j]
            Card temp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, temp);
        }
    }
    
    /**
     * Deal a single card from the deck.
     * 
     * @return The dealt card
     * @throws IllegalStateException if deck is empty
     */
    public Card deal() {
        if (isEmpty()) {
            throw new IllegalStateException("Cannot deal from empty deck");
        }
        return cards.get(currentIndex++);
    }
    
    /**
     * Deal multiple cards from the deck.
     * 
     * @param count Number of cards to deal
     * @return List of dealt cards
     * @throws IllegalStateException if not enough cards
     */
    public List<Card> deal(int count) {
        if (count > remainingCards()) {
            throw new IllegalStateException(
                String.format("Cannot deal %d cards, only %d remaining", count, remainingCards())
            );
        }
        
        List<Card> dealtCards = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            dealtCards.add(deal());
        }
        return dealtCards;
    }
    
    /**
     * Reset the deck to its original state.
     */
    public void reset() {
        cards.clear();
        cards.addAll(originalCards);
        currentIndex = 0;
    }
    
    /**
     * Get the number of remaining cards in the deck.
     */
    public int remainingCards() {
        return cards.size() - currentIndex;
    }
    
    /**
     * Get the total number of cards in the deck (including dealt cards).
     */
    public int size() {
        return cards.size();
    }
    
    /**
     * Check if the deck is empty.
     */
    public boolean isEmpty() {
        return remainingCards() == 0;
    }
    
    /**
     * Get all cards in the deck (for display purposes).
     */
    public List<Card> getAllCards() {
        return new ArrayList<>(cards);
    }
    
    /**
     * Get remaining cards in the deck.
     */
    public List<Card> getRemainingCards() {
        return new ArrayList<>(cards.subList(currentIndex, cards.size()));
    }
    
    @Override
    public String toString() {
        return String.format("Deck[total=%d, remaining=%d]", size(), remainingCards());
    }
}
