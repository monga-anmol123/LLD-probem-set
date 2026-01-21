package model;

import enums.NotificationType;
import observer.Observer;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Represents a user in the social media system.
 */
public class User implements Observer {
    private final String userId;
    private final String username;
    private String name;
    private String bio;
    private final LocalDateTime joinDate;
    private final Set<User> followers;
    private final Set<User> following;
    private final List<Post> posts;
    private final List<Notification> notifications;
    
    public User(String userId, String username, String name) {
        this.userId = userId;
        this.username = username;
        this.name = name;
        this.bio = "";
        this.joinDate = LocalDateTime.now();
        this.followers = new HashSet<>();
        this.following = new HashSet<>();
        this.posts = new ArrayList<>();
        this.notifications = new ArrayList<>();
    }
    
    /**
     * Follow another user.
     */
    public void follow(User user) {
        if (this.equals(user)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        
        if (following.add(user)) {
            user.addFollower(this);
            
            // Notify the followed user
            Notification notification = new Notification(NotificationType.FOLLOW, user, this, null);
            user.update(notification);
        }
    }
    
    /**
     * Unfollow a user.
     */
    public void unfollow(User user) {
        if (following.remove(user)) {
            user.removeFollower(this);
        }
    }
    
    /**
     * Add a follower (called by follow method).
     */
    private void addFollower(User user) {
        followers.add(user);
    }
    
    /**
     * Remove a follower (called by unfollow method).
     */
    private void removeFollower(User user) {
        followers.remove(user);
    }
    
    /**
     * Check if this user is following another user.
     */
    public boolean isFollowing(User user) {
        return following.contains(user);
    }
    
    /**
     * Add a post to this user's posts.
     */
    public void addPost(Post post) {
        posts.add(post);
    }
    
    /**
     * Get unread notifications.
     */
    public List<Notification> getUnreadNotifications() {
        return notifications.stream()
            .filter(n -> !n.isRead())
            .collect(Collectors.toList());
    }
    
    /**
     * Get all notifications.
     */
    public List<Notification> getAllNotifications() {
        return new ArrayList<>(notifications);
    }
    
    /**
     * Mark all notifications as read.
     */
    public void markAllNotificationsAsRead() {
        for (Notification notification : notifications) {
            notification.markAsRead();
        }
    }
    
    // Observer pattern implementation
    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }
    
    @Override
    public String getObserverId() {
        return userId;
    }
    
    // Getters and setters
    public String getUserId() {
        return userId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBio() {
        return bio;
    }
    
    public void setBio(String bio) {
        this.bio = bio;
    }
    
    public LocalDateTime getJoinDate() {
        return joinDate;
    }
    
    public Set<User> getFollowers() {
        return new HashSet<>(followers);
    }
    
    public Set<User> getFollowing() {
        return new HashSet<>(following);
    }
    
    public List<Post> getPosts() {
        return new ArrayList<>(posts);
    }
    
    public int getFollowerCount() {
        return followers.size();
    }
    
    public int getFollowingCount() {
        return following.size();
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof User)) return false;
        User other = (User) obj;
        return userId.equals(other.userId);
    }
    
    @Override
    public int hashCode() {
        return userId.hashCode();
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM yyyy");
        return "@" + username + " (" + name + ") | Followers: " + followers.size() + 
               " | Following: " + following.size() + " | Joined: " + joinDate.format(formatter);
    }
}

