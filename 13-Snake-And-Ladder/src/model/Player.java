package model;

/**
 * Represents a player in the Snake and Ladder game.
 */
public class Player {
    private final String id;
    private final String name;
    private int currentPosition;

    public Player(String id, String name) {
        this.id = id;
        this.name = name;
        this.currentPosition = 0; // Start off the board
    }

    /**
     * Move the player by the specified number of steps.
     * 
     * @param steps Number of steps to move
     */
    public void move(int steps) {
        this.currentPosition += steps;
    }

    /**
     * Set the player's position directly (used for snakes/ladders).
     * 
     * @param position New position
     */
    public void setPosition(int position) {
        this.currentPosition = position;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    @Override
    public String toString() {
        return name + " (Position: " + currentPosition + ")";
    }
}


