package model;

import enums.TaskPriority;
import enums.TaskType;

/**
 * A task that executes once after a specified delay.
 */
public class DelayedTask extends Task {
    private final Runnable action;
    private final long delayMillis;
    private final long simulatedDurationMillis;
    
    public DelayedTask(String name, TaskPriority priority, Runnable action, long delayMillis) {
        this(name, priority, action, delayMillis, 500);
    }
    
    public DelayedTask(String name, TaskPriority priority, Runnable action, 
                      long delayMillis, long simulatedDurationMillis) {
        super(name, TaskType.DELAYED, priority);
        this.action = action;
        this.delayMillis = delayMillis;
        this.simulatedDurationMillis = simulatedDurationMillis;
        // Set scheduled time to now + delay
        setScheduledTime(System.currentTimeMillis() + delayMillis);
    }
    
    @Override
    protected void execute() throws Exception {
        // Check if enough time has passed
        long currentTime = System.currentTimeMillis();
        if (currentTime < getScheduledTime()) {
            long remainingDelay = getScheduledTime() - currentTime;
            Thread.sleep(remainingDelay);
        }
        
        // Simulate work
        Thread.sleep(simulatedDurationMillis);
        
        // Execute the actual action
        if (action != null) {
            action.run();
        }
    }
    
    public long getDelayMillis() {
        return delayMillis;
    }
}
