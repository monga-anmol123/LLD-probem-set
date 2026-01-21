package model;

public class Position {
    private int row; // 0-7 (1-8 in chess notation)
    private int col; // 0-7 (a-h in chess notation)
    
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public static Position fromNotation(String notation) {
        if (notation == null || notation.length() != 2) {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }
        
        char file = notation.charAt(0); // a-h
        char rank = notation.charAt(1); // 1-8
        
        int col = file - 'a'; // 0-7
        int row = rank - '1'; // 0-7
        
        if (col < 0 || col > 7 || row < 0 || row > 7) {
            throw new IllegalArgumentException("Invalid notation: " + notation);
        }
        
        return new Position(row, col);
    }
    
    public String toNotation() {
        char file = (char)('a' + col);
        char rank = (char)('1' + row);
        return "" + file + rank;
    }
    
    public boolean isValid() {
        return row >= 0 && row <= 7 && col >= 0 && col <= 7;
    }
    
    public int getRow() { return row; }
    public int getCol() { return col; }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return row == position.row && col == position.col;
    }
    
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
    
    @Override
    public String toString() {
        return toNotation();
    }
}
