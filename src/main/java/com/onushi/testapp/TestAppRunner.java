package com.onushi.testapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class TestAppRunner implements CommandLineRunner {
    private final SampleService sampleService;
    public TestAppRunner(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @Override
    public void run(String... args) throws Exception {
        sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(new Date());
    }
}
