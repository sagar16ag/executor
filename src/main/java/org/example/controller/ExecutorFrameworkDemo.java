package org.example.controller;

// Executor Framework Complete Demo Project
// Single-file runnable example demonstrating all major features.
// Includes: ExecutorService, ThreadPoolExecutor, Callable, Future,
// ScheduledExecutorService, CompletionService, shutdown patterns.

import java.util.concurrent.*;

public class ExecutorFrameworkDemo {

    // -------------------------------
    // 1. Simple Runnable Task
    // -------------------------------
    static class SimpleTask implements Runnable {
        private final int id;
        public SimpleTask(int id) {
            this.id = id;
        }
        @Override
        public void run() {
            System.out.println("[SimpleTask] Running task " + id + " on " + Thread.currentThread().getName());
        }
    }

    // ---------------------------------
    // 2. Callable Task (returns a value)
    // ---------------------------------
    static class CalculationTask implements Callable<Integer> {
        private final int number;
        public CalculationTask(int number) {
            this.number = number;
        }
        @Override
        public Integer call() {
            System.out.println("[Callable] Calculating square for: " + number + " on " + Thread.currentThread().getName());
            return number * number;
        }
    }

    // ------------------------------------------------
    // 3. Demonstrate ThreadPoolExecutor Custom Settings
    // ------------------------------------------------
    static ExecutorService createCustomThreadPool() {
        return new ThreadPoolExecutor(
                2,                  // core threads
                4,                  // max threads
                10, TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(2), // queue size = 2
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.CallerRunsPolicy() // policy
        );
    }

    // -----------------------------
    // 4. Scheduled Executor Demo
    // -----------------------------
    static void demoScheduledExecutor() {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        System.out.println("\n--- ScheduledExecutorService Demo ---");

        scheduler.schedule(() ->
                        System.out.println("[Scheduled] Runs after 3 seconds"),
                3, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(() ->
                        System.out.println("[FixedRate] Runs every 2 seconds"),
                1, 2, TimeUnit.SECONDS);

        // shutdown later
        scheduler.schedule(() -> {
            System.out.println("Shutting down scheduled executor...");
            scheduler.shutdown();
        }, 10, TimeUnit.SECONDS);

//        // Delay shutdown for 10 seconds
//        try {
//            Thread.sleep(10000);
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }

        scheduler.shutdown();
    }

    // ---------------------------------------
    // 5. CompletionService (Best for Batch)
    // ---------------------------------------
    static void demoCompletionService() throws Exception {
        System.out.println("\n--- CompletionService Demo ---");
        ExecutorService pool = Executors.newFixedThreadPool(3);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(pool);

        for (int i = 1; i <= 5; i++) {
            int n = i;
            completionService.submit(() -> {
                Thread.sleep(1000 * n);
                return n * 10;
            });
        }

        System.out.println("Fetching results as they complete:");
        for (int i = 0; i < 5; i++) {
            Future<Integer> future = completionService.take();
            System.out.println("Result: " + future.get());
        }

        pool.shutdown();
    }

    // ------------------------------
    // 6. Graceful Shutdown Patterns
    // ------------------------------
    static void gracefulShutdown(ExecutorService executor) {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.out.println("Force shutdown initiated...");
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
        }
    }

    // =========================================
    // MAIN: RUN ENTIRE DEMO PROJECT
    // =========================================
    public static void main(String[] args) throws Exception {

        System.out.println("\n===== Executor Framework Full Demo =====\n");

        // 1. Simple fixed thread pool
        System.out.println("--- FixedThreadPool Demo ---");
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        for (int i = 1; i <= 5; i++) {
            fixedPool.submit(new SimpleTask(i));
        }
        gracefulShutdown(fixedPool);

        // 2. Callable + Future demo
        System.out.println("\n--- Callable & Future Demo ---");
        ExecutorService callPool = Executors.newCachedThreadPool();

        Future<Integer> future = callPool.submit(new CalculationTask(12));
        System.out.println("Square Result = " + future.get());
        gracefulShutdown(callPool);

        // 3. Custom ThreadPoolExecutor demo
        System.out.println("\n--- Custom ThreadPoolExecutor Demo ---");
        ExecutorService customPool = createCustomThreadPool();
        for (int i = 0; i < 6; i++) {
            int id = i;
            customPool.submit(() -> System.out.println("[CustomPool] Task " + id));
        }
        gracefulShutdown(customPool);

        // 4. ScheduledExecutorService demo
        demoScheduledExecutor();

        // 5. CompletionService demo
        demoCompletionService();

        System.out.println("\n===== Demo Finished =====");
    }
}
