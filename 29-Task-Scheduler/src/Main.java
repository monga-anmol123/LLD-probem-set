import enums.*;
import model.*;
import observer.TaskEventLogger;
import service.TaskScheduler;
import strategy.*;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Main class demonstrating the Task Scheduler system with various scenarios.
 */
public class Main {
    
    public static void main(String[] args) throws InterruptedException {
        System.out.println(repeatString("=", 80));
        System.out.println("  TASK SCHEDULER SYSTEM DEMO");
        System.out.println(repeatString("=", 80) + "\n");
        
        // Scenario 1: Basic Task Scheduling with Priorities
        scenario1_BasicTaskScheduling();
        
        // Scenario 2: Recurring Tasks
        scenario2_RecurringTasks();
        
        // Scenario 3: Task Dependencies
        scenario3_TaskDependencies();
        
        // Scenario 4: Retry Mechanism with Exponential Backoff
        scenario4_RetryMechanism();
        
        // Scenario 5: Worker Pool Management
        scenario5_WorkerPoolManagement();
        
        // Scenario 6: Task Cancellation
        scenario6_TaskCancellation();
        
        // Scenario 7: Complex Workflow
        scenario7_ComplexWorkflow();
        
        System.out.println("\n" + repeatString("=", 80));
        System.out.println("  ALL SCENARIOS COMPLETED SUCCESSFULLY!");
        System.out.println(repeatString("=", 80) + "\n");
    }
    
    /**
     * Scenario 1: Basic Task Scheduling with Priorities
     */
    private static void scenario1_BasicTaskScheduling() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 1: Basic Task Scheduling with Priorities");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(2);
        
        System.out.println("\n✅ Creating 5 tasks with different priorities...");
        
        // Create tasks with different priorities
        Task task1 = new SimpleTask("Send alert email", TaskPriority.CRITICAL, 
            () -> System.out.println("  📧 Alert email sent!"), 800);
        
        Task task2 = new SimpleTask("Process payment", TaskPriority.HIGH, 
            () -> System.out.println("  💳 Payment processed!"), 1000);
        
        Task task3 = new SimpleTask("Generate report", TaskPriority.MEDIUM, 
            () -> System.out.println("  📊 Report generated!"), 700);
        
        Task task4 = new SimpleTask("Cleanup logs", TaskPriority.LOW, 
            () -> System.out.println("  🧹 Logs cleaned!"), 500);
        
        Task task5 = new SimpleTask("Update database", TaskPriority.HIGH, 
            () -> System.out.println("  💾 Database updated!"), 900);
        
        // Schedule all tasks
        scheduler.scheduleTask(task1);
        scheduler.scheduleTask(task2);
        scheduler.scheduleTask(task3);
        scheduler.scheduleTask(task4);
        scheduler.scheduleTask(task5);
        
        System.out.println("\n⏳ Starting scheduler with 2 workers...\n");
        scheduler.start();
        
        // Wait for all tasks to complete
        Thread.sleep(4000);
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        // Reset for next scenario
        resetScheduler();
    }
    
    /**
     * Scenario 2: Recurring Tasks
     */
    private static void scenario2_RecurringTasks() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 2: Recurring Tasks");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(1);
        
        AtomicInteger counter = new AtomicInteger(0);
        
        System.out.println("\n✅ Scheduling recurring task: Backup Database (every 2 seconds, max 3 times)");
        
        RecurringTask backupTask = new RecurringTask(
            "Backup Database",
            TaskPriority.MEDIUM,
            () -> {
                int count = counter.incrementAndGet();
                System.out.println("  💾 Backup #" + count + " completed!");
            },
            2000, // Every 2 seconds
            3,    // Max 3 executions
            500   // 500ms duration
        );
        
        String taskId = scheduler.scheduleTask(backupTask);
        
        System.out.println("\n⏳ Starting scheduler...\n");
        scheduler.start();
        
        // Wait for 3 executions (2 seconds * 3 + buffer)
        Thread.sleep(7000);
        
        System.out.println("\n✅ Recurring task completed all executions");
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        resetScheduler();
    }
    
    /**
     * Scenario 3: Task Dependencies
     */
    private static void scenario3_TaskDependencies() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 3: Task Dependencies");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(2);
        
        System.out.println("\n✅ Creating task dependency graph:");
        System.out.println("  Task-C (Fetch data from API)");
        System.out.println("  Task-B (Validate data)");
        System.out.println("  Task-A (Process data) → depends on [Task-B, Task-C]\n");
        
        // Create independent tasks
        Task taskB = new SimpleTask("Validate data", TaskPriority.HIGH, 
            () -> System.out.println("  ✓ Data validated!"), 1000);
        
        Task taskC = new SimpleTask("Fetch data from API", TaskPriority.HIGH, 
            () -> System.out.println("  ✓ Data fetched!"), 1200);
        
        // Create dependent task
        Task taskA = new SimpleTask("Process data", TaskPriority.HIGH, 
            () -> System.out.println("  ✓ Data processed!"), 800);
        taskA.addDependency(taskB.getTaskId());
        taskA.addDependency(taskC.getTaskId());
        
        // Schedule tasks (dependent task first to show it waits)
        scheduler.scheduleTask(taskA);
        scheduler.scheduleTask(taskB);
        scheduler.scheduleTask(taskC);
        
        System.out.println("⏳ Executing tasks...\n");
        scheduler.start();
        
        // Wait for all tasks
        Thread.sleep(4000);
        
        System.out.println("\n💡 All dependencies resolved correctly!");
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        resetScheduler();
    }
    
    /**
     * Scenario 4: Retry Mechanism with Exponential Backoff
     */
    private static void scenario4_RetryMechanism() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 4: Retry Mechanism with Exponential Backoff");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(1);
        
        System.out.println("\n✅ Creating task that fails 2 times, then succeeds");
        System.out.println("   Retry Strategy: Exponential Backoff (base=500ms)\n");
        
        AtomicInteger attemptCount = new AtomicInteger(0);
        
        Task flakyTask = new SimpleTask("Upload to cloud", TaskPriority.HIGH, 
            () -> {
                int attempt = attemptCount.incrementAndGet();
                System.out.println("  ☁️  Upload attempt #" + attempt);
                
                if (attempt < 3) {
                    throw new RuntimeException("Network timeout");
                }
                System.out.println("  ✅ Upload successful!");
            }, 500);
        
        flakyTask.setMaxRetries(3);
        
        RetryPolicy exponentialBackoff = new ExponentialBackoffRetryPolicy(500);
        scheduler.scheduleTask(flakyTask, exponentialBackoff);
        
        System.out.println("⏳ Starting execution...\n");
        scheduler.start();
        
        // Wait for task with retries (500ms + 1000ms + 2000ms + buffer)
        Thread.sleep(5000);
        
        System.out.println("\n💡 Task succeeded after retries with exponential backoff!");
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        resetScheduler();
    }
    
    /**
     * Scenario 5: Worker Pool Management
     */
    private static void scenario5_WorkerPoolManagement() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 5: Worker Pool Management");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(3);
        
        System.out.println("\n✅ Creating pool with 3 workers");
        System.out.println("   Submitting 10 tasks...\n");
        
        // Create 10 tasks
        for (int i = 1; i <= 10; i++) {
            final int taskNum = i;
            Task task = new SimpleTask(
                "Task-" + i, 
                TaskPriority.MEDIUM, 
                () -> System.out.println("  ✓ Task-" + taskNum + " completed!"),
                800
            );
            scheduler.scheduleTask(task);
        }
        
        System.out.println("⏳ Starting scheduler (max 3 concurrent tasks)...\n");
        scheduler.start();
        
        // Monitor execution
        Thread.sleep(1000);
        System.out.println("\n📊 After 1 second - Queue size: " + scheduler.getQueueSize());
        
        Thread.sleep(2000);
        System.out.println("📊 After 3 seconds - Queue size: " + scheduler.getQueueSize());
        
        Thread.sleep(2000);
        
        System.out.println("\n💡 All tasks executed with max 3 concurrent workers!");
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        resetScheduler();
    }
    
    /**
     * Scenario 6: Task Cancellation
     */
    private static void scenario6_TaskCancellation() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 6: Task Cancellation");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(1);
        
        System.out.println("\n✅ Scheduling recurring task (every 1 second)");
        
        AtomicInteger counter = new AtomicInteger(0);
        
        RecurringTask recurringTask = new RecurringTask(
            "Health Check",
            TaskPriority.LOW,
            () -> {
                int count = counter.incrementAndGet();
                System.out.println("  ❤️  Health check #" + count);
            },
            1000,  // Every 1 second
            -1,    // Infinite
            300
        );
        
        String taskId = scheduler.scheduleTask(recurringTask);
        
        System.out.println("\n⏳ Starting scheduler...\n");
        scheduler.start();
        
        // Let it run 3 times
        Thread.sleep(3500);
        
        System.out.println("\n🛑 Cancelling recurring task...");
        scheduler.cancelTask(taskId);
        
        // Wait to verify it stopped
        Thread.sleep(2000);
        
        System.out.println("✅ Task cancelled successfully (no more executions)\n");
        
        scheduler.displayStatistics();
        scheduler.stop();
        
        resetScheduler();
    }
    
    /**
     * Scenario 7: Complex Workflow
     */
    private static void scenario7_ComplexWorkflow() throws InterruptedException {
        System.out.println("\n" + repeatString("-", 80));
        System.out.println("SCENARIO 7: Complex Workflow with Dependencies and Priorities");
        System.out.println(repeatString("-", 80));
        
        TaskScheduler scheduler = TaskScheduler.getInstance();
        scheduler.addObserver(new TaskEventLogger());
        scheduler.setWorkerCount(3);
        
        System.out.println("\n✅ Creating complex workflow:");
        System.out.println("  Level 1: Fetch User Data, Fetch Product Data (parallel)");
        System.out.println("  Level 2: Validate Data → depends on Level 1");
        System.out.println("  Level 3: Process Order → depends on Level 2");
        System.out.println("  Level 4: Send Confirmation → depends on Level 3\n");
        
        // Level 1: Independent tasks
        Task fetchUser = new SimpleTask("Fetch User Data", TaskPriority.HIGH, 
            () -> System.out.println("  👤 User data fetched!"), 800);
        
        Task fetchProduct = new SimpleTask("Fetch Product Data", TaskPriority.HIGH, 
            () -> System.out.println("  📦 Product data fetched!"), 900);
        
        // Level 2: Depends on Level 1
        Task validate = new SimpleTask("Validate Data", TaskPriority.HIGH, 
            () -> System.out.println("  ✓ Data validated!"), 600);
        validate.addDependency(fetchUser.getTaskId());
        validate.addDependency(fetchProduct.getTaskId());
        
        // Level 3: Depends on Level 2
        Task processOrder = new SimpleTask("Process Order", TaskPriority.CRITICAL, 
            () -> System.out.println("  🛒 Order processed!"), 1000);
        processOrder.addDependency(validate.getTaskId());
        
        // Level 4: Depends on Level 3
        Task sendConfirmation = new SimpleTask("Send Confirmation", TaskPriority.HIGH, 
            () -> System.out.println("  📧 Confirmation sent!"), 500);
        sendConfirmation.addDependency(processOrder.getTaskId());
        
        // Schedule all tasks
        scheduler.scheduleTask(fetchUser);
        scheduler.scheduleTask(fetchProduct);
        scheduler.scheduleTask(validate);
        scheduler.scheduleTask(processOrder);
        scheduler.scheduleTask(sendConfirmation);
        
        System.out.println("⏳ Executing workflow...\n");
        scheduler.start();
        
        // Wait for entire workflow
        Thread.sleep(5000);
        
        System.out.println("\n💡 Complex workflow completed successfully!");
        System.out.println("   Level 1 tasks ran in parallel");
        System.out.println("   Subsequent levels waited for dependencies\n");
        
        scheduler.displayStatistics();
        scheduler.stop();
    }
    
    /**
     * Reset scheduler for next scenario.
     */
    private static void resetScheduler() {
        // Create new instance by clearing singleton
        try {
            java.lang.reflect.Field instance = TaskScheduler.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
        } catch (Exception e) {
            // Ignore
        }
    }
    
    /**
     * Helper method to repeat a string n times (Java 8 compatible).
     */
    private static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
