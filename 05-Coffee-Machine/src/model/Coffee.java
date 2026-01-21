package model;

import enums.*;

public class Coffee {
    private final CoffeeType type;
    private final Size size;
    private final MilkType milkType;
    private final int sugarLevel; // 0-5
    private final boolean hasWhippedCream;
    private final boolean hasExtraShot;
    
    // Private constructor - only accessible via Builder
    private Coffee(Builder builder) {
        this.type = builder.type;
        this.size = builder.size;
        this.milkType = builder.milkType;
        this.sugarLevel = builder.sugarLevel;
        this.hasWhippedCream = builder.hasWhippedCream;
        this.hasExtraShot = builder.hasExtraShot;
    }
    
    // Getters
    public CoffeeType getType() {
        return type;
    }
    
    public Size getSize() {
        return size;
    }
    
    public MilkType getMilkType() {
        return milkType;
    }
    
    public int getSugarLevel() {
        return sugarLevel;
    }
    
    public boolean hasWhippedCream() {
        return hasWhippedCream;
    }
    
    public boolean hasExtraShot() {
        return hasExtraShot;
    }
    
    // Calculate total ingredients needed
    public int getCoffeeBeansNeeded() {
        int beans = type.getCoffeeBeans();
        beans *= size.getIngredientMultiplier();
        if (hasExtraShot) {
            beans += 18; // Extra shot = 18g more beans
        }
        return beans;
    }
    
    public int getMilkNeeded() {
        if (milkType == MilkType.NONE) {
            return 0;
        }
        int milk = type.getMilk();
        milk *= size.getIngredientMultiplier();
        return milk;
    }
    
    public int getWaterNeeded() {
        int water = type.getWater();
        water *= size.getIngredientMultiplier();
        return water;
    }
    
    public int getSugarNeeded() {
        return sugarLevel * 5; // 5g per sugar level
    }
    
    // Get detailed description
    public String getDescription() {
        StringBuilder desc = new StringBuilder();
        desc.append(size).append(" ").append(type);
        
        if (milkType != MilkType.NONE) {
            desc.append(" with ").append(milkType.getDisplayName());
        }
        
        if (sugarLevel > 0) {
            desc.append(", ").append(sugarLevel).append(" sugar");
        }
        
        if (hasWhippedCream) {
            desc.append(", whipped cream");
        }
        
        if (hasExtraShot) {
            desc.append(", extra shot");
        }
        
        return desc.toString();
    }
    
    @Override
    public String toString() {
        return getDescription();
    }
    
    // ============================================
    // BUILDER PATTERN
    // ============================================
    
    public static class Builder {
        // Required parameter
        private final CoffeeType type;
        
        // Optional parameters with default values
        private Size size = Size.MEDIUM;
        private MilkType milkType = MilkType.NONE;
        private int sugarLevel = 0;
        private boolean hasWhippedCream = false;
        private boolean hasExtraShot = false;
        
        public Builder(CoffeeType type) {
            this.type = type;
        }
        
        public Builder size(Size size) {
            this.size = size;
            return this;
        }
        
        public Builder milkType(MilkType milkType) {
            this.milkType = milkType;
            return this;
        }
        
        public Builder sugarLevel(int sugarLevel) {
            if (sugarLevel < 0 || sugarLevel > 5) {
                throw new IllegalArgumentException("Sugar level must be between 0 and 5");
            }
            this.sugarLevel = sugarLevel;
            return this;
        }
        
        public Builder withWhippedCream() {
            this.hasWhippedCream = true;
            return this;
        }
        
        public Builder withExtraShot() {
            this.hasExtraShot = true;
            return this;
        }
        
        public Coffee build() {
            // Validation: Can't have whipped cream without milk-based coffee
            if (hasWhippedCream && type.getMilk() == 0 && milkType == MilkType.NONE) {
                throw new IllegalStateException("Cannot add whipped cream to coffee without milk");
            }
            
            return new Coffee(this);
        }
    }
}


