package factory;

import enums.DiceType;
import strategy.DiceStrategy;
import strategy.NormalDice;
import strategy.DoubleDice;
import strategy.LoadedDice;

/**
 * Factory class for creating different types of dice.
 */
public class DiceFactory {
    
    /**
     * Create a dice strategy based on the specified type.
     * 
     * @param type Type of dice to create
     * @return DiceStrategy instance
     */
    public static DiceStrategy createDice(DiceType type) {
        switch (type) {
            case NORMAL:
                return new NormalDice();
            case DOUBLE:
                return new DoubleDice();
            case LOADED:
                return new LoadedDice();
            default:
                return new NormalDice();
        }
    }
}


