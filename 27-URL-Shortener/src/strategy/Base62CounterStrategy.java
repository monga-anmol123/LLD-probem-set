package strategy;

public class Base62CounterStrategy implements ShorteningStrategy {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private long counter;
    
    public Base62CounterStrategy() {
        this.counter = 1000000; // Start from 1M for 6-char codes
    }
    
    public Base62CounterStrategy(long startCounter) {
        this.counter = startCounter;
    }
    
    @Override
    public synchronized String generateShortCode(String longURL) {
        long num = counter++;
        return toBase62(num);
    }
    
    private String toBase62(long num) {
        if (num == 0) {
            return String.valueOf(BASE62.charAt(0));
        }
        
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(BASE62.charAt((int)(num % 62)));
            num /= 62;
        }
        return sb.reverse().toString();
    }
    
    @Override
    public String getStrategyName() {
        return "Base62Counter";
    }
}
