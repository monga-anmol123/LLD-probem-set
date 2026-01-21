package model;

import enums.TaskPriority;
import enums.TaskType;

/**
 * A task that executes repeatedly at fixed intervals.
 */
public class RecurringTask extends Task {
    private final Runnable action;
    private final long intervalMillis;
    private final long simulatedDurationMillis;
    private int executionCount;
    private final int maxExecutions; // -1 for infinite
    private volatile boolean cancelled;
    
    public RecurringTask(String name, TaskPriority priority, Runnable action, 
                        long intervalMillis, int maxExecutions) {
        this(name, priority, action, intervalMillis, maxExecutions, 500);
    }
    
    public RecurringTask(String name, TaskPriority priority, Runnable action, 
                        long intervalMillis, int maxExecutions, long simulatedDurationMillis) {
        super(name, TaskType.RECURRING, priority);
        this.action = action;
        this.intervalMillis = intervalMillis;
        this.maxExecutions = maxExecutions;
        this.simulatedDurationMillis = simulatedDurationMillis;
        this.executionCount = 0;
        this.cancelled = false;
    }
    
    @Override
    protected void execute() throws Exception {
        if (cancelled) {
            throw new Exception("Task was cancelled");
        }
        
        // Simulate work
        Thread.sleep(simulatedDurationMillis);
        
        // Execute the actual action
        if (action != null) {
            action.run();
        }
        
        executionCount++;
    }
    
    /**
     * Check if this task should continue executing.
     */
    public boolean shouldContinue() {
        return !cancelled && (maxExecutions == -1 || executionCount < maxExecutions);
    }
    
    /**
     * Cancel this recurring task.
     */
    public void cancel() {
        this.cancelled = true;
    }
    
    public long getIntervalMillis() {
        return intervalMillis;
    }
    
    public int getExecutionCount() {
        return executionCount;
    }
    
    public boolean isCancelled() {
        return cancelled;
    }
}
