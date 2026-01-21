package game;

import model.Card;
import model.Deck;
import model.Hand;
import model.Player;

/**
 * Blackjack game implementation.
 * Demonstrates Template Method Pattern - concrete implementation.
 */
public class BlackjackGame extends Game {
    private Player dealer;
    private static final int BLACKJACK = 21;
    private static final int DEALER_STAND_VALUE = 17;
    
    public BlackjackGame(Deck deck) {
        super(deck);
        this.dealer = new Player("Dealer");
    }
    
    @Override
    public String getGameName() {
        return "Blackjack";
    }
    
    @Override
    protected void prepareGame() {
        super.prepareGame();
        dealer.clearHand();
        System.out.println("\n🎰 Starting Blackjack Game...\n");
    }
    
    @Override
    protected void dealInitialCards() {
        // Deal 2 cards to each player
        for (Player player : players) {
            player.getHand().addCard(deck.deal());
            player.getHand().addCard(deck.deal());
        }
        
        // Deal 2 cards to dealer
        dealer.getHand().addCard(deck.deal());
        dealer.getHand().addCard(deck.deal());
        
        // Show initial hands
        displayHands(false); // Hide dealer's second card
    }
    
    @Override
    protected void playRounds() {
        // In a real game, players would choose to hit or stand
        // For this demo, we'll use a simple strategy
        
        for (Player player : players) {
            Hand hand = player.getHand();
            
            // Simple strategy: hit if under 17
            while (hand.getBlackjackValue() < DEALER_STAND_VALUE && !hand.isBust()) {
                System.out.println(player.getName() + " hits...");
                Card card = deck.deal();
                hand.addCard(card);
                System.out.println("  Drew: " + card + " → " + hand);
                
                if (hand.isBust()) {
                    System.out.println("  💥 " + player.getName() + " BUSTS!\n");
                    break;
                }
            }
            
            if (!hand.isBust()) {
                System.out.println("  " + player.getName() + " stands at " + 
                                 hand.getBlackjackValue() + "\n");
            }
        }
        
        // Dealer's turn
        System.out.println("Dealer reveals: " + dealer.getHand() + "\n");
        
        // Dealer hits until 17 or higher
        while (dealer.getHand().getBlackjackValue() < DEALER_STAND_VALUE) {
            System.out.println("Dealer hits...");
            Card card = deck.deal();
            dealer.getHand().addCard(card);
            System.out.println("  Drew: " + card + " → " + dealer.getHand());
            
            if (dealer.getHand().isBust()) {
                System.out.println("  💥 Dealer BUSTS!\n");
                break;
            }
        }
        
        if (!dealer.getHand().isBust()) {
            System.out.println("  Dealer stands at " + 
                             dealer.getHand().getBlackjackValue() + "\n");
        }
    }
    
    @Override
    protected void determineWinner() {
        System.out.println(repeatString("-", 60));
        System.out.println("RESULTS");
        System.out.println(repeatString("-", 60));
        
        Hand dealerHand = dealer.getHand();
        int dealerValue = dealerHand.getBlackjackValue();
        boolean dealerBust = dealerHand.isBust();
        boolean dealerBlackjack = dealerHand.isBlackjack();
        
        for (Player player : players) {
            Hand playerHand = player.getHand();
            int playerValue = playerHand.getBlackjackValue();
            boolean playerBust = playerHand.isBust();
            boolean playerBlackjack = playerHand.isBlackjack();
            
            System.out.println("\n" + player.getName() + ": " + playerHand);
            System.out.println("Dealer: " + dealerHand);
            
            String result;
            if (playerBust) {
                result = "❌ " + player.getName() + " LOSES (Bust)";
            } else if (dealerBust) {
                result = "🎉 " + player.getName() + " WINS (Dealer Bust)";
            } else if (playerBlackjack && !dealerBlackjack) {
                result = "🎉 " + player.getName() + " WINS with BLACKJACK!";
            } else if (dealerBlackjack && !playerBlackjack) {
                result = "❌ " + player.getName() + " LOSES (Dealer Blackjack)";
            } else if (playerValue > dealerValue) {
                result = "🎉 " + player.getName() + " WINS (" + playerValue + " vs " + dealerValue + ")";
            } else if (playerValue < dealerValue) {
                result = "❌ " + player.getName() + " LOSES (" + playerValue + " vs " + dealerValue + ")";
            } else {
                result = "🤝 PUSH (Tie at " + playerValue + ")";
            }
            
            System.out.println(result);
        }
        
        System.out.println(repeatString("-", 60));
    }
    
    /**
     * Display all hands.
     * 
     * @param showDealerHand If false, only show dealer's first card
     */
    private void displayHands(boolean showDealerHand) {
        System.out.println("Initial Hands:");
        
        for (Player player : players) {
            System.out.println("  " + player.getName() + ": " + player.getHand());
        }
        
        if (showDealerHand) {
            System.out.println("  Dealer: " + dealer.getHand());
        } else {
            // Show only first card
            Card firstCard = dealer.getHand().getCards().get(0);
            System.out.println("  Dealer: " + firstCard + " [?]");
        }
        
        System.out.println();
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
