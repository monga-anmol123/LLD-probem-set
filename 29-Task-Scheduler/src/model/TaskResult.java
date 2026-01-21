package model;

/**
 * Represents the result of a task execution.
 */
public class TaskResult {
    private final boolean success;
    private final boolean shouldRetry;
    private final String taskId;
    private final String taskName;
    private final long executionTimeMillis;
    private final String message;
    private final int retryAttempt;
    
    private TaskResult(boolean success, boolean shouldRetry, String taskId, String taskName,
                      long executionTimeMillis, String message, int retryAttempt) {
        this.success = success;
        this.shouldRetry = shouldRetry;
        this.taskId = taskId;
        this.taskName = taskName;
        this.executionTimeMillis = executionTimeMillis;
        this.message = message;
        this.retryAttempt = retryAttempt;
    }
    
    public static TaskResult success(Task task, long executionTime) {
        return new TaskResult(true, false, task.getTaskId(), task.getName(),
                            executionTime, "Task completed successfully", 0);
    }
    
    public static TaskResult failure(Task task, String errorMessage) {
        return new TaskResult(false, false, task.getTaskId(), task.getName(),
                            task.getDuration(), errorMessage, task.getRetryCount());
    }
    
    public static TaskResult retry(Task task, String errorMessage, int retryAttempt) {
        return new TaskResult(false, true, task.getTaskId(), task.getName(),
                            task.getDuration(), errorMessage, retryAttempt);
    }
    
    // Getters
    public boolean isSuccess() { return success; }
    public boolean shouldRetry() { return shouldRetry; }
    public String getTaskId() { return taskId; }
    public String getTaskName() { return taskName; }
    public long getExecutionTimeMillis() { return executionTimeMillis; }
    public String getMessage() { return message; }
    public int getRetryAttempt() { return retryAttempt; }
    
    @Override
    public String toString() {
        if (success) {
            return String.format("✅ SUCCESS: %s (%.2fs)", taskName, executionTimeMillis / 1000.0);
        } else if (shouldRetry) {
            return String.format("🔄 RETRY %d: %s - %s", retryAttempt, taskName, message);
        } else {
            return String.format("❌ FAILED: %s - %s", taskName, message);
        }
    }
}
