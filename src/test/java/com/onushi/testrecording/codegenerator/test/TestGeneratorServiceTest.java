package com.onushi.testrecording.codegenerator.test;

import com.onushi.testrecording.utils.ServiceCreatorUtils;
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
