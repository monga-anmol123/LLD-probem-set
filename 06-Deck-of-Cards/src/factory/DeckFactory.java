package factory;

import enums.Rank;
import enums.Suit;
import model.Card;
import model.Deck;

import java.util.ArrayList;
import java.util.List;

/**
 * Factory class for creating different types of decks.
 * Demonstrates Factory Pattern.
 */
public class DeckFactory {
    
    /**
     * Create a standard 52-card deck.
     */
    public static Deck createStandardDeck() {
        return new Deck();
    }
    
    /**
     * Create a shoe (multiple decks combined).
     * Common in casinos for games like Blackjack.
     * 
     * @param numberOfDecks Number of decks to combine (typically 1-8)
     * @return A deck containing all cards from multiple decks
     */
    public static Deck createShoe(int numberOfDecks) {
        if (numberOfDecks < 1 || numberOfDecks > 8) {
            throw new IllegalArgumentException("Number of decks must be between 1 and 8");
        }
        
        List<Card> allCards = new ArrayList<>();
        
        // Add cards from each deck
        for (int i = 0; i < numberOfDecks; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    allCards.add(new Card(rank, suit));
                }
            }
        }
        
        return new Deck(allCards);
    }
    
    /**
     * Create a deck with jokers (54 cards).
     * Note: Joker implementation would require extending the Rank enum.
     * This is a placeholder for extensibility.
     */
    public static Deck createDeckWithJokers() {
        // For now, just return standard deck
        // In a full implementation, would add 2 joker cards
        return createStandardDeck();
    }
    
    /**
     * Create a custom deck with specified number of each card.
     * Useful for testing or special game rules.
     */
    public static Deck createCustomDeck(int copiesOfEachCard) {
        if (copiesOfEachCard < 1) {
            throw new IllegalArgumentException("Copies must be at least 1");
        }
        
        List<Card> allCards = new ArrayList<>();
        
        for (int i = 0; i < copiesOfEachCard; i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    allCards.add(new Card(rank, suit));
                }
            }
        }
        
        return new Deck(allCards);
    }
    
    /**
     * Create a deck with only specific suits.
     * Useful for games that use partial decks.
     */
    public static Deck createPartialDeck(Suit... suits) {
        if (suits == null || suits.length == 0) {
            throw new IllegalArgumentException("Must specify at least one suit");
        }
        
        List<Card> cards = new ArrayList<>();
        
        for (Suit suit : suits) {
            for (Rank rank : Rank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        
        return new Deck(cards);
    }
}
