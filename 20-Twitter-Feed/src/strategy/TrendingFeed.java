package strategy;

import model.Post;
import model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Trending feed algorithm - shows recent posts with high engagement velocity.
 */
public class TrendingFeed implements FeedAlgorithm {
    
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()) || post.getAuthor().equals(user))
            .sorted(Comparator.comparing(Post::getEngagementVelocity).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public String getAlgorithmName() {
        return "Trending (High Velocity)";
    }
}

