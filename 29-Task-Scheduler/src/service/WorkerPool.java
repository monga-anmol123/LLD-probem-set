package service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manages a pool of worker threads for task execution.
 */
public class WorkerPool {
    private final List<Thread> workers;
    private final AtomicInteger workerCount;
    private volatile boolean running;
    private Runnable taskProcessor;
    
    public WorkerPool(int initialWorkerCount) {
        this.workers = new ArrayList<>();
        this.workerCount = new AtomicInteger(initialWorkerCount);
        this.running = false;
    }
    
    /**
     * Start the worker pool with a task processor.
     */
    public synchronized void start(Runnable taskProcessor) {
        if (running) {
            return;
        }
        
        this.taskProcessor = taskProcessor;
        this.running = true;
        
        // Create and start worker threads
        for (int i = 0; i < workerCount.get(); i++) {
            createAndStartWorker(i + 1);
        }
    }
    
    /**
     * Stop all workers gracefully.
     */
    public synchronized void stop() {
        running = false;
        
        // Interrupt all workers
        for (Thread worker : workers) {
            worker.interrupt();
        }
        
        // Wait for all workers to finish
        for (Thread worker : workers) {
            try {
                worker.join(1000); // Wait up to 1 second per worker
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        workers.clear();
    }
    
    /**
     * Set the number of workers (dynamic scaling).
     */
    public synchronized void setWorkerCount(int count) {
        int currentCount = workerCount.get();
        
        if (count > currentCount) {
            // Add more workers
            for (int i = currentCount; i < count; i++) {
                if (running) {
                    createAndStartWorker(i + 1);
                }
            }
        } else if (count < currentCount) {
            // Remove workers (interrupt excess workers)
            for (int i = count; i < currentCount && i < workers.size(); i++) {
                workers.get(i).interrupt();
            }
        }
        
        workerCount.set(count);
    }
    
    /**
     * Get current worker count.
     */
    public int getWorkerCount() {
        return workerCount.get();
    }
    
    /**
     * Get active worker count.
     */
    public int getActiveWorkerCount() {
        int active = 0;
        for (Thread worker : workers) {
            if (worker.isAlive()) {
                active++;
            }
        }
        return active;
    }
    
    /**
     * Check if pool is running.
     */
    public boolean isRunning() {
        return running;
    }
    
    /**
     * Create and start a new worker thread.
     */
    private void createAndStartWorker(int workerId) {
        Thread worker = new Thread(() -> {
            String workerName = "Worker-" + workerId;
            Thread.currentThread().setName(workerName);
            
            while (running && !Thread.currentThread().isInterrupted()) {
                try {
                    if (taskProcessor != null) {
                        taskProcessor.run();
                    }
                } catch (Exception e) {
                    // Log error but keep worker running
                    System.err.println(workerName + " error: " + e.getMessage());
                }
            }
        });
        
        workers.add(worker);
        worker.start();
    }
}
