package model;

import enums.URLStatus;
import java.time.LocalDateTime;

public class URL {
    private final String shortCode;
    private final String longURL;
    private final LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private final String userId;
    private int clicks;
    private URLStatus status;
    
    public URL(String shortCode, String longURL, String userId) {
        this(shortCode, longURL, userId, null);
    }
    
    public URL(String shortCode, String longURL, String userId, LocalDateTime expiresAt) {
        this.shortCode = shortCode;
        this.longURL = longURL;
        this.userId = userId;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
        this.clicks = 0;
        this.status = URLStatus.ACTIVE;
    }
    
    public String getShortCode() {
        return shortCode;
    }
    
    public String getLongURL() {
        return longURL;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public int getClicks() {
        return clicks;
    }
    
    public synchronized void incrementClicks() {
        this.clicks++;
    }
    
    public URLStatus getStatus() {
        return status;
    }
    
    public void setStatus(URLStatus status) {
        this.status = status;
    }
    
    public boolean isExpired() {
        if (expiresAt == null) {
            return false;
        }
        return LocalDateTime.now().isAfter(expiresAt);
    }
    
    public boolean isActive() {
        return status == URLStatus.ACTIVE && !isExpired();
    }
    
    @Override
    public String toString() {
        return String.format("ShortURL[%s] -> %s (clicks: %d, status: %s)", 
            shortCode, longURL, clicks, status);
    }
}
