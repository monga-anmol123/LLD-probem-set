package model;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks metrics for task execution.
 */
public class TaskMetrics {
    private final AtomicInteger totalScheduled;
    private final AtomicInteger totalCompleted;
    private final AtomicInteger totalFailed;
    private final AtomicInteger totalCancelled;
    private final AtomicInteger currentlyRunning;
    private final AtomicLong totalExecutionTime;
    
    public TaskMetrics() {
        this.totalScheduled = new AtomicInteger(0);
        this.totalCompleted = new AtomicInteger(0);
        this.totalFailed = new AtomicInteger(0);
        this.totalCancelled = new AtomicInteger(0);
        this.currentlyRunning = new AtomicInteger(0);
        this.totalExecutionTime = new AtomicLong(0);
    }
    
    public void taskScheduled() {
        totalScheduled.incrementAndGet();
    }
    
    public void taskStarted() {
        currentlyRunning.incrementAndGet();
    }
    
    public void taskCompleted(long executionTime) {
        currentlyRunning.decrementAndGet();
        totalCompleted.incrementAndGet();
        totalExecutionTime.addAndGet(executionTime);
    }
    
    public void taskFailed() {
        currentlyRunning.decrementAndGet();
        totalFailed.incrementAndGet();
    }
    
    public void taskCancelled() {
        totalCancelled.incrementAndGet();
    }
    
    public int getTotalScheduled() {
        return totalScheduled.get();
    }
    
    public int getTotalCompleted() {
        return totalCompleted.get();
    }
    
    public int getTotalFailed() {
        return totalFailed.get();
    }
    
    public int getTotalCancelled() {
        return totalCancelled.get();
    }
    
    public int getCurrentlyRunning() {
        return currentlyRunning.get();
    }
    
    public double getAverageExecutionTime() {
        int completed = totalCompleted.get();
        if (completed == 0) return 0.0;
        return (double) totalExecutionTime.get() / completed;
    }
    
    public double getSuccessRate() {
        int total = totalCompleted.get() + totalFailed.get();
        if (total == 0) return 0.0;
        return (double) totalCompleted.get() / total * 100;
    }
    
    @Override
    public String toString() {
        return String.format(
            "Metrics: Scheduled=%d, Running=%d, Completed=%d, Failed=%d, Cancelled=%d, " +
            "AvgTime=%.2fs, Success=%.1f%%",
            getTotalScheduled(), getCurrentlyRunning(), getTotalCompleted(), 
            getTotalFailed(), getTotalCancelled(),
            getAverageExecutionTime() / 1000.0, getSuccessRate()
        );
    }
}
