package observer;

import model.Task;
import model.TaskResult;

/**
 * Observer interface for task lifecycle events.
 */
public interface TaskObserver {
    /**
     * Called when a task is scheduled.
     */
    void onTaskScheduled(Task task);
    
    /**
     * Called when a task starts executing.
     */
    void onTaskStarted(Task task);
    
    /**
     * Called when a task completes successfully.
     */
    void onTaskCompleted(Task task, TaskResult result);
    
    /**
     * Called when a task fails.
     */
    void onTaskFailed(Task task, TaskResult result);
    
    /**
     * Called when a task is cancelled.
     */
    void onTaskCancelled(Task task);
}
