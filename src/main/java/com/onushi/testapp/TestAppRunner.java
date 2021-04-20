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
        sampleService.addFloats(2f, 3f);
        sampleService.logicalAnd(true, true);
        sampleService.toYYYY_MM_DD_T_HH_MM_SS_Z(new Date());
        sampleService.testTypes((short)6, (byte)4, 5, true, 'c', 1.5);
    }
}
