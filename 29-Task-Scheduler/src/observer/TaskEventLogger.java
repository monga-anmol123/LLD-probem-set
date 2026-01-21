package observer;

import model.Task;
import model.TaskResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Concrete observer that logs task events.
 */
public class TaskEventLogger implements TaskObserver {
    private final SimpleDateFormat timeFormat;
    private final boolean verbose;
    
    public TaskEventLogger(boolean verbose) {
        this.timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
        this.verbose = verbose;
    }
    
    public TaskEventLogger() {
        this(false);
    }
    
    @Override
    public void onTaskScheduled(Task task) {
        if (verbose) {
            log(String.format("📅 SCHEDULED: %s", task));
        }
    }
    
    @Override
    public void onTaskStarted(Task task) {
        log(String.format("▶️  STARTED: %s", task));
    }
    
    @Override
    public void onTaskCompleted(Task task, TaskResult result) {
        log(String.format("✅ COMPLETED: %s (%.2fs)", 
            task, result.getExecutionTimeMillis() / 1000.0));
    }
    
    @Override
    public void onTaskFailed(Task task, TaskResult result) {
        log(String.format("❌ FAILED: %s - %s", task, result.getMessage()));
    }
    
    @Override
    public void onTaskCancelled(Task task) {
        log(String.format("🛑 CANCELLED: %s", task));
    }
    
    private void log(String message) {
        String timestamp = timeFormat.format(new Date());
        System.out.println(String.format("[%s] %s", timestamp, message));
    }
}
