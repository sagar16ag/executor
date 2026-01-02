package org.example.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class WorkStealingDemo {
    public static void main(String[] args) throws Exception {

        ExecutorService pool = Executors.newWorkStealingPool();


        //Runnable Example (Work-Stealing Pool) - START
        for (int i = 1; i <= 8; i++) {
            int taskId = i;
            pool.submit(() -> {
                System.out.println(
                        "Task " + taskId + " executed by " +
                                Thread.currentThread().getName()
                );
                Thread.sleep(1000);
                return null;
            });
        }
        //Runnable Example (Work-Stealing Pool) - END

        //Callable Example (Work-Stealing Pool) - START

        List<Callable<Integer>> tasks = new ArrayList<>();

        for (int i = 1; i <= 5; i++) {
            int num = i;
            tasks.add(() -> {
                System.out.println("Processing " + num);
                return num * num;
            });
        }

        List<Future<Integer>> results = pool.invokeAll(tasks);

        for (Future<Integer> f : results) {
            System.out.println("Result: " + f.get());
        }

        //Callable Example (Work-Stealing Pool) - END

        // Shutdown the pool
        pool.shutdown();
        pool.awaitTermination(5, TimeUnit.SECONDS);
    }
}
