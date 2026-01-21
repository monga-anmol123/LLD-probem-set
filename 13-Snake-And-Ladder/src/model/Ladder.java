package model;

/**
 * Represents a ladder on the board.
 * When a player lands on the start, they climb up to the end.
 */
public class Ladder {
    private final int start;
    private final int end;

    public Ladder(int start, int end) {
        if (start >= end) {
            throw new IllegalArgumentException("Ladder start must be less than end");
        }
        this.start = start;
        this.end = end;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    @Override
    public String toString() {
        return "Ladder[" + start + " → " + end + "]";
    }
}


