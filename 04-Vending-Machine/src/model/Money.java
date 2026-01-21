package model;

import enums.MoneyType;

/**
 * Money represents coins and notes with their values.
 */
public class Money {
    private final double value;
    private final MoneyType type;

    public Money(double value, MoneyType type) {
        this.value = value;
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public MoneyType getType() {
        return type;
    }

    @Override
    public String toString() {
        if (type == MoneyType.COIN) {
            return String.format("%.0f¢", value * 100);
        } else {
            return String.format("$%.2f", value);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return Double.compare(money.value, value) == 0 && type == money.type;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(value) * 31 + type.hashCode();
    }

    // Common denominations
    public static final Money PENNY = new Money(0.01, MoneyType.COIN);
    public static final Money NICKEL = new Money(0.05, MoneyType.COIN);
    public static final Money DIME = new Money(0.10, MoneyType.COIN);
    public static final Money QUARTER = new Money(0.25, MoneyType.COIN);
    public static final Money ONE_DOLLAR = new Money(1.0, MoneyType.NOTE);
    public static final Money FIVE_DOLLAR = new Money(5.0, MoneyType.NOTE);
    public static final Money TEN_DOLLAR = new Money(10.0, MoneyType.NOTE);
    public static final Money TWENTY_DOLLAR = new Money(20.0, MoneyType.NOTE);
}
