package org.example.controller;

// Complete Executor Framework Demo Project in a Single Java File
// You can copy and run this entire file directly.

import java.util.*;
import java.util.concurrent.*;

public class ExecutorFrameworkDemo2 {

    // --------------------------------------
    // 1. Simple Runnable Task
    // --------------------------------------
    static class SimpleTask implements Runnable {
        private final int id;
        SimpleTask(int id) { this.id = id; }
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("[SimpleTask] Executing task " + id + " on " + Thread.currentThread().getName());
        }
    }

    // --------------------------------------
    // 2. Callable Task (returns value)
    // --------------------------------------
    static class ComputationTask implements Callable<Integer> {
        private final int num;
        ComputationTask(int num) { this.num = num; }
        @Override
        public Integer call() throws Exception {
            System.out.println("[Callable] Computing square of " + num + " on " + Thread.currentThread().getName());
            return num * num;
        }
    }

    // --------------------------------------
    // 3. Scheduled Executor Demo
    // --------------------------------------
    static class ScheduledPrinter implements Runnable {
        private final String msg;
        ScheduledPrinter(String msg) { this.msg = msg; }
        @Override
        public void run() {
            System.out.println("[Scheduled] " + msg + " at " + new Date());
        }
    }

    // --------------------------------------
    // MAIN METHOD
    // --------------------------------------
    public static void main(String[] args) throws Exception {
        System.out.println("===== EXECUTOR FRAMEWORK DEMO =====");

        // ------------------------------------------------------
        // 1. Fixed Thread Pool
        // ------------------------------------------------------
        ExecutorService fixedPool = Executors.newFixedThreadPool(3);
        for(int i = 1; i <= 10; i++)
            fixedPool.execute(new SimpleTask(i));

        // ------------------------------------------------------
        // 2. Cached Thread Pool
        // ------------------------------------------------------
        ExecutorService cachedPool = Executors.newCachedThreadPool();
        for(int i = 1; i <= 3; i++)
            cachedPool.submit(new SimpleTask(i + 100));

        // ------------------------------------------------------
        // 3. Callable + Future
        // ------------------------------------------------------
        Future<Integer> result = fixedPool.submit(new ComputationTask(12));
        System.out.println("Square Result: " + result.get());

        // ------------------------------------------------------
        // 4. Scheduled Executor
        // ------------------------------------------------------
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
        scheduler.schedule(new ScheduledPrinter("Runs After 3 Seconds"), 3, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new ScheduledPrinter("Repeating Every 2 Seconds"), 1, 2, TimeUnit.SECONDS);

        // Allow demonstration time
        Thread.sleep(7000);

        // Shutdown executors
        fixedPool.shutdown();
        cachedPool.shutdown();
        scheduler.shutdown();

        System.out.println("===== END OF DEMO =====");
    }
}
