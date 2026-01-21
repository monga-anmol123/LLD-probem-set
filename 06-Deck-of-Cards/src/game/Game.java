package game;

import model.Deck;
import model.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base class for card games.
 * Demonstrates Template Method Pattern.
 */
public abstract class Game {
    protected final Deck deck;
    protected final List<Player> players;
    protected boolean gameInProgress;
    
    public Game(Deck deck) {
        if (deck == null) {
            throw new IllegalArgumentException("Deck cannot be null");
        }
        this.deck = deck;
        this.players = new ArrayList<>();
        this.gameInProgress = false;
    }
    
    /**
     * Add a player to the game.
     */
    public void addPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player cannot be null");
        }
        if (gameInProgress) {
            throw new IllegalStateException("Cannot add players while game is in progress");
        }
        players.add(player);
    }
    
    /**
     * Get all players in the game.
     */
    public List<Player> getPlayers() {
        return new ArrayList<>(players);
    }
    
    /**
     * Template method defining the game flow.
     * This is the main algorithm that subclasses customize.
     */
    public final void play() {
        if (players.isEmpty()) {
            throw new IllegalStateException("No players in the game");
        }
        
        gameInProgress = true;
        
        try {
            prepareGame();
            dealInitialCards();
            playRounds();
            determineWinner();
            cleanup();
        } finally {
            gameInProgress = false;
        }
    }
    
    /**
     * Prepare the game (shuffle deck, reset hands, etc.).
     * Can be overridden by subclasses.
     */
    protected void prepareGame() {
        deck.shuffle();
        for (Player player : players) {
            player.clearHand();
        }
    }
    
    /**
     * Deal initial cards to players.
     * Must be implemented by subclasses.
     */
    protected abstract void dealInitialCards();
    
    /**
     * Play the main rounds of the game.
     * Must be implemented by subclasses.
     */
    protected abstract void playRounds();
    
    /**
     * Determine and announce the winner.
     * Must be implemented by subclasses.
     */
    protected abstract void determineWinner();
    
    /**
     * Cleanup after the game.
     * Can be overridden by subclasses.
     */
    protected void cleanup() {
        // Default: do nothing
        // Subclasses can override to add cleanup logic
    }
    
    /**
     * Get the name of the game.
     */
    public abstract String getGameName();
    
    @Override
    public String toString() {
        return String.format("%s [Players: %d, Deck: %s]", 
                           getGameName(), players.size(), deck);
    }
}
