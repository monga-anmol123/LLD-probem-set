package strategy;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashBasedStrategy implements ShorteningStrategy {
    private static final String BASE62 = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int codeLength;
    
    public HashBasedStrategy() {
        this.codeLength = 6;
    }
    
    public HashBasedStrategy(int codeLength) {
        this.codeLength = codeLength;
    }
    
    @Override
    public String generateShortCode(String longURL) {
        try {
            // Add timestamp to reduce collisions
            String input = longURL + System.nanoTime();
            
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hash = md.digest(input.getBytes());
            
            // Convert hash to Base62
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < codeLength && i < hash.length; i++) {
                int index = Math.abs(hash[i]) % 62;
                sb.append(BASE62.charAt(index));
            }
            
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Fallback to simple hash
            return String.valueOf(Math.abs(longURL.hashCode())).substring(0, codeLength);
        }
    }
    
    @Override
    public String getStrategyName() {
        return "HashBased";
    }
}
