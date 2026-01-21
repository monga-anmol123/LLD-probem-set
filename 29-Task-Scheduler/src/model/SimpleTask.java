package model;

import enums.TaskPriority;
import enums.TaskType;

import java.util.concurrent.Callable;

/**
 * A simple one-time task that executes a Runnable or Callable.
 */
public class SimpleTask extends Task {
    private final Runnable action;
    private final long simulatedDurationMillis;
    
    public SimpleTask(String name, TaskPriority priority, Runnable action) {
        this(name, priority, action, 1000); // Default 1 second
    }
    
    public SimpleTask(String name, TaskPriority priority, Runnable action, long simulatedDurationMillis) {
        super(name, TaskType.ONE_TIME, priority);
        this.action = action;
        this.simulatedDurationMillis = simulatedDurationMillis;
    }
    
    @Override
    protected void execute() throws Exception {
        // Simulate work
        Thread.sleep(simulatedDurationMillis);
        
        // Execute the actual action
        if (action != null) {
            action.run();
        }
    }
}
