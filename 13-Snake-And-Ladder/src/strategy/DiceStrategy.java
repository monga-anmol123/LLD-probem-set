package strategy;

/**
 * Strategy interface for different dice rolling mechanisms.
 */
public interface DiceStrategy {
    /**
     * Roll the dice and return the result.
     * 
     * @return The dice roll value
     */
    int roll();
    
    /**
     * Get the name of this dice strategy.
     * 
     * @return Strategy name
     */
    String getName();
}


