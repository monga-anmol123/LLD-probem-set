# Problem 29: Task Scheduler System

## 📋 Problem Statement

Design a **Task Scheduler System** that can schedule, execute, and manage tasks with various scheduling strategies, priorities, dependencies, and retry mechanisms. The system should support both one-time and recurring tasks, handle task failures gracefully, and provide monitoring capabilities.

**Difficulty:** ⭐⭐⭐⭐ Hard

**Interview Frequency:** Very High (Asked at Google, Amazon, Uber, LinkedIn, Airbnb)

---

## 🎯 Functional Requirements

### Core Features

1. **Task Scheduling**
   - Schedule one-time tasks (execute at specific time)
   - Schedule recurring tasks (cron-style expressions)
   - Schedule delayed tasks (execute after X seconds/minutes)
   - Schedule immediate tasks

2. **Task Dependencies**
   - Tasks can depend on other tasks
   - Execute tasks only after dependencies complete
   - Handle circular dependency detection
   - Support parallel execution of independent tasks

3. **Priority Management**
   - Tasks have priorities (LOW, MEDIUM, HIGH, CRITICAL)
   - Higher priority tasks execute first
   - Priority inheritance for dependent tasks

4. **Task Lifecycle**
   - States: SCHEDULED, QUEUED, RUNNING, COMPLETED, FAILED, CANCELLED
   - Track task execution history
   - Support task cancellation
   - Handle task timeouts

5. **Retry Mechanism**
   - Automatic retry on failure
   - Configurable retry count and delay
   - Exponential backoff strategy
   - Fixed delay strategy

6. **Worker Pool**
   - Fixed number of worker threads
   - Dynamic task assignment
   - Worker health monitoring
   - Graceful shutdown

7. **Monitoring & Metrics**
   - Track task execution times
   - Monitor success/failure rates
   - View scheduled tasks
   - View running tasks
   - View task history

---

## 🚫 Non-Functional Requirements

1. **Thread Safety**
   - Handle concurrent task submissions
   - Thread-safe task queue operations
   - Prevent race conditions

2. **Scalability**
   - Support thousands of scheduled tasks
   - Efficient task queue management
   - Minimal memory footprint

3. **Reliability**
   - Graceful error handling
   - No task loss on failures
   - Proper resource cleanup

4. **Performance**
   - Fast task scheduling (O(log n))
   - Efficient priority queue operations
   - Minimal scheduling overhead

5. **Maintainability**
   - Clean separation of concerns
   - Extensible design patterns
   - Clear logging and debugging

---

## 🎨 Design Patterns to Use

1. **Strategy Pattern** - Different retry strategies (Fixed Delay, Exponential Backoff)
2. **Observer Pattern** - Task lifecycle event notifications
3. **Singleton Pattern** - TaskScheduler service
4. **Command Pattern** - Encapsulate task execution logic
5. **Factory Pattern** - Create different types of tasks

---

## 💡 Hints & Approach

### Class Structure

```
29-Task-Scheduler/
├── enums/
│   ├── TaskStatus.java          # SCHEDULED, RUNNING, COMPLETED, FAILED
│   ├── TaskPriority.java        # LOW, MEDIUM, HIGH, CRITICAL
│   ├── TaskType.java            # ONE_TIME, RECURRING, DELAYED
│   └── RetryStrategy.java       # FIXED_DELAY, EXPONENTIAL_BACKOFF
├── model/
│   ├── Task.java                # Abstract task
│   ├── OneTimeTask.java         # Execute once
│   ├── RecurringTask.java       # Execute repeatedly
│   ├── DelayedTask.java         # Execute after delay
│   ├── TaskResult.java          # Execution result
│   └── TaskMetrics.java         # Execution metrics
├── strategy/
│   ├── RetryPolicy.java         # Interface
│   ├── FixedDelayRetry.java     # Fixed delay between retries
│   └── ExponentialBackoffRetry.java  # Exponential backoff
├── observer/
│   ├── TaskObserver.java        # Interface
│   └── TaskEventListener.java   # Concrete observer
├── service/
│   ├── TaskScheduler.java       # Main scheduler (Singleton)
│   ├── WorkerPool.java          # Thread pool management
│   └── TaskQueue.java           # Priority queue
└── Main.java                    # Demo with 6+ scenarios
```

### Key Algorithms

1. **Priority Queue** - Use PriorityQueue for task ordering
2. **Dependency Graph** - Topological sort for task dependencies
3. **Exponential Backoff** - delay = baseDelay * (2 ^ attemptNumber)
4. **Cron Parsing** - Simple cron expression parser

### Edge Cases to Handle

- Circular dependencies between tasks
- Task cancellation while running
- Worker thread failures
- Task timeout handling
- Concurrent task submissions
- Scheduler shutdown with pending tasks

---

## 🧪 Test Scenarios

### Scenario 1: Basic Task Scheduling
```
- Schedule 5 one-time tasks with different priorities
- Verify execution order (CRITICAL → HIGH → MEDIUM → LOW)
- Check all tasks complete successfully
```

### Scenario 2: Recurring Tasks
```
- Schedule recurring task (every 2 seconds)
- Let it execute 3 times
- Cancel the task
- Verify it stops executing
```

### Scenario 3: Task Dependencies
```
- Task A depends on Task B and Task C
- Task B and C can run in parallel
- Task A runs only after both complete
- Verify execution order
```

### Scenario 4: Retry Mechanism
```
- Schedule task that fails 2 times
- Configure 3 retry attempts with exponential backoff
- Verify retries with increasing delays
- Task succeeds on 3rd attempt
```

### Scenario 5: Worker Pool Management
```
- Create pool with 3 workers
- Submit 10 tasks
- Monitor worker utilization
- Verify parallel execution (max 3 concurrent)
```

### Scenario 6: Task Cancellation & Timeout
```
- Schedule long-running task (10 seconds)
- Cancel after 2 seconds
- Schedule task with 3-second timeout
- Verify timeout handling
```

### Scenario 7: Complex Workflow
```
- Multi-level task dependencies (A → B → C → D)
- Mixed priorities
- Some tasks fail and retry
- Monitor entire workflow execution
```

---

## 📊 Expected Output Format

```
================================================================================
  TASK SCHEDULER SYSTEM DEMO
================================================================================

--------------------------------------------------------------------------------
SCENARIO 1: Basic Task Scheduling with Priorities
--------------------------------------------------------------------------------

✅ Scheduled 5 tasks:
  Task-1 (CRITICAL) - Send alert email
  Task-2 (HIGH) - Process payment
  Task-3 (MEDIUM) - Generate report
  Task-4 (LOW) - Cleanup logs
  Task-5 (HIGH) - Update database

⏳ Starting scheduler with 2 workers...

[Worker-1] Executing: Task-1 (CRITICAL) - Send alert email
[Worker-2] Executing: Task-2 (HIGH) - Process payment
[Worker-1] ✅ Completed: Task-1 (Duration: 1.2s)
[Worker-1] Executing: Task-5 (HIGH) - Update database
[Worker-2] ✅ Completed: Task-2 (Duration: 1.5s)
[Worker-2] Executing: Task-3 (MEDIUM) - Generate report
...

📊 Statistics:
  Total Tasks: 5
  Completed: 5
  Failed: 0
  Average Duration: 1.3s

--------------------------------------------------------------------------------
SCENARIO 2: Recurring Tasks
--------------------------------------------------------------------------------

✅ Scheduled recurring task: Backup Database (every 2 seconds)

[00:00:00] Executing: Backup Database (Attempt 1)
[00:00:02] Executing: Backup Database (Attempt 2)
[00:00:04] Executing: Backup Database (Attempt 3)

🛑 Cancelling recurring task...
✅ Task cancelled successfully

--------------------------------------------------------------------------------
SCENARIO 3: Task Dependencies
--------------------------------------------------------------------------------

✅ Task Dependency Graph:
  Task-C (Fetch data from API)
  Task-B (Validate data)
  Task-A (Process data) → depends on [Task-B, Task-C]

⏳ Executing tasks...

[Worker-1] Executing: Task-C (Fetch data from API)
[Worker-2] Executing: Task-B (Validate data)
[Worker-1] ✅ Completed: Task-C
[Worker-2] ✅ Completed: Task-B
[Worker-1] Executing: Task-A (Process data)
[Worker-1] ✅ Completed: Task-A

💡 All dependencies resolved correctly!

...
```

---

## 🎓 Learning Objectives

After implementing this problem, you should understand:

1. **Thread Pool Management** - How to manage worker threads efficiently
2. **Priority Queues** - Task ordering and scheduling
3. **Dependency Resolution** - Topological sorting and DAG traversal
4. **Retry Mechanisms** - Exponential backoff and retry strategies
5. **Observer Pattern** - Event-driven task lifecycle management
6. **Concurrent Programming** - Thread-safe operations and synchronization
7. **Resource Management** - Graceful shutdown and cleanup

---

## 🏢 Real-World Applications

- **Cron Job Systems** - Linux cron, Kubernetes CronJobs
- **Task Queues** - Celery, RabbitMQ, AWS SQS
- **Workflow Engines** - Apache Airflow, Temporal
- **CI/CD Pipelines** - Jenkins, GitHub Actions
- **Data Processing** - Apache Spark job scheduling

---

## 🚀 Extension Ideas

1. **Persistent Storage** - Save tasks to database
2. **Distributed Scheduling** - Multiple scheduler instances
3. **Task Chaining** - Fluent API for task workflows
4. **Rate Limiting** - Limit task execution rate
5. **Task Prioritization** - Dynamic priority adjustment
6. **Monitoring Dashboard** - Real-time task visualization
7. **Dead Letter Queue** - Handle permanently failed tasks

---

## 📚 Related Problems

- Problem 26: API Rate Limiter (Rate limiting concepts)
- Problem 28: Notification Service (Observer pattern)
- Problem 11: Restaurant Management (State management)

---

**Companies that frequently ask this:** Google, Amazon, Uber, LinkedIn, Airbnb, Netflix, Stripe

**Difficulty Level:** Hard (90-minute problem)

**Key Concepts:** Concurrency, Scheduling, Design Patterns, Thread Management
