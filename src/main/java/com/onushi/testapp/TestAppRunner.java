package com.onushi.testapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestAppRunner implements CommandLineRunner {
    private final MathService mathService;
    public TestAppRunner(MathService mathService) {
        this.mathService = mathService;
    }

    @Override
    public void run(String... args) throws Exception {
        mathService.addStrings("2", "5");
    }
}
