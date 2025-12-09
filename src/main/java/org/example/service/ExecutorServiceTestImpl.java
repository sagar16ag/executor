package org.example.service;

import org.springframework.stereotype.Service;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExecutorServiceTestImpl implements ExecutorServiceTest {
    @Override
    public String testExecutor() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        try {
            Future<String> future = executor.submit(() -> "Executor Service is working!");
            return future.get();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        } finally {
            executor.shutdown();
        }
    }
}

