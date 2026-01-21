package service;

import enums.TaskStatus;
import model.*;
import observer.TaskObserver;
import strategy.RetryPolicy;
import strategy.NoRetryPolicy;

import java.util.*;
import java.util.concurrent.*;

/**
 * Main task scheduler service (Singleton).
 * Manages task scheduling, execution, and lifecycle.
 */
public class TaskScheduler {
    private static TaskScheduler instance;
    
    private final Map<String, Task> allTasks;
    private final Map<String, Task> completedTasks;
    private final PriorityBlockingQueue<Task> taskQueue;
    private final WorkerPool workerPool;
    private final TaskMetrics metrics;
    private final List<TaskObserver> observers;
    private final Map<String, RetryPolicy> taskRetryPolicies;
    private final ScheduledExecutorService recurringTaskExecutor;
    private final Map<String, ScheduledFuture<?>> recurringTaskFutures;
    
    private volatile boolean running;
    
    private TaskScheduler() {
        this.allTasks = new ConcurrentHashMap<>();
        this.completedTasks = new ConcurrentHashMap<>();
        this.taskQueue = new PriorityBlockingQueue<>();
        this.workerPool = new WorkerPool(2); // Default 2 workers
        this.metrics = new TaskMetrics();
        this.observers = new CopyOnWriteArrayList<>();
        this.taskRetryPolicies = new ConcurrentHashMap<>();
        this.recurringTaskExecutor = Executors.newScheduledThreadPool(1);
        this.recurringTaskFutures = new ConcurrentHashMap<>();
        this.running = false;
    }
    
    public static synchronized TaskScheduler getInstance() {
        if (instance == null) {
            instance = new TaskScheduler();
        }
        return instance;
    }
    
    /**
     * Configure the number of worker threads.
     */
    public void setWorkerCount(int count) {
        workerPool.setWorkerCount(count);
    }
    
    /**
     * Add an observer for task events.
     */
    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Schedule a task for execution.
     */
    public String scheduleTask(Task task) {
        return scheduleTask(task, new NoRetryPolicy());
    }
    
    /**
     * Schedule a task with a retry policy.
     */
    public String scheduleTask(Task task, RetryPolicy retryPolicy) {
        allTasks.put(task.getTaskId(), task);
        taskRetryPolicies.put(task.getTaskId(), retryPolicy);
        
        metrics.taskScheduled();
        notifyObservers(observer -> observer.onTaskScheduled(task));
        
        // Handle recurring tasks specially
        if (task instanceof RecurringTask) {
            scheduleRecurringTask((RecurringTask) task);
        } else {
            task.setStatus(TaskStatus.QUEUED);
            taskQueue.offer(task);
        }
        
        return task.getTaskId();
    }
    
    /**
     * Schedule a recurring task.
     */
    private void scheduleRecurringTask(RecurringTask task) {
        ScheduledFuture<?> future = recurringTaskExecutor.scheduleAtFixedRate(
            () -> {
                if (task.shouldContinue()) {
                    task.setStatus(TaskStatus.QUEUED);
                    taskQueue.offer(task);
                } else {
                    cancelTask(task.getTaskId());
                }
            },
            0, // Initial delay
            task.getIntervalMillis(),
            TimeUnit.MILLISECONDS
        );
        
        recurringTaskFutures.put(task.getTaskId(), future);
    }
    
    /**
     * Cancel a task.
     */
    public boolean cancelTask(String taskId) {
        Task task = allTasks.get(taskId);
        if (task == null) {
            return false;
        }
        
        // Cancel recurring task future
        if (task instanceof RecurringTask) {
            ScheduledFuture<?> future = recurringTaskFutures.remove(taskId);
            if (future != null) {
                future.cancel(false);
            }
            ((RecurringTask) task).cancel();
        }
        
        task.setStatus(TaskStatus.CANCELLED);
        metrics.taskCancelled();
        notifyObservers(observer -> observer.onTaskCancelled(task));
        
        return true;
    }
    
    /**
     * Start the scheduler.
     */
    public void start() {
        if (running) {
            return;
        }
        
        running = true;
        workerPool.start(this::processTask);
    }
    
    /**
     * Stop the scheduler gracefully.
     */
    public void stop() {
        running = false;
        workerPool.stop();
        recurringTaskExecutor.shutdown();
        try {
            recurringTaskExecutor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Process a single task (called by worker threads).
     */
    private void processTask() {
        try {
            Task task = taskQueue.poll(100, TimeUnit.MILLISECONDS);
            if (task == null) {
                return;
            }
            
            // Check if task is cancelled
            if (task.getStatus() == TaskStatus.CANCELLED) {
                return;
            }
            
            // Check dependencies
            if (!task.getDependencies().isEmpty()) {
                List<String> completedTaskIds = new ArrayList<>(completedTasks.keySet());
                if (!task.canExecute(completedTaskIds)) {
                    // Re-queue task if dependencies not met
                    taskQueue.offer(task);
                    Thread.sleep(100); // Avoid busy waiting
                    return;
                }
            }
            
            // Execute task
            metrics.taskStarted();
            notifyObservers(observer -> observer.onTaskStarted(task));
            
            TaskResult result = task.call();
            
            if (result.isSuccess()) {
                completedTasks.put(task.getTaskId(), task);
                metrics.taskCompleted(result.getExecutionTimeMillis());
                notifyObservers(observer -> observer.onTaskCompleted(task, result));
            } else if (result.shouldRetry()) {
                // Schedule retry with delay
                RetryPolicy retryPolicy = taskRetryPolicies.get(task.getTaskId());
                long retryDelay = retryPolicy.getRetryDelay(result.getRetryAttempt());
                
                if (retryDelay > 0) {
                    Thread.sleep(retryDelay);
                }
                
                task.setStatus(TaskStatus.QUEUED);
                taskQueue.offer(task);
            } else {
                metrics.taskFailed();
                notifyObservers(observer -> observer.onTaskFailed(task, result));
            }
            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Get task by ID.
     */
    public Task getTask(String taskId) {
        return allTasks.get(taskId);
    }
    
    /**
     * Get all tasks.
     */
    public Collection<Task> getAllTasks() {
        return new ArrayList<>(allTasks.values());
    }
    
    /**
     * Get tasks by status.
     */
    public List<Task> getTasksByStatus(TaskStatus status) {
        List<Task> result = new ArrayList<>();
        for (Task task : allTasks.values()) {
            if (task.getStatus() == status) {
                result.add(task);
            }
        }
        return result;
    }
    
    /**
     * Get metrics.
     */
    public TaskMetrics getMetrics() {
        return metrics;
    }
    
    /**
     * Get next task from queue (for monitoring).
     */
    public Task peekNextTask() {
        return taskQueue.peek();
    }
    
    /**
     * Get queue size.
     */
    public int getQueueSize() {
        return taskQueue.size();
    }
    
    /**
     * Check if scheduler is running.
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Notify all observers.
     */
    private void notifyObservers(java.util.function.Consumer<TaskObserver> action) {
        for (TaskObserver observer : observers) {
            try {
                action.accept(observer);
            } catch (Exception e) {
                // Log but don't fail on observer errors
                System.err.println("Observer error: " + e.getMessage());
            }
        }
    }
    
    /**
     * Display scheduler statistics.
     */
    public void displayStatistics() {
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("TASK SCHEDULER STATISTICS");
        System.out.println(repeatString("=", 80));
        System.out.println("Status: " + (running ? "RUNNING" : "STOPPED"));
        System.out.println("Workers: " + workerPool.getWorkerCount());
        System.out.println("Queue Size: " + getQueueSize());
        System.out.println(metrics);
        System.out.println(repeatString("=", 80));
    }
    
    private String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
