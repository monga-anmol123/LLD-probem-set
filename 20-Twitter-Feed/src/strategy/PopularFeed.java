package strategy;

import model.Post;
import model.User;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Popular feed algorithm - shows most engaged posts first.
 */
public class PopularFeed implements FeedAlgorithm {
    
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()) || post.getAuthor().equals(user))
            .sorted(Comparator.comparing(Post::getEngagementScore).reversed())
            .collect(Collectors.toList());
    }
    
    @Override
    public String getAlgorithmName() {
        return "Popular (Most Engaged)";
    }
}

