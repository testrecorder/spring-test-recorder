package com.onushi.testrecorder.codegenerator.test;

import com.onushi.testrecorder.utils.ServiceCreatorUtils;
import org.junit.jupiter.api.BeforeEach;

abstract class TestGeneratorServiceTest {
    protected TestGeneratorFactory testGeneratorFactory;
    protected TestGeneratorService testGeneratorService;

    @BeforeEach
    protected void setUp() {
        testGeneratorFactory = ServiceCreatorUtils.createTestGeneratorFactory();
        testGeneratorService = ServiceCreatorUtils.createTestGeneratorService();
    }
}
