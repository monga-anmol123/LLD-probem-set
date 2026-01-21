package service;

import enums.GameStatus;
import model.Board;
import model.Player;
import strategy.DiceStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Main game service that manages the Snake and Ladder game flow.
 */
public class Game {
    private final Board board;
    private final List<Player> players;
    private final DiceStrategy dice;
    private int currentPlayerIndex;
    private GameStatus status;
    private Player winner;

    public Game(Board board, List<Player> players, DiceStrategy dice) {
        if (players == null || players.size() < 2 || players.size() > 4) {
            throw new IllegalArgumentException("Game requires 2-4 players");
        }
        this.board = board;
        this.players = new ArrayList<>(players);
        this.dice = dice;
        this.currentPlayerIndex = 0;
        this.status = GameStatus.NOT_STARTED;
        this.winner = null;
    }

    /**
     * Start the game and play until someone wins.
     */
    public void start() {
        status = GameStatus.IN_PROGRESS;
        System.out.println("\n" + repeatString("=", 60));
        System.out.println("🎲 SNAKE AND LADDER GAME STARTED! 🎲");
        System.out.println(repeatString("=", 60));
        System.out.println("Board Size: " + board.getSize());
        System.out.println("Dice Type: " + dice.getName());
        System.out.println("Players: " + players.size());
        for (Player player : players) {
            System.out.println("  - " + player.getName());
        }
        System.out.println("Snakes: " + board.getSnakes().size());
        board.getSnakes().forEach(snake -> System.out.println("  " + snake));
        System.out.println("Ladders: " + board.getLadders().size());
        board.getLadders().forEach(ladder -> System.out.println("  " + ladder));
        System.out.println(repeatString("=", 60) + "\n");

        int turnNumber = 1;
        while (!isGameOver()) {
            Player currentPlayer = players.get(currentPlayerIndex);
            System.out.println("Turn " + turnNumber + ": " + currentPlayer.getName() + "'s turn");
            playTurn(currentPlayer);
            
            if (board.isFinalPosition(currentPlayer.getCurrentPosition())) {
                winner = currentPlayer;
                status = GameStatus.COMPLETED;
                System.out.println("\n" + repeatString("🎉", 30));
                System.out.println("🏆 " + winner.getName() + " WINS THE GAME! 🏆");
                System.out.println(repeatString("🎉", 30) + "\n");
                break;
            }
            
            currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            turnNumber++;
            System.out.println();
        }
    }

    /**
     * Play a single turn for the given player.
     * 
     * @param player Player whose turn it is
     */
    private void playTurn(Player player) {
        int currentPosition = player.getCurrentPosition();
        int diceValue = dice.roll();
        
        System.out.println("  " + player.getName() + " rolled: " + diceValue);
        
        int targetPosition = currentPosition + diceValue;
        
        // Check if move would exceed board size
        if (targetPosition > board.getSize()) {
            System.out.println("  ❌ Cannot move! Would exceed board size (" + targetPosition + " > " + board.getSize() + ")");
            System.out.println("  " + player.getName() + " stays at position " + currentPosition);
            return;
        }
        
        // Move player
        player.setPosition(targetPosition);
        System.out.println("  " + player.getName() + " moved from " + currentPosition + " to " + targetPosition);
        
        // Check for snake
        if (board.hasSnake(targetPosition)) {
            int snakeTail = board.getSnakeTail(targetPosition);
            System.out.println("  🐍 Oh no! Hit a snake at " + targetPosition + "!");
            System.out.println("  " + player.getName() + " slides down to " + snakeTail);
            player.setPosition(snakeTail);
        }
        // Check for ladder
        else if (board.hasLadder(targetPosition)) {
            int ladderEnd = board.getLadderEnd(targetPosition);
            System.out.println("  🪜 Yay! Found a ladder at " + targetPosition + "!");
            System.out.println("  " + player.getName() + " climbs up to " + ladderEnd);
            player.setPosition(ladderEnd);
        }
        
        System.out.println("  📍 " + player.getName() + " is now at position " + player.getCurrentPosition());
    }

    /**
     * Check if the game is over.
     * 
     * @return true if game is completed
     */
    public boolean isGameOver() {
        return status == GameStatus.COMPLETED;
    }

    /**
     * Get the winner of the game.
     * 
     * @return Winner player, or null if game is not over
     */
    public Player getWinner() {
        return winner;
    }

    /**
     * Get current game status.
     * 
     * @return Current game status
     */
    public GameStatus getStatus() {
        return status;
    }

    /**
     * Display current game state.
     */
    public void displayGameState() {
        System.out.println("\n" + repeatString("-", 60));
        System.out.println("CURRENT GAME STATE");
        System.out.println(repeatString("-", 60));
        System.out.println("Status: " + status);
        System.out.println("Players:");
        for (int i = 0; i < players.size(); i++) {
            String marker = (i == currentPlayerIndex) ? " ← Current" : "";
            System.out.println("  " + players.get(i) + marker);
        }
        if (winner != null) {
            System.out.println("Winner: " + winner.getName());
        }
        System.out.println(repeatString("-", 60) + "\n");
    }

    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     * 
     * @param str String to repeat
     * @param count Number of times to repeat
     * @return Repeated string
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

