package model;

import enums.TaskPriority;
import enums.TaskStatus;
import enums.TaskType;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * Abstract base class for all tasks.
 * Represents a unit of work to be scheduled and executed.
 */
public abstract class Task implements Callable<TaskResult>, Comparable<Task> {
    private final String taskId;
    private final String name;
    private final TaskType type;
    private TaskPriority priority;
    private TaskStatus status;
    private final List<String> dependencies;
    private int retryCount;
    private int maxRetries;
    private long timeoutMillis;
    private long scheduledTime;
    private long startTime;
    private long endTime;
    private String errorMessage;
    
    public Task(String name, TaskType type, TaskPriority priority) {
        this.taskId = UUID.randomUUID().toString().substring(0, 8);
        this.name = name;
        this.type = type;
        this.priority = priority;
        this.status = TaskStatus.SCHEDULED;
        this.dependencies = new ArrayList<>();
        this.retryCount = 0;
        this.maxRetries = 0;
        this.timeoutMillis = 30000; // 30 seconds default
        this.scheduledTime = System.currentTimeMillis();
    }
    
    /**
     * Execute the task logic. Must be implemented by subclasses.
     */
    protected abstract void execute() throws Exception;
    
    /**
     * Called by the scheduler to run the task.
     */
    @Override
    public TaskResult call() {
        startTime = System.currentTimeMillis();
        status = TaskStatus.RUNNING;
        
        try {
            execute();
            endTime = System.currentTimeMillis();
            status = TaskStatus.COMPLETED;
            return TaskResult.success(this, endTime - startTime);
        } catch (Exception e) {
            endTime = System.currentTimeMillis();
            errorMessage = e.getMessage();
            
            if (retryCount < maxRetries) {
                retryCount++;
                status = TaskStatus.QUEUED; // Will be retried
                return TaskResult.retry(this, e.getMessage(), retryCount);
            } else {
                status = TaskStatus.FAILED;
                return TaskResult.failure(this, e.getMessage());
            }
        }
    }
    
    /**
     * Add a dependency on another task.
     */
    public void addDependency(String taskId) {
        dependencies.add(taskId);
    }
    
    /**
     * Check if this task can be executed (all dependencies met).
     */
    public boolean canExecute(List<String> completedTasks) {
        return completedTasks.containsAll(dependencies);
    }
    
    /**
     * Compare tasks by priority (higher priority first) and then by scheduled time.
     */
    @Override
    public int compareTo(Task other) {
        // Higher priority comes first (reverse order)
        int priorityCompare = other.priority.compareLevel(this.priority);
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        // Earlier scheduled time comes first
        return Long.compare(this.scheduledTime, other.scheduledTime);
    }
    
    // Getters and setters
    public String getTaskId() { return taskId; }
    public String getName() { return name; }
    public TaskType getType() { return type; }
    public TaskPriority getPriority() { return priority; }
    public void setPriority(TaskPriority priority) { this.priority = priority; }
    public TaskStatus getStatus() { return status; }
    public void setStatus(TaskStatus status) { this.status = status; }
    public List<String> getDependencies() { return new ArrayList<>(dependencies); }
    public int getRetryCount() { return retryCount; }
    public int getMaxRetries() { return maxRetries; }
    public void setMaxRetries(int maxRetries) { this.maxRetries = maxRetries; }
    public long getTimeoutMillis() { return timeoutMillis; }
    public void setTimeoutMillis(long timeoutMillis) { this.timeoutMillis = timeoutMillis; }
    public long getScheduledTime() { return scheduledTime; }
    public void setScheduledTime(long scheduledTime) { this.scheduledTime = scheduledTime; }
    public long getStartTime() { return startTime; }
    public long getEndTime() { return endTime; }
    public String getErrorMessage() { return errorMessage; }
    
    public long getDuration() {
        if (startTime > 0 && endTime > 0) {
            return endTime - startTime;
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return String.format("%s (%s, %s)", name, taskId, priority);
    }
}
