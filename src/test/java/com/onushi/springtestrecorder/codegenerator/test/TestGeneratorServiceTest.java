/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.test;

import com.onushi.springtestrecorder.utils.ServiceCreatorUtils;
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
