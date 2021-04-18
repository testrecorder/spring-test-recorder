package com.onushi.testapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TestAppRunner implements CommandLineRunner {
    private final Service2 service2;
    public TestAppRunner(Service2 service2) {
        this.service2 = service2;
    }

    @Override
    public void run(String... args) throws Exception {
        int result = service2.add(-6, 4);
        System.out.println("Result of service2.add " + result);
    }
}
