package model;

import java.time.LocalDateTime;

public class Analytics {
    private final String shortCode;
    private final LocalDateTime timestamp;
    private final String referrer;
    private final String userAgent;
    private final String ipAddress;
    
    public Analytics(String shortCode, String referrer, String userAgent, String ipAddress) {
        this.shortCode = shortCode;
        this.timestamp = LocalDateTime.now();
        this.referrer = referrer;
        this.userAgent = userAgent;
        this.ipAddress = ipAddress;
    }
    
    public String getShortCode() {
        return shortCode;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public String getReferrer() {
        return referrer;
    }
    
    public String getUserAgent() {
        return userAgent;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    @Override
    public String toString() {
        return String.format("[%s] %s from %s (%s)", 
            timestamp, shortCode, referrer != null ? referrer : "direct", ipAddress);
    }
}
