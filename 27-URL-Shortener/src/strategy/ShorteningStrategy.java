package strategy;

public interface ShorteningStrategy {
    String generateShortCode(String longURL);
    String getStrategyName();
}
