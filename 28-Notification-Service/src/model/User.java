package model;

import enums.NotificationChannel;
import java.util.*;

/**
 * Represents a user who can receive notifications
 */
public class User {
    private String userId;
    private String name;
    private String email;
    private String phone;
    private String deviceToken; // For push notifications
    private Map<NotificationChannel, Boolean> channelPreferences;

    public User(String userId, String name, String email, String phone) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.deviceToken = null;
        this.channelPreferences = new HashMap<>();
        
        // Enable all channels by default
        for (NotificationChannel channel : NotificationChannel.values()) {
            channelPreferences.put(channel, true);
        }
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public void setChannelPreference(NotificationChannel channel, boolean enabled) {
        channelPreferences.put(channel, enabled);
    }

    public boolean isChannelEnabled(NotificationChannel channel) {
        return channelPreferences.getOrDefault(channel, false);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    @Override
    public String toString() {
        return String.format("User %s - %s (%s)", userId, name, email);
    }
}
