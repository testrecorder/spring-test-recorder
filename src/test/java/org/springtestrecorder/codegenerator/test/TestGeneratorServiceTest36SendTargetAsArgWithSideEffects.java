/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.sample.services.ServiceWithState;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;

public class TestGeneratorServiceTest36SendTargetAsArgWithSideEffects extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        final ServiceWithState serviceWithState = new ServiceWithState(5);
        final TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(serviceWithState)
                .methodName("changeService")
                .arguments(Collections.singletonList(serviceWithState))
                .fallBackResultType(void.class)
                .build());
        serviceWithState.changeService(serviceWithState);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(null)
                .build());

        // Act
        final String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        Assertions.assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                "\n" +
                "package org.sample.services;\n" +
                "\n" +
                "import org.junit.jupiter.api.Test;\n" +
                "import static org.junit.jupiter.api.Assertions.*;\n" +
                "\n" +
                "class ServiceWithStateTest {\n" +
                "    //TODO rename the test to describe the use case\n" +
                "    //TODO refactor the generated code to make it easier to understand\n" +
                "    @Test\n" +
                "    void changeService() throws Exception {\n" +
                "        // Arrange\n" +
                "        ServiceWithState serviceWithState = new ServiceWithState(5);\n" +
                "\n" +
                "        // Act\n" +
                "        serviceWithState.changeService(serviceWithState);\n" +
                "\n" +
                "\n" +
                "        // Side Effects\n" +
                "        assertEquals(10, serviceWithState.getSampleInt());\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void changeService() {
        // Arrange
        final ServiceWithState serviceWithState = new ServiceWithState(5);

        // Act
        serviceWithState.changeService(serviceWithState);

        // Side Effects
        assertEquals(10, serviceWithState.getSampleInt());
    }
}
