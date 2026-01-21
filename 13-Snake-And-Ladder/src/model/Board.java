package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the game board with snakes and ladders.
 */
public class Board {
    private final int size;
    private final List<Snake> snakes;
    private final List<Ladder> ladders;
    private final Map<Integer, Integer> snakeMap; // head -> tail
    private final Map<Integer, Integer> ladderMap; // start -> end

    public Board(int size) {
        if (size < 10) {
            throw new IllegalArgumentException("Board size must be at least 10");
        }
        this.size = size;
        this.snakes = new ArrayList<>();
        this.ladders = new ArrayList<>();
        this.snakeMap = new HashMap<>();
        this.ladderMap = new HashMap<>();
    }

    /**
     * Add a snake to the board.
     * 
     * @param snake Snake to add
     */
    public void addSnake(Snake snake) {
        if (snake.getHead() >= size || snake.getTail() < 1) {
            throw new IllegalArgumentException("Invalid snake position");
        }
        if (snakeMap.containsKey(snake.getHead()) || ladderMap.containsKey(snake.getHead())) {
            throw new IllegalArgumentException("Position " + snake.getHead() + " already has a snake or ladder");
        }
        snakes.add(snake);
        snakeMap.put(snake.getHead(), snake.getTail());
    }

    /**
     * Add a ladder to the board.
     * 
     * @param ladder Ladder to add
     */
    public void addLadder(Ladder ladder) {
        if (ladder.getEnd() > size || ladder.getStart() < 1) {
            throw new IllegalArgumentException("Invalid ladder position");
        }
        if (snakeMap.containsKey(ladder.getStart()) || ladderMap.containsKey(ladder.getStart())) {
            throw new IllegalArgumentException("Position " + ladder.getStart() + " already has a snake or ladder");
        }
        ladders.add(ladder);
        ladderMap.put(ladder.getStart(), ladder.getEnd());
    }

    /**
     * Get the new position after considering snakes and ladders.
     * 
     * @param currentPosition Current position
     * @param diceValue Dice roll value
     * @return New position after move and any snake/ladder effects
     */
    public int getNewPosition(int currentPosition, int diceValue) {
        int newPosition = currentPosition + diceValue;

        // Check if exceeds board size
        if (newPosition > size) {
            return currentPosition; // Stay at current position
        }

        // Check for snake
        if (snakeMap.containsKey(newPosition)) {
            return snakeMap.get(newPosition);
        }

        // Check for ladder
        if (ladderMap.containsKey(newPosition)) {
            return ladderMap.get(newPosition);
        }

        return newPosition;
    }

    /**
     * Check if the position is the final position on the board.
     * 
     * @param position Position to check
     * @return true if it's the final position
     */
    public boolean isFinalPosition(int position) {
        return position == size;
    }

    /**
     * Check if there's a snake at the given position.
     * 
     * @param position Position to check
     * @return true if there's a snake head at this position
     */
    public boolean hasSnake(int position) {
        return snakeMap.containsKey(position);
    }

    /**
     * Check if there's a ladder at the given position.
     * 
     * @param position Position to check
     * @return true if there's a ladder start at this position
     */
    public boolean hasLadder(int position) {
        return ladderMap.containsKey(position);
    }

    /**
     * Get the snake tail position for a given head position.
     * 
     * @param head Snake head position
     * @return Snake tail position, or -1 if no snake at this position
     */
    public int getSnakeTail(int head) {
        return snakeMap.getOrDefault(head, -1);
    }

    /**
     * Get the ladder end position for a given start position.
     * 
     * @param start Ladder start position
     * @return Ladder end position, or -1 if no ladder at this position
     */
    public int getLadderEnd(int start) {
        return ladderMap.getOrDefault(start, -1);
    }

    // Getters
    public int getSize() {
        return size;
    }

    public List<Snake> getSnakes() {
        return new ArrayList<>(snakes);
    }

    public List<Ladder> getLadders() {
        return new ArrayList<>(ladders);
    }

    @Override
    public String toString() {
        return "Board[Size=" + size + ", Snakes=" + snakes.size() + ", Ladders=" + ladders.size() + "]";
    }
}


