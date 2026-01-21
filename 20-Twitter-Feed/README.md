# Problem 20: Twitter/Social Media Feed

## 🎯 Difficulty: Hard ⭐⭐⭐⭐

## 📝 Problem Statement

Design a Twitter-like social media feed system that supports users, posts (tweets), following/followers, likes, comments, retweets, and customizable feed generation algorithms.

---

## 🔍 Functional Requirements (FR)

### FR1: User Management
- Users can register with unique username
- Users have profile (name, bio, join date)
- Users can follow/unfollow other users
- Track followers and following lists

### FR2: Post Management
- Users can create posts (tweets) with text content
- Posts have timestamp, author, and unique ID
- Support different post types:
  - Regular post
  - Retweet (share another user's post)
  - Quote tweet (retweet with comment)
- Posts can be deleted by author

### FR3: Engagement
- Users can like/unlike posts
- Users can comment on posts
- Track engagement metrics (likes count, comments count, retweets count)
- Users can view who liked a post

### FR4: Feed Generation
- Generate personalized feed for each user
- Feed contains posts from followed users
- Support different feed algorithms:
  - **Chronological:** Latest posts first
  - **Popular:** Posts with most engagement
  - **Trending:** Recent posts with high engagement velocity
- Paginated feed (load more functionality)

### FR5: Notifications
- Notify users when:
  - Someone follows them
  - Someone likes their post
  - Someone comments on their post
  - Someone retweets their post
- Users can view notification history

### FR6: Search & Discovery
- Search posts by keyword
- Search users by username
- View trending hashtags
- Discover suggested users to follow

---

## 🚫 Non-Functional Requirements (NFR)

### NFR1: Scalability
- Support millions of users
- Handle thousands of posts per second
- Efficient feed generation (avoid N+1 queries)

### NFR2: Performance
- Feed generation should be <100ms
- Post creation should be <50ms
- Follow/unfollow should be instant

### NFR3: Extensibility
- Easy to add new post types (polls, images, videos)
- Easy to add new feed algorithms
- Easy to add new notification types

### NFR4: Maintainability
- Clean separation of concerns
- Well-documented code
- Testable components

### NFR5: Consistency
- Eventual consistency for feeds (acceptable delay)
- Strong consistency for user actions (follow, post)

---

## 🎨 Design Patterns to Use

### 1. **Observer Pattern**
- **Where:** Notification system
- **Why:** Users observe posts/users for notifications

### 2. **Strategy Pattern**
- **Where:** Feed generation algorithms
- **Why:** Different feed sorting strategies

### 3. **Factory Pattern**
- **Where:** Post creation (Regular, Retweet, Quote)
- **Why:** Centralize post object creation

---

## 📋 Core Entities

### 1. **User**
- Attributes: `userId`, `username`, `name`, `bio`, `joinDate`, `followers`, `following`, `posts`
- Methods: `follow(user)`, `unfollow(user)`, `createPost()`, `like()`, `comment()`

### 2. **Post** (Abstract)
- Attributes: `postId`, `author`, `content`, `timestamp`, `likes`, `comments`, `retweets`
- Methods: `like()`, `unlike()`, `addComment()`, `getEngagementScore()`
- Subclasses: `RegularPost`, `Retweet`, `QuoteTweet`

### 3. **Comment**
- Attributes: `commentId`, `author`, `content`, `timestamp`, `postId`

### 4. **Notification**
- Attributes: `notificationId`, `recipient`, `type`, `timestamp`, `isRead`, `relatedUser`, `relatedPost`
- Types: FOLLOW, LIKE, COMMENT, RETWEET

### 5. **Feed**
- Attributes: `userId`, `posts`, `algorithm`
- Methods: `generate()`, `refresh()`, `loadMore()`

### 6. **FeedAlgorithm** (Interface)
- Methods: `sortPosts(posts)`
- Implementations: `ChronologicalFeed`, `PopularFeed`, `TrendingFeed`

---

## 🧪 Test Scenarios

### Scenario 1: Basic User Interactions
```
1. Create 5 users: Alice, Bob, Charlie, David, Emma
2. Alice follows Bob, Charlie
3. Bob follows Alice, Charlie, David
4. Charlie follows everyone
5. Display follower/following counts
```

### Scenario 2: Post Creation & Engagement
```
1. Alice creates post: "Hello Twitter!"
2. Bob creates post: "Learning design patterns"
3. Charlie likes Alice's post
4. David comments on Bob's post
5. Emma retweets Alice's post
6. Display engagement metrics
```

### Scenario 3: Feed Generation - Chronological
```
1. Multiple users create posts at different times
2. Alice views her chronological feed
3. Should see posts from Bob and Charlie (users she follows)
4. Posts ordered by timestamp (newest first)
```

### Scenario 4: Feed Generation - Popular
```
1. Multiple posts with different engagement levels
2. Bob views popular feed
3. Should see posts sorted by engagement score
4. High engagement posts appear first
```

### Scenario 5: Notifications
```
1. Alice creates post
2. Bob likes Alice's post → Alice gets notification
3. Charlie comments on Alice's post → Alice gets notification
4. David follows Alice → Alice gets notification
5. Alice views notifications (unread first)
```

### Scenario 6: Retweets & Quote Tweets
```
1. Alice creates original post
2. Bob retweets Alice's post
3. Charlie quote tweets with comment
4. Display retweet chain
5. Verify engagement counts
```

---

## ⏱️ Time Allocation (90 minutes)

- **10 mins:** Clarify requirements, understand system
- **10 mins:** List entities and relationships
- **10 mins:** Identify design patterns
- **50 mins:** Write code (enums → model → strategy → factory → observer → service → main)
- **10 mins:** Test with scenarios, handle edge cases

---

## 💡 Hints

<details>
<summary>Hint 1: Observer Pattern for Notifications</summary>

```java
public interface Observer {
    void update(Notification notification);
}

public class User implements Observer {
    private List<Notification> notifications = new ArrayList<>();
    
    @Override
    public void update(Notification notification) {
        notifications.add(notification);
        System.out.println("📬 " + username + " received notification: " + notification);
    }
}

public class Post {
    private List<Observer> observers = new ArrayList<>();
    
    public void like(User user) {
        // ... like logic ...
        notifyObservers(new Notification(LIKE, author, user, this));
    }
}
```
</details>

<details>
<summary>Hint 2: Strategy Pattern for Feed Algorithms</summary>

```java
public interface FeedAlgorithm {
    List<Post> generateFeed(User user, List<Post> allPosts);
}

public class ChronologicalFeed implements FeedAlgorithm {
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()))
            .sorted(Comparator.comparing(Post::getTimestamp).reversed())
            .collect(Collectors.toList());
    }
}
```
</details>

<details>
<summary>Hint 3: Factory Pattern for Posts</summary>

```java
public class PostFactory {
    public static Post createPost(PostType type, User author, String content, Post originalPost) {
        switch (type) {
            case REGULAR:
                return new RegularPost(author, content);
            case RETWEET:
                return new Retweet(author, originalPost);
            case QUOTE_TWEET:
                return new QuoteTweet(author, content, originalPost);
        }
    }
}
```
</details>

<details>
<summary>Hint 4: Engagement Score Calculation</summary>

```java
public int getEngagementScore() {
    int likeWeight = 1;
    int commentWeight = 3;
    int retweetWeight = 5;
    
    return (likes.size() * likeWeight) + 
           (comments.size() * commentWeight) + 
           (retweets * retweetWeight);
}
```
</details>

---

## 🚀 Extensions (If Time Permits)

1. **Hashtags**
   - Extract hashtags from posts
   - Track trending hashtags
   - Search by hashtag

2. **Mentions**
   - @username mentions in posts
   - Notify mentioned users

3. **Direct Messages**
   - Private messaging between users
   - Message threads

4. **Verified Users**
   - Blue checkmark for verified accounts
   - Prioritize in search results

5. **Blocking/Muting**
   - Block users (hide all content)
   - Mute users (hide from feed but not profile)

6. **Analytics**
   - Post impressions
   - Profile views
   - Engagement rate

---

## ✅ Success Criteria

Your solution should:
- [ ] Compile without errors
- [ ] Use Observer, Strategy, Factory patterns correctly
- [ ] Handle all test scenarios
- [ ] Support follow/unfollow
- [ ] Support post creation (regular, retweet, quote)
- [ ] Support likes and comments
- [ ] Generate feeds with different algorithms
- [ ] Send notifications for user actions
- [ ] Display clear output with engagement metrics
- [ ] Be extensible (easy to add new post types, algorithms)

---

## 📁 File Structure

```
20-Twitter-Feed/
├── README.md (this file)
├── src/
│   ├── enums/
│   │   ├── PostType.java
│   │   ├── NotificationType.java
│   │   └── FeedAlgorithmType.java
│   ├── model/
│   │   ├── User.java
│   │   ├── Post.java (abstract)
│   │   ├── RegularPost.java
│   │   ├── Retweet.java
│   │   ├── QuoteTweet.java
│   │   ├── Comment.java
│   │   └── Notification.java
│   ├── observer/
│   │   ├── Observer.java (interface)
│   │   └── Subject.java (interface)
│   ├── strategy/
│   │   ├── FeedAlgorithm.java (interface)
│   │   ├── ChronologicalFeed.java
│   │   ├── PopularFeed.java
│   │   └── TrendingFeed.java
│   ├── factory/
│   │   └── PostFactory.java
│   ├── service/
│   │   ├── TwitterService.java
│   │   └── FeedService.java
│   └── Main.java
├── SOLUTION.md
└── COMPILATION-GUIDE.md
```

---

**Good luck! Start coding! 🚀**

