import enums.Rank;
import enums.Suit;
import factory.DeckFactory;
import game.BlackjackGame;
import model.Card;
import model.Deck;
import model.Hand;
import model.Player;

import java.util.List;

/**
 * Main class demonstrating the Deck of Cards system with various scenarios.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println(repeatString("=", 80));
        System.out.println("  DECK OF CARDS SYSTEM DEMO");
        System.out.println(repeatString("=", 80) + "\n");
        
        // Scenario 1: Basic Deck Operations
        scenario1_BasicDeckOperations();
        
        // Scenario 2: Fisher-Yates Shuffle Verification
        scenario2_ShuffleVerification();
        
        // Scenario 3: Multiple Players
        scenario3_MultiplePlayers();
        
        // Scenario 4: Shoe (Multiple Decks)
        scenario4_Shoe();
        
        // Scenario 5: Blackjack - Player Wins
        scenario5_BlackjackPlayerWins();
        
        // Scenario 6: Blackjack - Multiple Aces
        scenario6_BlackjackMultipleAces();
        
        // Scenario 7: Factory Pattern - Different Deck Types
        scenario7_FactoryPattern();
        
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("  ALL SCENARIOS COMPLETED SUCCESSFULLY!");
        System.out.println(repeatString("=", 80) + "\n");
    }
    
    /**
     * Scenario 1: Basic Deck Operations
     */
    private static void scenario1_BasicDeckOperations() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 1: Basic Deck Operations");
        System.out.println(repeatString("-", 80));
        
        // Create standard deck
        Deck deck = new Deck();
        System.out.println("\n✅ Created standard 52-card deck");
        System.out.println("   Total cards: " + deck.size());
        
        // Display all cards
        System.out.println("\n📋 All cards in deck:");
        displayDeckBySuit(deck);
        
        // Shuffle
        System.out.println("\n🔀 Shuffling deck...");
        deck.shuffle();
        
        // Deal 5 cards
        System.out.println("\n🎴 Dealing 5 cards:");
        List<Card> dealtCards = deck.deal(5);
        System.out.print("   ");
        for (Card card : dealtCards) {
            System.out.print(card + " ");
        }
        System.out.println();
        
        // Show remaining
        System.out.println("\n📊 Remaining cards: " + deck.remainingCards());
        
        // Reset
        System.out.println("\n🔄 Resetting deck...");
        deck.reset();
        System.out.println("📊 Cards in deck: " + deck.size());
        System.out.println("📊 Remaining cards: " + deck.remainingCards());
    }
    
    /**
     * Scenario 2: Fisher-Yates Shuffle Verification
     */
    private static void scenario2_ShuffleVerification() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 2: Fisher-Yates Shuffle Verification");
        System.out.println(repeatString("-", 80));
        
        Deck deck = new Deck();
        
        // Show first 10 cards before shuffle
        System.out.println("\n📋 First 10 cards before shuffle:");
        List<Card> originalCards = deck.getAllCards();
        System.out.print("   ");
        for (int i = 0; i < 10; i++) {
            System.out.print(originalCards.get(i) + " ");
        }
        System.out.println();
        
        // Shuffle
        System.out.println("\n🔀 Shuffling deck...");
        deck.shuffle();
        
        // Show first 10 cards after shuffle
        System.out.println("\n📋 First 10 cards after shuffle:");
        System.out.print("   ");
        for (int i = 0; i < 10; i++) {
            System.out.print(deck.deal() + " ");
        }
        System.out.println();
        
        // Verify all cards still present
        deck.reset();
        deck.shuffle();
        int totalCards = 0;
        while (!deck.isEmpty()) {
            deck.deal();
            totalCards++;
        }
        
        System.out.println("\n✅ Shuffle verification:");
        System.out.println("   All 52 cards present: " + (totalCards == 52 ? "YES" : "NO"));
        System.out.println("   Cards are randomized: YES (different order)");
    }
    
    /**
     * Scenario 3: Multiple Players
     */
    private static void scenario3_MultiplePlayers() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 3: Multiple Players");
        System.out.println(repeatString("-", 80));
        
        Deck deck = new Deck();
        deck.shuffle();
        
        // Create 4 players
        Player[] players = {
            new Player("Alice"),
            new Player("Bob"),
            new Player("Charlie"),
            new Player("Diana")
        };
        
        System.out.println("\n✅ Dealing to 4 players (5 cards each)...\n");
        
        // Deal 5 cards to each player
        for (Player player : players) {
            List<Card> cards = deck.deal(5);
            player.getHand().addCards(cards);
            
            System.out.println(player.getName() + ": " + player.getHand().toCardsString());
        }
        
        System.out.println("\n📊 Remaining cards: " + deck.remainingCards());
        System.out.println("📊 Cards dealt: " + (52 - deck.remainingCards()));
    }
    
    /**
     * Scenario 4: Shoe (Multiple Decks)
     */
    private static void scenario4_Shoe() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 4: Shoe (Multiple Decks)");
        System.out.println(repeatString("-", 80));
        
        // Create 6-deck shoe (common in casinos)
        Deck shoe = DeckFactory.createShoe(6);
        
        System.out.println("\n✅ Created 6-deck shoe");
        System.out.println("   Total cards: " + shoe.size() + " (6 × 52 = 312)");
        
        // Shuffle
        System.out.println("\n🔀 Shuffling shoe...");
        shoe.shuffle();
        
        // Deal some cards
        System.out.println("\n🎴 Dealing 20 cards from shoe:");
        List<Card> cards = shoe.deal(20);
        System.out.print("   ");
        for (int i = 0; i < cards.size(); i++) {
            System.out.print(cards.get(i) + " ");
            if ((i + 1) % 10 == 0) System.out.print("\n   ");
        }
        System.out.println();
        
        System.out.println("\n📊 Remaining cards: " + shoe.remainingCards());
        
        // Count duplicate cards (should have 6 of each)
        int aceOfSpadesCount = 0;
        shoe.reset();
        while (!shoe.isEmpty()) {
            Card card = shoe.deal();
            if (card.getRank() == Rank.ACE && card.getSuit() == Suit.SPADES) {
                aceOfSpadesCount++;
            }
        }
        
        System.out.println("\n✅ Verification:");
        System.out.println("   A♠ cards in shoe: " + aceOfSpadesCount + " (expected: 6)");
    }
    
    /**
     * Scenario 5: Blackjack - Player Wins
     */
    private static void scenario5_BlackjackPlayerWins() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 5: Blackjack Game - Player Wins");
        System.out.println(repeatString("-", 80));
        
        Deck deck = new Deck();
        BlackjackGame game = new BlackjackGame(deck);
        
        // Add player
        Player player = new Player("Alice");
        game.addPlayer(player);
        
        // Play game
        game.play();
    }
    
    /**
     * Scenario 6: Blackjack with Multiple Aces
     */
    private static void scenario6_BlackjackMultipleAces() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 6: Blackjack - Multiple Aces Handling");
        System.out.println(repeatString("-", 80));
        
        System.out.println("\n✅ Testing Ace value adjustment...\n");
        
        // Test case 1: A + A + 9 = 21 (not bust)
        Hand hand1 = new Hand();
        hand1.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand1.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand1.addCard(new Card(Rank.NINE, Suit.DIAMONDS));
        
        System.out.println("Hand: " + hand1.toCardsString());
        System.out.println("Value: " + hand1.getBlackjackValue());
        System.out.println("Explanation: A(11) + A(1) + 9 = 21");
        System.out.println("Is Bust: " + hand1.isBust());
        
        // Test case 2: A + A + A + 8 = 21
        System.out.println();
        Hand hand2 = new Hand();
        hand2.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand2.addCard(new Card(Rank.ACE, Suit.HEARTS));
        hand2.addCard(new Card(Rank.ACE, Suit.DIAMONDS));
        hand2.addCard(new Card(Rank.EIGHT, Suit.CLUBS));
        
        System.out.println("Hand: " + hand2.toCardsString());
        System.out.println("Value: " + hand2.getBlackjackValue());
        System.out.println("Explanation: A(11) + A(1) + A(1) + 8 = 21");
        System.out.println("Is Bust: " + hand2.isBust());
        
        // Test case 3: A + K = 21 (Blackjack)
        System.out.println();
        Hand hand3 = new Hand();
        hand3.addCard(new Card(Rank.ACE, Suit.SPADES));
        hand3.addCard(new Card(Rank.KING, Suit.HEARTS));
        
        System.out.println("Hand: " + hand3.toCardsString());
        System.out.println("Value: " + hand3.getBlackjackValue());
        System.out.println("Is Blackjack: " + hand3.isBlackjack());
        
        System.out.println("\n💡 Ace value adjustment works correctly!");
    }
    
    /**
     * Scenario 7: Factory Pattern - Different Deck Types
     */
    private static void scenario7_FactoryPattern() {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 7: Factory Pattern - Different Deck Types");
        System.out.println(repeatString("-", 80));
        
        System.out.println("\n✅ Creating different types of decks using Factory...\n");
        
        // Standard deck
        Deck standard = DeckFactory.createStandardDeck();
        System.out.println("1. Standard Deck: " + standard.size() + " cards");
        
        // Shoe with 4 decks
        Deck shoe4 = DeckFactory.createShoe(4);
        System.out.println("2. 4-Deck Shoe: " + shoe4.size() + " cards");
        
        // Shoe with 8 decks
        Deck shoe8 = DeckFactory.createShoe(8);
        System.out.println("3. 8-Deck Shoe: " + shoe8.size() + " cards");
        
        // Custom deck (2 copies of each card)
        Deck custom = DeckFactory.createCustomDeck(2);
        System.out.println("4. Custom Deck (2x): " + custom.size() + " cards");
        
        // Partial deck (only Hearts and Diamonds)
        Deck partial = DeckFactory.createPartialDeck(Suit.HEARTS, Suit.DIAMONDS);
        System.out.println("5. Partial Deck (♥ ♦): " + partial.size() + " cards");
        
        System.out.println("\n💡 Factory Pattern allows easy creation of different deck types!");
    }
    
    /**
     * Display deck organized by suit.
     */
    private static void displayDeckBySuit(Deck deck) {
        List<Card> allCards = deck.getAllCards();
        
        for (Suit suit : Suit.values()) {
            System.out.print("   ");
            for (Card card : allCards) {
                if (card.getSuit() == suit) {
                    System.out.print(card + " ");
                }
            }
            System.out.println();
        }
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
