package org.example.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.example.service.ExecutorServiceTest;

@RestController
@RequestMapping("/executor")
public class Controller {
    private final ExecutorServiceTest executorServiceTest;

    @Autowired
    public Controller(ExecutorServiceTest executorServiceTest) {
        this.executorServiceTest = executorServiceTest;
    }

    @GetMapping("/test")
    public String testExecutor() {
        return executorServiceTest.testExecutor();
    }
}
