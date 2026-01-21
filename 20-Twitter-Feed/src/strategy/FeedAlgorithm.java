package strategy;

import model.Post;
import model.User;

import java.util.List;

/**
 * Strategy interface for different feed generation algorithms.
 */
public interface FeedAlgorithm {
    /**
     * Generate a personalized feed for the user.
     * 
     * @param user User requesting the feed
     * @param allPosts All available posts in the system
     * @return Sorted list of posts for the user's feed
     */
    List<Post> generateFeed(User user, List<Post> allPosts);
    
    /**
     * Get the name of this algorithm.
     * 
     * @return Algorithm name
     */
    String getAlgorithmName();
}

