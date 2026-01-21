package service;

import enums.FeedAlgorithmType;
import model.Comment;
import model.Post;
import model.User;
import strategy.ChronologicalFeed;
import strategy.FeedAlgorithm;
import strategy.PopularFeed;
import strategy.TrendingFeed;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Service for generating personalized feeds using different algorithms.
 */
public class FeedService {
    private FeedAlgorithm currentAlgorithm;
    
    public FeedService(FeedAlgorithmType algorithmType) {
        setAlgorithm(algorithmType);
    }
    
    /**
     * Set the feed algorithm.
     */
    public void setAlgorithm(FeedAlgorithmType algorithmType) {
        switch (algorithmType) {
            case CHRONOLOGICAL:
                this.currentAlgorithm = new ChronologicalFeed();
                break;
            case POPULAR:
                this.currentAlgorithm = new PopularFeed();
                break;
            case TRENDING:
                this.currentAlgorithm = new TrendingFeed();
                break;
            default:
                this.currentAlgorithm = new ChronologicalFeed();
        }
    }
    
    /**
     * Generate feed for a user.
     */
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return currentAlgorithm.generateFeed(user, allPosts);
    }
    
    /**
     * Generate feed with pagination.
     */
    public List<Post> generateFeed(User user, List<Post> allPosts, int page, int pageSize) {
        List<Post> fullFeed = currentAlgorithm.generateFeed(user, allPosts);
        
        int start = page * pageSize;
        int end = Math.min(start + pageSize, fullFeed.size());
        
        if (start >= fullFeed.size()) {
            return Collections.emptyList();
        }
        
        return fullFeed.subList(start, end);
    }
    
    /**
     * Display feed for a user.
     */
    public void displayFeed(User user, List<Post> allPosts, int limit) {
        List<Post> feed = generateFeed(user, allPosts);
        
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("FEED FOR @" + user.getUsername() + " | Algorithm: " + currentAlgorithm.getAlgorithmName());
        System.out.println(repeatString("=", 80));
        
        if (feed.isEmpty()) {
            System.out.println("No posts in feed. Follow users to see their posts!");
        } else {
            int count = 0;
            for (Post post : feed) {
                if (count >= limit) break;
                System.out.println("\n" + post);
                
                // Show comments if any
                if (!post.getComments().isEmpty()) {
                    for (Comment comment : post.getComments()) {
                        System.out.println(comment);
                    }
                }
                count++;
            }
            
            if (feed.size() > limit) {
                System.out.println("\n... and " + (feed.size() - limit) + " more posts");
            }
        }
        
        System.out.println(repeatString("=", 80));
    }
    
    /**
     * Get current algorithm name.
     */
    public String getCurrentAlgorithmName() {
        return currentAlgorithm.getAlgorithmName();
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

