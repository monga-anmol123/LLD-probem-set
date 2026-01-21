# Solution: Twitter/Social Media Feed System

## ✅ Complete Implementation

This folder contains a fully working Twitter-like social media feed system demonstrating Observer, Strategy, and Factory design patterns.

---

## 🏗️ Architecture Overview

```
┌─────────────────────────────────────────────────────────────┐
│                         Main.java                            │
│                    (Demo/Entry Point)                        │
└───────────────────────┬─────────────────────────────────────┘
                        │
        ┌───────────────┼───────────────┬───────────────┐
        │               │               │               │
        ▼               ▼               ▼               ▼
┌──────────────┐ ┌─────────────┐ ┌────────────┐ ┌────────────┐
│   Observer   │ │  Strategy   │ │  Factory   │ │  Service   │
│              │ │             │ │            │ │            │
│ User         │ │ Feed        │ │ Post       │ │ Twitter    │
│ Post         │ │ Algorithm   │ │ Factory    │ │ Service    │
│ (Subject)    │ │             │ │            │ │            │
└──────────────┘ └─────────────┘ └────────────┘ └────────────┘
        │               │               │               │
        └───────────────┼───────────────┼───────────────┘
                        │               │
                        ▼               ▼
                ┌─────────────────────────┐
                │         Model           │
                │                         │
                │  User, Post, Comment    │
                │  Notification           │
                │  RegularPost, Retweet   │
                │  QuoteTweet             │
                └─────────────────────────┘
```

---

## 📦 Package Structure

```
src/
├── enums/                          # Type-safe enumerations
│   ├── PostType.java              # REGULAR, RETWEET, QUOTE_TWEET
│   ├── NotificationType.java     # FOLLOW, LIKE, COMMENT, RETWEET
│   └── FeedAlgorithmType.java    # CHRONOLOGICAL, POPULAR, TRENDING
│
├── observer/                       # Observer Pattern
│   ├── Observer.java              # Interface for observers
│   └── Subject.java               # Interface for subjects
│
├── model/                          # Domain entities
│   ├── User.java                  # User (implements Observer)
│   ├── Post.java                  # Abstract post (implements Subject)
│   ├── RegularPost.java           # Normal tweet
│   ├── Retweet.java               # Share another's post
│   ├── QuoteTweet.java            # Retweet with comment
│   ├── Comment.java               # Comment on post
│   └── Notification.java          # User notification
│
├── strategy/                       # Strategy Pattern
│   ├── FeedAlgorithm.java         # Interface
│   ├── ChronologicalFeed.java     # Latest first
│   ├── PopularFeed.java           # Most engaged first
│   └── TrendingFeed.java          # High velocity first
│
├── factory/                        # Factory Pattern
│   └── PostFactory.java           # Creates different post types
│
├── service/                        # Business logic
│   ├── TwitterService.java        # Main service (Singleton)
│   └── FeedService.java           # Feed generation
│
└── Main.java                       # Demo with 6 scenarios
```

---

## 🎨 Design Patterns Explained

### 1. **Observer Pattern** (User observes Posts)

**Purpose:** Notify users when actions occur on their posts

**Implementation:**
```java
// Observer interface
public interface Observer {
    void update(Notification notification);
}

// User implements Observer
public class User implements Observer {
    @Override
    public void update(Notification notification) {
        notifications.add(notification);
    }
}

// Post implements Subject
public abstract class Post implements Subject {
    protected final List<Observer> observers;
    
    public void like(User user) {
        likes.add(user);
        Notification notification = new Notification(LIKE, author, user, this);
        notifyObservers(notification);
    }
}
```

**Benefits:**
- ✅ Loose coupling between posts and users
- ✅ Easy to add new notification types
- ✅ Users automatically notified of relevant actions
- ✅ Real-time notification system

**Real-world Usage:**
- User gets notified when someone likes their post
- User gets notified when someone comments
- User gets notified when someone follows them
- User gets notified when someone retweets

---

### 2. **Strategy Pattern** (Feed Algorithms)

**Purpose:** Allow different feed sorting algorithms to be used interchangeably

**Implementation:**
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

public class PopularFeed implements FeedAlgorithm {
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()))
            .sorted(Comparator.comparing(Post::getEngagementScore).reversed())
            .collect(Collectors.toList());
    }
}
```

**Benefits:**
- ✅ Easy to switch between algorithms at runtime
- ✅ Easy to add new algorithms (e.g., ML-based ranking)
- ✅ Each algorithm is isolated and testable
- ✅ FeedService doesn't need to know algorithm details

**Real-world Usage:**
- User preference: "Show me latest" vs "Show me popular"
- A/B testing different algorithms
- Personalized feeds based on user behavior

---

### 3. **Factory Pattern** (Post Creation)

**Purpose:** Centralize creation of different post types

**Implementation:**
```java
public class PostFactory {
    public static Post createPost(PostType type, User author, 
                                  String content, Post originalPost) {
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

**Benefits:**
- ✅ Single place to manage post creation
- ✅ Easy to add new post types (polls, images, videos)
- ✅ Validation logic centralized
- ✅ Client code doesn't need to know concrete classes

**Usage in Code:**
```java
Post post = PostFactory.createRegularPost(user, "Hello World!");
Post retweet = PostFactory.createRetweet(user, originalPost);
Post quote = PostFactory.createQuoteTweet(user, "I agree!", originalPost);
```

---

## 🔑 Key Design Decisions

### 1. **User as Observer, Post as Subject**

**Decision:** Users observe posts they're interested in

**Why:**
- Natural mapping: users want to know about their posts
- Decouples notification logic from post logic
- Easy to extend with more notification types

**Alternative Considered:**
- Central notification service (rejected - tight coupling)
- Push notifications via external service (future enhancement)

---

### 2. **Abstract Post Base Class**

**Decision:** Post is abstract with concrete subclasses

**Why:**
- Common behavior (likes, comments) in base class
- Type-specific behavior in subclasses
- Easy to add new post types

**Hierarchy:**
```
Post (abstract)
├── RegularPost
├── Retweet
└── QuoteTweet
```

---

### 3. **Engagement Score Calculation**

**Decision:** Weighted scoring for different engagement types

```java
public int getEngagementScore() {
    return (likes.size() * 1) + 
           (comments.size() * 3) + 
           (retweetCount * 5);
}
```

**Why:**
- Comments are more valuable than likes (more effort)
- Retweets are most valuable (amplification)
- Simple, explainable algorithm

**Alternative Considered:**
- Time decay (older posts score lower) - added in TrendingFeed
- User reputation weighting - future enhancement

---

### 4. **Singleton TwitterService**

**Decision:** Single instance of TwitterService

**Why:**
- Centralized user and post management
- Consistent state across application
- Easy to add caching layer

**Implementation:**
```java
public class TwitterService {
    private static TwitterService instance;
    
    public static synchronized TwitterService getInstance() {
        if (instance == null) {
            instance = new TwitterService();
        }
        return instance;
    }
}
```

---

### 5. **Immutable IDs**

**Decision:** Generate UUIDs for posts, comments, notifications

**Why:**
- Unique across distributed systems
- No collision risk
- Can be generated client-side

```java
this.postId = UUID.randomUUID().toString().substring(0, 8);
```

---

## 🧩 Class Responsibilities

### **User**
- ✅ Follow/unfollow other users
- ✅ Create posts (via service)
- ✅ Like and comment on posts
- ✅ Receive notifications (Observer)
- ❌ Does NOT generate own feed (FeedService does this)

### **Post (Abstract)**
- ✅ Store engagement (likes, comments, retweets)
- ✅ Notify observers of changes (Subject)
- ✅ Calculate engagement score
- ❌ Does NOT know about feed algorithms

### **RegularPost / Retweet / QuoteTweet**
- ✅ Implement type-specific display logic
- ✅ Handle retweet counting
- ✅ Notify original author on retweet

### **FeedAlgorithm (Strategy)**
- ✅ Sort posts for personalized feed
- ✅ Filter posts from followed users
- ❌ Does NOT store state

### **TwitterService**
- ✅ Manage users and posts
- ✅ Coordinate actions (like, comment, follow)
- ✅ Search functionality
- ❌ Does NOT generate feeds (FeedService does this)

### **FeedService**
- ✅ Generate personalized feeds
- ✅ Switch between algorithms
- ✅ Pagination support
- ❌ Does NOT store posts (TwitterService does this)

---

## 🎯 Design Principles Applied

### **1. Single Responsibility Principle (SRP)**
- User: User data and relationships
- Post: Post data and engagement
- FeedService: Feed generation
- TwitterService: System coordination

### **2. Open/Closed Principle (OCP)**
- Open for extension: Add new post types, feed algorithms
- Closed for modification: Existing code doesn't change

### **3. Liskov Substitution Principle (LSP)**
- Any Post subclass can be used wherever Post is expected
- Any FeedAlgorithm can be used in FeedService

### **4. Interface Segregation Principle (ISP)**
- Observer: Minimal interface (update, getObserverId)
- Subject: Minimal interface (add/remove/notify)
- FeedAlgorithm: Single method (generateFeed)

### **5. Dependency Inversion Principle (DIP)**
- FeedService depends on FeedAlgorithm interface
- Post depends on Observer interface
- High-level modules don't depend on low-level details

---

## 🚀 Extensibility Examples

### **Adding a New Post Type (Poll)**

```java
// 1. Add to enum
public enum PostType {
    REGULAR, RETWEET, QUOTE_TWEET, POLL
}

// 2. Create Poll class
public class Poll extends Post {
    private final String question;
    private final List<String> options;
    private final Map<String, Set<User>> votes;
    
    @Override
    public String getDisplayContent() {
        return "📊 Poll: " + question;
    }
}

// 3. Add to factory
case POLL:
    return new Poll(author, question, options);
```

**No changes needed in:**
- FeedService
- TwitterService (minimal changes)
- User
- Other post types

---

### **Adding a New Feed Algorithm (ML-Based)**

```java
public class MLFeed implements FeedAlgorithm {
    private final MLModel model;
    
    @Override
    public List<Post> generateFeed(User user, List<Post> allPosts) {
        return allPosts.stream()
            .filter(post -> user.isFollowing(post.getAuthor()))
            .sorted((p1, p2) -> Double.compare(
                model.predict(user, p2),
                model.predict(user, p1)
            ))
            .collect(Collectors.toList());
    }
}

// Add to enum
public enum FeedAlgorithmType {
    CHRONOLOGICAL, POPULAR, TRENDING, ML_BASED
}
```

---

### **Adding Hashtag Support**

```java
public class RegularPost extends Post {
    private final Set<String> hashtags;
    
    public RegularPost(User author, String content) {
        super(author);
        this.content = content;
        this.hashtags = extractHashtags(content);
    }
    
    private Set<String> extractHashtags(String content) {
        Pattern pattern = Pattern.compile("#\\w+");
        Matcher matcher = pattern.matcher(content);
        Set<String> tags = new HashSet<>();
        while (matcher.find()) {
            tags.add(matcher.group().toLowerCase());
        }
        return tags;
    }
}

// Add to TwitterService
public List<Post> searchByHashtag(String hashtag) {
    return posts.values().stream()
        .filter(p -> p.getHashtags().contains(hashtag.toLowerCase()))
        .collect(Collectors.toList());
}
```

---

## 🧪 Test Scenarios Covered

### **Scenario 1: User Registration and Following** ✅
- 5 users registered
- Complex follow relationships
- Follower/following counts displayed

### **Scenario 2: Post Creation and Engagement** ✅
- Multiple posts created
- Likes added (3 users liked Alice's post)
- Comments added (3 comments total)
- Engagement metrics displayed

### **Scenario 3: Chronological Feed** ✅
- Feed shows posts from followed users
- Posts sorted by timestamp (newest first)
- Comments displayed under posts

### **Scenario 4: Popular Feed** ✅
- Feed shows posts sorted by engagement score
- Most engaged posts appear first
- Strategy pattern in action

### **Scenario 5: Notifications** ✅
- Users receive follow notifications
- Users receive like notifications
- Users receive comment notifications
- Unread vs read notifications
- Mark all as read functionality

### **Scenario 6: Retweets and Quote Tweets** ✅
- Regular retweet (share without comment)
- Quote tweet (share with comment)
- Retweet count incremented
- Original author notified
- Factory pattern in action

---

## 🎮 Sample Output

```
================================================================================
  TWITTER / SOCIAL MEDIA FEED SYSTEM DEMO
================================================================================

--------------------------------------------------------------------------------
SCENARIO 1: User Registration and Following
--------------------------------------------------------------------------------

👥 Registering Users...
✅ Registered 5 users

🔗 Creating Follow Relationships...
✓ Alice follows Bob and Charlie
✓ Bob follows Alice, Charlie, and David
✓ Charlie follows everyone

📊 User Statistics:
@alice (Alice Johnson) | Followers: 3 | Following: 2 | Joined: Jan 2026
@bob (Bob Smith) | Followers: 3 | Following: 3 | Joined: Jan 2026
...

--------------------------------------------------------------------------------
SCENARIO 5: Notifications System
--------------------------------------------------------------------------------

📬 Notifications for @alice:
Total: 7 notifications

UNREAD (7):
  ● bob started following you (Jan 06, 16:45)
  ● charlie started following you (Jan 06, 16:45)
  ● bob liked your post (Jan 06, 16:45)
  ● emma commented on your post (Jan 06, 16:45)
...
```

---

## ⚖️ Trade-offs and Alternatives

### **1. Observer Pattern vs Event Bus**

**Current:** Direct observer pattern
```java
post.addObserver(user);
post.notifyObservers(notification);
```

**Alternative:** Event bus (e.g., Guava EventBus)
```java
eventBus.post(new LikeEvent(post, user));
```

**Trade-off:**
- ✅ Current: Simple, no external dependencies
- ✅ Alternative: More decoupled, easier testing
- 💡 Current approach is sufficient for this problem

---

### **2. In-Memory Storage vs Database**

**Current:** HashMaps for users and posts
```java
private final Map<String, User> users;
private final Map<String, Post> posts;
```

**Alternative:** Database with JPA/Hibernate
```java
@Entity
public class User { ... }
```

**Trade-off:**
- ✅ Current: Fast, simple, good for demo
- ✅ Alternative: Persistent, scalable
- 💡 For production, would use database

---

### **3. Synchronous Notifications vs Async**

**Current:** Immediate notification
```java
public void like(User user) {
    likes.add(user);
    notifyObservers(notification);  // Synchronous
}
```

**Alternative:** Async with queue
```java
public void like(User user) {
    likes.add(user);
    notificationQueue.enqueue(notification);  // Async
}
```

**Trade-off:**
- ✅ Current: Simple, consistent
- ✅ Alternative: Better performance, eventual consistency
- 💡 For high-scale systems, use async

---

## 🏆 Strengths of This Solution

1. **Three Design Patterns Correctly Implemented**
   - Observer: Notification system
   - Strategy: Feed algorithms
   - Factory: Post creation

2. **Clean Separation of Concerns**
   - Model: Domain entities
   - Service: Business logic
   - Strategy: Algorithms
   - Factory: Object creation

3. **Extensible Design**
   - Add new post types easily
   - Add new feed algorithms easily
   - Add new notification types easily

4. **Rich Feature Set**
   - Follow/unfollow
   - Posts, retweets, quote tweets
   - Likes and comments
   - Multiple feed algorithms
   - Notification system
   - Search functionality

5. **Production-Ready Code**
   - Proper validation
   - Error handling
   - JavaDoc comments
   - Consistent naming

6. **Comprehensive Demo**
   - 6 detailed scenarios
   - Clear output with emojis
   - All features demonstrated

---

## 📈 Complexity Analysis

### **Time Complexity**

| Operation | Complexity | Explanation |
|-----------|-----------|-------------|
| Follow User | O(1) | HashSet add |
| Create Post | O(1) | HashMap put |
| Like Post | O(1) | HashSet add |
| Comment | O(1) | List add |
| Generate Feed (Chronological) | O(n log n) | Filter + sort |
| Generate Feed (Popular) | O(n log n) | Filter + sort |
| Search Posts | O(n) | Linear scan |
| Notify Observers | O(m) | m = number of observers |

### **Space Complexity**

| Component | Complexity | Explanation |
|-----------|-----------|-------------|
| Users | O(u) | u = number of users |
| Posts | O(p) | p = number of posts |
| Followers/Following | O(u²) | Worst case: everyone follows everyone |
| Notifications | O(u * n) | n = avg notifications per user |
| Total | O(u² + p) | Dominated by relationships |

---

## 🎓 Interview Discussion Points

### **What would you discuss in an interview?**

1. **Design Patterns**
   - Why Observer for notifications?
   - Why Strategy for feeds?
   - Why Factory for posts?
   - Could we use other patterns?

2. **Scalability**
   - How to handle millions of users?
   - How to handle thousands of posts per second?
   - How to optimize feed generation?
   - Caching strategies?

3. **Consistency**
   - Strong consistency for user actions
   - Eventual consistency for feeds
   - CAP theorem trade-offs

4. **Performance**
   - Feed generation is O(n log n) - acceptable?
   - Could we pre-compute feeds?
   - Redis for caching?
   - CDN for media?

5. **Features**
   - How to add direct messages?
   - How to add stories (24h expiry)?
   - How to add live video?
   - How to handle spam/abuse?

---

## ✅ Checklist

- [x] Compiles without errors
- [x] Runs successfully
- [x] Uses Observer pattern correctly
- [x] Uses Strategy pattern correctly
- [x] Uses Factory pattern correctly
- [x] Handles follow/unfollow
- [x] Supports multiple post types
- [x] Supports likes and comments
- [x] Generates feeds with different algorithms
- [x] Sends notifications
- [x] Displays clear output
- [x] Well-documented code
- [x] Follows naming conventions
- [x] Proper error handling
- [x] Comprehensive demo (6 scenarios)

---

## 🎯 Learning Outcomes

After studying this solution, you should understand:

1. ✅ How to implement Observer pattern for event-driven systems
2. ✅ How to implement Strategy pattern for interchangeable algorithms
3. ✅ How to implement Factory pattern for object creation
4. ✅ How to design a social media feed system
5. ✅ How to handle user relationships (followers/following)
6. ✅ How to calculate engagement metrics
7. ✅ How to generate personalized feeds
8. ✅ How to implement a notification system

---

**This solution is interview-ready and demonstrates advanced design patterns!** 🚀

