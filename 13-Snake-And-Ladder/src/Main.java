import enums.DiceType;
import factory.DiceFactory;
import model.Board;
import model.Ladder;
import model.Player;
import model.Snake;
import service.Game;
import strategy.DiceStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * Main class demonstrating the Snake and Ladder game with various scenarios.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("\n" + repeatString("█", 70));
        System.out.println("█" + repeatString(" ", 68) + "█");
        System.out.println("█" + repeatString(" ", 15) + "SNAKE AND LADDER GAME - DEMO" + repeatString(" ", 25) + "█");
        System.out.println("█" + repeatString(" ", 68) + "█");
        System.out.println(repeatString("█", 70) + "\n");
        
        // Scenario 1: Basic game with normal dice
        scenario1_BasicGame();
        
        // Scenario 2: Game with double dice
        scenario2_DoubleDiceGame();
        
        // Scenario 3: Small board game
        scenario3_SmallBoardGame();
        
        // Scenario 4: Game with loaded dice
        scenario4_LoadedDiceGame();
        
        // Scenario 5: Game with many snakes and ladders
        scenario5_ComplexBoard();
        
        System.out.println("\n" + repeatString("█", 70));
        System.out.println("█" + repeatString(" ", 68) + "█");
        System.out.println("█" + repeatString(" ", 20) + "ALL SCENARIOS COMPLETED!" + repeatString(" ", 25) + "█");
        System.out.println("█" + repeatString(" ", 68) + "█");
        System.out.println(repeatString("█", 70) + "\n");
    }
    
    /**
     * Scenario 1: Basic game with 3 players and normal dice.
     */
    private static void scenario1_BasicGame() {
        System.out.println("\n" + repeatString("▓", 70));
        System.out.println("SCENARIO 1: Basic Game (3 Players, Normal Dice, 100 Cells)");
        System.out.println(repeatString("▓", 70));
        
        // Create board
        Board board = new Board(100);
        
        // Add snakes
        board.addSnake(new Snake(17, 7));
        board.addSnake(new Snake(54, 34));
        board.addSnake(new Snake(62, 19));
        board.addSnake(new Snake(64, 60));
        board.addSnake(new Snake(87, 24));
        board.addSnake(new Snake(93, 73));
        board.addSnake(new Snake(95, 75));
        board.addSnake(new Snake(99, 78));
        
        // Add ladders
        board.addLadder(new Ladder(3, 38));
        board.addLadder(new Ladder(8, 31));
        board.addLadder(new Ladder(28, 84));
        board.addLadder(new Ladder(58, 77));
        board.addLadder(new Ladder(75, 86));
        board.addLadder(new Ladder(80, 97));
        board.addLadder(new Ladder(36, 44));
        board.addLadder(new Ladder(71, 91));
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player("P1", "Alice"));
        players.add(new Player("P2", "Bob"));
        players.add(new Player("P3", "Charlie"));
        
        // Create dice
        DiceStrategy dice = DiceFactory.createDice(DiceType.NORMAL);
        
        // Create and start game
        Game game = new Game(board, players, dice);
        game.start();
    }
    
    /**
     * Scenario 2: Game with double dice (faster gameplay).
     */
    private static void scenario2_DoubleDiceGame() {
        System.out.println("\n" + repeatString("▓", 70));
        System.out.println("SCENARIO 2: Double Dice Game (2 Players, Double Dice, 100 Cells)");
        System.out.println(repeatString("▓", 70));
        
        // Create board
        Board board = new Board(100);
        
        // Add fewer snakes and ladders for faster game
        board.addSnake(new Snake(45, 25));
        board.addSnake(new Snake(78, 38));
        board.addSnake(new Snake(88, 24));
        board.addSnake(new Snake(96, 42));
        
        board.addLadder(new Ladder(5, 58));
        board.addLadder(new Ladder(15, 44));
        board.addLadder(new Ladder(35, 64));
        board.addLadder(new Ladder(50, 82));
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player("P1", "David"));
        players.add(new Player("P2", "Emma"));
        
        // Create double dice
        DiceStrategy dice = DiceFactory.createDice(DiceType.DOUBLE);
        
        // Create and start game
        Game game = new Game(board, players, dice);
        game.start();
    }
    
    /**
     * Scenario 3: Small board for quick game.
     */
    private static void scenario3_SmallBoardGame() {
        System.out.println("\n" + repeatString("▓", 70));
        System.out.println("SCENARIO 3: Small Board Game (4 Players, Normal Dice, 50 Cells)");
        System.out.println(repeatString("▓", 70));
        
        // Create small board
        Board board = new Board(50);
        
        // Add snakes
        board.addSnake(new Snake(27, 5));
        board.addSnake(new Snake(35, 15));
        board.addSnake(new Snake(48, 10));
        
        // Add ladders
        board.addLadder(new Ladder(4, 25));
        board.addLadder(new Ladder(13, 33));
        board.addLadder(new Ladder(20, 39));
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player("P1", "Frank"));
        players.add(new Player("P2", "Grace"));
        players.add(new Player("P3", "Henry"));
        players.add(new Player("P4", "Ivy"));
        
        // Create dice
        DiceStrategy dice = DiceFactory.createDice(DiceType.NORMAL);
        
        // Create and start game
        Game game = new Game(board, players, dice);
        game.start();
    }
    
    /**
     * Scenario 4: Game with loaded dice (biased towards higher values).
     */
    private static void scenario4_LoadedDiceGame() {
        System.out.println("\n" + repeatString("▓", 70));
        System.out.println("SCENARIO 4: Loaded Dice Game (2 Players, Loaded Dice, 100 Cells)");
        System.out.println(repeatString("▓", 70));
        
        // Create board
        Board board = new Board(100);
        
        // Add snakes
        board.addSnake(new Snake(32, 10));
        board.addSnake(new Snake(56, 33));
        board.addSnake(new Snake(85, 45));
        board.addSnake(new Snake(98, 28));
        
        // Add ladders
        board.addLadder(new Ladder(7, 29));
        board.addLadder(new Ladder(18, 42));
        board.addLadder(new Ladder(40, 66));
        board.addLadder(new Ladder(70, 92));
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player("P1", "Jack"));
        players.add(new Player("P2", "Kate"));
        
        // Create loaded dice
        DiceStrategy dice = DiceFactory.createDice(DiceType.LOADED);
        
        // Create and start game
        Game game = new Game(board, players, dice);
        game.start();
    }
    
    /**
     * Scenario 5: Complex board with many snakes and ladders.
     */
    private static void scenario5_ComplexBoard() {
        System.out.println("\n" + repeatString("▓", 70));
        System.out.println("SCENARIO 5: Complex Board (3 Players, Normal Dice, Many Obstacles)");
        System.out.println(repeatString("▓", 70));
        
        // Create board
        Board board = new Board(100);
        
        // Add many snakes
        board.addSnake(new Snake(16, 6));
        board.addSnake(new Snake(22, 3));
        board.addSnake(new Snake(34, 12));
        board.addSnake(new Snake(47, 26));
        board.addSnake(new Snake(52, 29));
        board.addSnake(new Snake(62, 18));
        board.addSnake(new Snake(69, 33));
        board.addSnake(new Snake(74, 45));
        board.addSnake(new Snake(83, 19));
        board.addSnake(new Snake(92, 51));
        board.addSnake(new Snake(95, 24));
        board.addSnake(new Snake(99, 5));
        
        // Add many ladders
        board.addLadder(new Ladder(2, 37));
        board.addLadder(new Ladder(4, 14));
        board.addLadder(new Ladder(9, 31));
        board.addLadder(new Ladder(20, 42));
        board.addLadder(new Ladder(28, 56));
        board.addLadder(new Ladder(40, 77));
        board.addLadder(new Ladder(50, 67));
        board.addLadder(new Ladder(54, 88));
        board.addLadder(new Ladder(63, 81));
        board.addLadder(new Ladder(71, 89));
        board.addLadder(new Ladder(80, 96));
        
        // Create players
        List<Player> players = new ArrayList<>();
        players.add(new Player("P1", "Leo"));
        players.add(new Player("P2", "Mia"));
        players.add(new Player("P3", "Noah"));
        
        // Create dice
        DiceStrategy dice = DiceFactory.createDice(DiceType.NORMAL);
        
        // Create and start game
        Game game = new Game(board, players, dice);
        game.start();
    }

    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     * 
     * @param str String to repeat
     * @param count Number of times to repeat
     * @return Repeated string
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

