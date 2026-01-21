package strategy;

public class CustomAliasStrategy implements ShorteningStrategy {
    private final String customAlias;
    
    public CustomAliasStrategy(String customAlias) {
        this.customAlias = customAlias;
    }
    
    @Override
    public String generateShortCode(String longURL) {
        return customAlias;
    }
    
    @Override
    public String getStrategyName() {
        return "CustomAlias";
    }
}
