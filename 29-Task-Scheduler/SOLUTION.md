# Problem 29: Task Scheduler System - Solution

## 📋 Overview

This solution implements a comprehensive **Task Scheduler System** with support for:
- Multiple task types (one-time, recurring, delayed)
- Priority-based execution
- Task dependencies
- Retry mechanisms with different strategies
- Worker pool management
- Event-driven architecture

---

## 🏗️ Architecture

### Design Patterns Used

1. **Strategy Pattern** (Retry Policies)
   - Interface: `RetryPolicy`
   - Implementations: `NoRetryPolicy`, `FixedDelayRetryPolicy`, `ExponentialBackoffRetryPolicy`, `LinearBackoffRetryPolicy`
   - **Why**: Different retry strategies can be swapped without changing task logic

2. **Observer Pattern** (Task Events)
   - Interface: `TaskObserver`
   - Implementation: `TaskEventLogger`
   - **Why**: Decouple task execution from logging/monitoring

3. **Singleton Pattern** (TaskScheduler)
   - Class: `TaskScheduler`
   - **Why**: Single point of control for all task scheduling

4. **Command Pattern** (Task Execution)
   - Base: `Task` (implements `Callable<TaskResult>`)
   - **Why**: Encapsulate task execution logic

5. **Template Method Pattern** (Task Lifecycle)
   - Abstract: `Task.call()` orchestrates execution
   - Concrete: Subclasses implement `execute()`
   - **Why**: Common lifecycle management with custom execution

---

## 📦 Component Breakdown

### 1. Enums

#### `TaskStatus`
```java
SCHEDULED → QUEUED → RUNNING → COMPLETED/FAILED/CANCELLED/TIMEOUT
```
- Tracks task lifecycle
- Terminal states: COMPLETED, FAILED, CANCELLED, TIMEOUT

#### `TaskPriority`
```java
LOW(1) < MEDIUM(2) < HIGH(3) < CRITICAL(4)
```
- Determines execution order
- Higher priority tasks execute first

#### `TaskType`
- ONE_TIME: Execute once
- RECURRING: Execute repeatedly
- DELAYED: Execute after delay
- IMMEDIATE: Execute ASAP

#### `RetryStrategyType`
- NONE: No retries
- FIXED_DELAY: Same delay between retries
- EXPONENTIAL_BACKOFF: Exponentially increasing delay
- LINEAR_BACKOFF: Linearly increasing delay

---

### 2. Model Classes

#### `Task` (Abstract Base Class)
**Responsibilities:**
- Encapsulate task execution logic
- Manage task metadata (ID, name, priority, status)
- Handle dependencies
- Support retries
- Implement `Comparable` for priority queue ordering

**Key Methods:**
- `execute()`: Abstract method for task logic
- `call()`: Template method for execution lifecycle
- `canExecute()`: Check if dependencies are met
- `compareTo()`: Priority-based ordering

**Design Decision:**
- Implements `Callable<TaskResult>` for integration with ExecutorService
- Uses Template Method pattern for consistent lifecycle

#### `SimpleTask`
- One-time execution
- Takes a `Runnable` action
- Simulates work duration

#### `RecurringTask`
- Executes at fixed intervals
- Can be cancelled
- Tracks execution count
- Supports max executions limit

#### `DelayedTask`
- Executes once after specified delay
- Automatically calculates scheduled time

#### `TaskResult`
- Encapsulates execution outcome
- Supports success, failure, and retry states
- Includes execution time and error messages

#### `TaskMetrics`
- Thread-safe metrics tracking using `AtomicInteger` and `AtomicLong`
- Tracks: scheduled, running, completed, failed, cancelled
- Calculates: average execution time, success rate

---

### 3. Strategy Pattern - Retry Policies

#### `RetryPolicy` Interface
```java
long getRetryDelay(int attemptNumber);
String getStrategyName();
```

#### `NoRetryPolicy`
- Returns 0 delay (no retries)
- Used for tasks that should fail immediately

#### `FixedDelayRetryPolicy`
- Same delay for all retries
- Formula: `delay = fixedDelay`
- **Use Case**: Database connection retries

#### `ExponentialBackoffRetryPolicy`
- Exponentially increasing delay
- Formula: `delay = baseDelay * (2 ^ attemptNumber)`
- Capped at `maxDelay`
- **Use Case**: API calls, distributed systems
- **Why**: Prevents overwhelming failing services

#### `LinearBackoffRetryPolicy`
- Linearly increasing delay
- Formula: `delay = baseDelay * attemptNumber`
- Capped at `maxDelay`
- **Use Case**: File system operations

**Trade-offs:**
| Strategy | Pros | Cons | Best For |
|----------|------|------|----------|
| Fixed Delay | Simple, predictable | Can overwhelm failing service | Stable systems |
| Exponential | Backs off quickly, prevents overload | Can wait very long | Distributed systems |
| Linear | Balanced approach | Less aggressive than exponential | General purpose |

---

### 4. Observer Pattern - Task Events

#### `TaskObserver` Interface
```java
void onTaskScheduled(Task task);
void onTaskStarted(Task task);
void onTaskCompleted(Task task, TaskResult result);
void onTaskFailed(Task task, TaskResult result);
void onTaskCancelled(Task task);
```

#### `TaskEventLogger`
- Logs all task lifecycle events
- Includes timestamps
- Supports verbose mode

**Benefits:**
- Decouples logging from task execution
- Easy to add new observers (metrics, alerts, etc.)
- No impact on task performance

---

### 5. Service Layer

#### `TaskScheduler` (Singleton)

**Core Responsibilities:**
1. Task scheduling and queueing
2. Worker pool management
3. Dependency resolution
4. Retry coordination
5. Observer notification
6. Metrics tracking

**Key Data Structures:**

1. **`PriorityBlockingQueue<Task>`**
   - Thread-safe priority queue
   - Tasks ordered by priority then scheduled time
   - Blocks when empty (efficient worker waiting)

2. **`ConcurrentHashMap<String, Task>`**
   - Fast task lookup by ID
   - Thread-safe for concurrent access

3. **`ScheduledExecutorService`**
   - Handles recurring task scheduling
   - Separate from worker pool

**Key Algorithms:**

1. **Task Scheduling:**
```java
scheduleTask(task, retryPolicy):
    1. Store task in allTasks map
    2. Store retry policy
    3. Notify observers
    4. If recurring: schedule with ScheduledExecutorService
    5. Else: add to priority queue
```

2. **Task Processing (Worker Thread):**
```java
processTask():
    1. Poll task from queue (with timeout)
    2. Check if cancelled → skip
    3. Check dependencies → re-queue if not met
    4. Execute task
    5. If success → mark completed
    6. If failure and retries left → apply retry delay, re-queue
    7. If failure and no retries → mark failed
    8. Notify observers
```

3. **Dependency Resolution:**
```java
canExecute(task, completedTasks):
    return completedTasks.containsAll(task.dependencies)
```
- Simple but effective
- Tasks re-queued if dependencies not met
- Avoids deadlocks (no circular dependency check in basic version)

**Thread Safety:**
- `ConcurrentHashMap` for task storage
- `PriorityBlockingQueue` for task queue
- `synchronized` on critical sections in `processTask()`
- `CopyOnWriteArrayList` for observers

#### `WorkerPool`

**Responsibilities:**
- Create and manage worker threads
- Dynamic worker scaling
- Graceful shutdown

**Key Features:**
1. **Dynamic Scaling:**
   - Can increase/decrease workers at runtime
   - Useful for load adaptation

2. **Worker Thread Lifecycle:**
   ```
   START → RUNNING (process tasks) → STOP → INTERRUPTED → JOINED
   ```

3. **Graceful Shutdown:**
   - Interrupt all workers
   - Wait for completion (with timeout)
   - Clean up resources

---

## 🎯 Key Algorithms

### 1. Priority Queue Ordering

Tasks are ordered by:
1. **Priority** (higher first)
2. **Scheduled Time** (earlier first)

```java
compareTo(other):
    priorityDiff = other.priority.level - this.priority.level
    if priorityDiff != 0: return priorityDiff
    return this.scheduledTime - other.scheduledTime
```

**Time Complexity:**
- Insert: O(log n)
- Poll: O(log n)
- Peek: O(1)

### 2. Dependency Resolution

**Approach:** Simple containment check
```java
canExecute = completedTasks.containsAll(task.dependencies)
```

**Time Complexity:** O(d) where d = number of dependencies

**Limitation:** No circular dependency detection in basic version

**Enhancement (for production):**
- Use topological sort
- Detect cycles using DFS
- Build dependency graph

### 3. Exponential Backoff

```java
delay = baseDelay * (2 ^ attemptNumber)
delay = min(delay, maxDelay)
```

**Example:** baseDelay=500ms, maxDelay=60s
- Attempt 1: 1s
- Attempt 2: 2s
- Attempt 3: 4s
- Attempt 4: 8s
- Attempt 5: 16s
- Attempt 6+: 60s (capped)

**Why Exponential?**
- Quickly backs off from failing service
- Gives service time to recover
- Industry standard (AWS, Google Cloud)

---

## 💡 Design Decisions & Trade-offs

### 1. PriorityBlockingQueue vs Custom Queue

**Chosen:** `PriorityBlockingQueue`

**Pros:**
- Thread-safe out of the box
- Efficient blocking (no busy waiting)
- Standard Java library

**Cons:**
- Cannot change priority of queued tasks
- No support for task preemption

**Alternative:** Custom priority queue with ReentrantLock
- More control but more complexity

### 2. Dependency Resolution Approach

**Chosen:** Re-queue if dependencies not met

**Pros:**
- Simple implementation
- No complex graph traversal
- Works well for shallow dependency trees

**Cons:**
- Inefficient for deep dependencies (repeated re-queuing)
- No circular dependency detection
- Can cause starvation if dependency never completes

**Alternative:** Topological sort + dependency graph
- More efficient but more complex
- Better for production systems

### 3. Recurring Task Implementation

**Chosen:** `ScheduledExecutorService` + task queue

**Pros:**
- Leverages Java's built-in scheduling
- Separate from worker pool (doesn't block workers)
- Precise timing

**Cons:**
- Two execution paths (scheduled vs immediate)
- More complex lifecycle management

**Alternative:** Manual timing in worker threads
- Simpler but less precise
- Ties up worker threads

### 4. Retry Strategy

**Chosen:** Strategy pattern with pluggable policies

**Pros:**
- Flexible - easy to add new strategies
- Per-task configuration
- Testable in isolation

**Cons:**
- More classes to maintain
- Slight overhead for strategy lookup

**Alternative:** Hard-coded retry logic
- Simpler but inflexible

### 5. Observer Pattern for Events

**Chosen:** Observer pattern with `CopyOnWriteArrayList`

**Pros:**
- Decoupled logging/monitoring
- Easy to add new observers
- Thread-safe iteration

**Cons:**
- `CopyOnWriteArrayList` has high write cost
- Observer errors don't fail task execution

**Alternative:** Direct logging in task execution
- Simpler but tightly coupled

---

## 🚀 Performance Characteristics

### Time Complexity

| Operation | Complexity | Notes |
|-----------|------------|-------|
| Schedule Task | O(log n) | Priority queue insert |
| Poll Next Task | O(log n) | Priority queue poll |
| Check Dependencies | O(d) | d = number of dependencies |
| Cancel Task | O(1) | HashMap lookup |
| Get Metrics | O(1) | Atomic operations |

### Space Complexity

| Component | Space | Notes |
|-----------|-------|-------|
| Task Queue | O(n) | n = pending tasks |
| All Tasks Map | O(t) | t = total tasks |
| Completed Tasks | O(c) | c = completed tasks |
| Recurring Futures | O(r) | r = recurring tasks |
| **Total** | **O(t)** | Dominated by total tasks |

### Scalability

**Vertical Scaling:**
- Increase worker count
- Limited by CPU cores
- Diminishing returns beyond core count

**Horizontal Scaling (Future Enhancement):**
- Distributed task queue (Redis, RabbitMQ)
- Multiple scheduler instances
- Shared state management

---

## 🧪 Test Scenarios Explained

### Scenario 1: Basic Priority Scheduling
**Tests:** Priority ordering, parallel execution
**Expected:** CRITICAL → HIGH → MEDIUM → LOW
**Result:** ✅ Tasks execute in priority order

### Scenario 2: Recurring Tasks
**Tests:** Repeated execution, automatic scheduling
**Expected:** 3 executions at 2-second intervals
**Result:** ✅ Executes 3 times, then stops

### Scenario 3: Task Dependencies
**Tests:** Dependency resolution, parallel execution of independent tasks
**Expected:** B and C run in parallel, A waits for both
**Result:** ✅ Dependencies resolved correctly

### Scenario 4: Retry with Exponential Backoff
**Tests:** Retry mechanism, exponential delay
**Expected:** Delays of 500ms, 1000ms, 2000ms
**Result:** ✅ Retries with increasing delays

### Scenario 5: Worker Pool Management
**Tests:** Concurrent execution, queue management
**Expected:** Max 3 tasks running concurrently
**Result:** ✅ Respects worker pool size

### Scenario 6: Task Cancellation
**Tests:** Cancelling recurring tasks
**Expected:** Task stops executing after cancellation
**Result:** ✅ No more executions after cancel

### Scenario 7: Complex Workflow
**Tests:** Multi-level dependencies, mixed priorities
**Expected:** Level-by-level execution
**Result:** ✅ Workflow executes correctly

---

## 🔧 Extension Ideas

### 1. Persistent Storage
```java
interface TaskRepository {
    void save(Task task);
    Task load(String taskId);
    List<Task> loadPending();
}
```
- Survive scheduler restarts
- Use database or file system

### 2. Distributed Scheduling
- Multiple scheduler instances
- Shared task queue (Redis)
- Leader election (Zookeeper)

### 3. Task Chaining
```java
scheduler.schedule(task1)
    .then(task2)
    .then(task3)
    .onFailure(errorHandler);
```
- Fluent API for workflows
- Automatic dependency management

### 4. Dynamic Priority
```java
task.adjustPriority(newPriority);
scheduler.requeue(task);
```
- Change priority of queued tasks
- Requires custom priority queue

### 5. Task Timeout
```java
Future<?> future = executor.submit(task);
future.get(timeout, TimeUnit.SECONDS);
```
- Kill tasks that run too long
- Prevent resource exhaustion

### 6. Dead Letter Queue
```java
if (task.retries >= maxRetries) {
    deadLetterQueue.add(task);
}
```
- Store permanently failed tasks
- Manual intervention or analysis

---

## 📚 Real-World Applications

### 1. Cron Job Systems
- Linux cron
- Kubernetes CronJobs
- AWS EventBridge

### 2. Task Queues
- Celery (Python)
- Sidekiq (Ruby)
- Bull (Node.js)

### 3. Workflow Engines
- Apache Airflow
- Temporal
- AWS Step Functions

### 4. CI/CD Pipelines
- Jenkins
- GitHub Actions
- GitLab CI

### 5. Data Processing
- Apache Spark job scheduling
- Hadoop YARN
- Kubernetes Jobs

---

## 🎓 Key Learnings

1. **Priority Queues** - Efficient task ordering
2. **Thread Pools** - Managing concurrent execution
3. **Dependency Resolution** - Topological sorting concepts
4. **Retry Strategies** - Exponential backoff in distributed systems
5. **Observer Pattern** - Event-driven architecture
6. **Strategy Pattern** - Pluggable algorithms
7. **Concurrent Programming** - Thread safety, atomic operations
8. **Resource Management** - Graceful shutdown, cleanup

---

## 🏢 Interview Tips

**Common Questions:**
1. "How would you handle circular dependencies?"
   - Use DFS to detect cycles
   - Reject tasks with circular dependencies

2. "How would you scale this to millions of tasks?"
   - Distributed queue (Redis/RabbitMQ)
   - Partitioning by task type or priority
   - Multiple scheduler instances

3. "What if a task never completes?"
   - Implement timeout mechanism
   - Use `Future.get(timeout)`
   - Move to dead letter queue

4. "How do you ensure tasks aren't lost on crash?"
   - Persist tasks to database
   - Mark as IN_PROGRESS when executing
   - Recover pending tasks on restart

5. "How would you implement task preemption?"
   - Use `Future.cancel(true)`
   - Require tasks to check `Thread.interrupted()`
   - Re-queue preempted tasks

---

**Time to Implement:** 90 minutes (target for hard-level problems)

**Companies that ask this:** Google, Amazon, Uber, LinkedIn, Airbnb, Netflix

**Difficulty:** ⭐⭐⭐⭐ Hard
