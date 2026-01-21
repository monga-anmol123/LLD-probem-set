package service;

import enums.PostType;
import factory.PostFactory;
import model.Comment;
import model.Post;
import model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Main service for managing Twitter-like functionality.
 * Singleton pattern for centralized management.
 */
public class TwitterService {
    private static TwitterService instance;
    
    private final Map<String, User> users;
    private final Map<String, Post> posts;
    
    private TwitterService() {
        this.users = new HashMap<>();
        this.posts = new HashMap<>();
    }
    
    /**
     * Get the singleton instance.
     */
    public static synchronized TwitterService getInstance() {
        if (instance == null) {
            instance = new TwitterService();
        }
        return instance;
    }
    
    /**
     * Register a new user.
     */
    public User registerUser(String userId, String username, String name) {
        if (users.containsKey(userId)) {
            throw new IllegalArgumentException("User ID already exists: " + userId);
        }
        
        // Check if username is taken
        for (User user : users.values()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                throw new IllegalArgumentException("Username already taken: " + username);
            }
        }
        
        User user = new User(userId, username, name);
        users.put(userId, user);
        return user;
    }
    
    /**
     * Get user by ID.
     */
    public User getUser(String userId) {
        return users.get(userId);
    }
    
    /**
     * Get user by username.
     */
    public User getUserByUsername(String username) {
        return users.values().stream()
            .filter(u -> u.getUsername().equalsIgnoreCase(username))
            .findFirst()
            .orElse(null);
    }
    
    /**
     * Create a post.
     */
    public Post createPost(PostType type, User author, String content, Post originalPost) {
        Post post = PostFactory.createPost(type, author, content, originalPost);
        posts.put(post.getPostId(), post);
        author.addPost(post);
        return post;
    }
    
    /**
     * Get post by ID.
     */
    public Post getPost(String postId) {
        return posts.get(postId);
    }
    
    /**
     * Get all posts.
     */
    public List<Post> getAllPosts() {
        return new ArrayList<>(posts.values());
    }
    
    /**
     * Like a post.
     */
    public void likePost(String postId, User user) {
        Post post = posts.get(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        post.like(user);
    }
    
    /**
     * Unlike a post.
     */
    public void unlikePost(String postId, User user) {
        Post post = posts.get(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        post.unlike(user);
    }
    
    /**
     * Comment on a post.
     */
    public Comment commentOnPost(String postId, User user, String content) {
        Post post = posts.get(postId);
        if (post == null) {
            throw new IllegalArgumentException("Post not found: " + postId);
        }
        
        Comment comment = new Comment(user, content, postId);
        post.addComment(comment);
        return comment;
    }
    
    /**
     * Search posts by keyword.
     */
    public List<Post> searchPosts(String keyword) {
        return posts.values().stream()
            .filter(post -> post.getDisplayContent().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Search users by username.
     */
    public List<User> searchUsers(String keyword) {
        return users.values().stream()
            .filter(user -> user.getUsername().toLowerCase().contains(keyword.toLowerCase()) ||
                           user.getName().toLowerCase().contains(keyword.toLowerCase()))
            .collect(Collectors.toList());
    }
    
    /**
     * Get all users.
     */
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
    
    /**
     * Display system statistics.
     */
    public void displayStatistics() {
        System.out.println("\n" + repeatString("=", 60));
        System.out.println("SYSTEM STATISTICS");
        System.out.println(repeatString("=", 60));
        System.out.println("Total Users: " + users.size());
        System.out.println("Total Posts: " + posts.size());
        
        long totalLikes = posts.values().stream().mapToLong(p -> p.getLikes().size()).sum();
        long totalComments = posts.values().stream().mapToLong(p -> p.getComments().size()).sum();
        long totalRetweets = posts.values().stream().mapToLong(Post::getRetweetCount).sum();
        
        System.out.println("Total Likes: " + totalLikes);
        System.out.println("Total Comments: " + totalComments);
        System.out.println("Total Retweets: " + totalRetweets);
        System.out.println(repeatString("=", 60));
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}

