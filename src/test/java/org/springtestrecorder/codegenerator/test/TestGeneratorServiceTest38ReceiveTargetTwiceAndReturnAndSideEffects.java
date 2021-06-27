/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.springtestrecorder.codegenerator.test;

import org.springtestrecorder.sample.services.ServiceWithState;
import org.springtestrecorder.analyzer.methodrun.AfterMethodRunInfo;
import org.springtestrecorder.analyzer.methodrun.BeforeMethodRunInfo;
import org.springtestrecorder.utils.StringUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorServiceTest38ReceiveTargetTwiceAndReturnAndSideEffects extends TestGeneratorServiceTest {
    @Test
    void generateTest() {
        // Arrange
        ServiceWithState serviceWithState = new ServiceWithState(5);
        TestGenerator testGenerator = testGeneratorFactory.createTestGenerator(BeforeMethodRunInfo.builder()
                .target(serviceWithState)
                .methodName("returnService2")
                .arguments(Arrays.asList(serviceWithState, serviceWithState))
                .fallBackResultType(void.class)
                .build());
        serviceWithState.returnService2(serviceWithState, serviceWithState);
        testGeneratorFactory.addAfterMethodRunInfo(testGenerator, AfterMethodRunInfo.builder()
                .result(serviceWithState)
                .build());

        // Act
        String testString = testGeneratorService.generateTestCode(testGenerator);

        // Assert
        Assertions.assertEquals(StringUtils.prepareForCompare("BEGIN GENERATED TEST =========\n" +
                        "\n" +
                        "package org.springtestrecorder.sample.services;\n" +
                        "\n" +
                        "import org.junit.jupiter.api.Test;\n" +
                        "import static org.junit.jupiter.api.Assertions.*;\n" +
                        "\n" +
                        "class ServiceWithStateTest {\n" +
                        "    //TODO rename the test to describe the use case\n" +
                        "    //TODO refactor the generated code to make it easier to understand\n" +
                        "    @Test\n" +
                        "    void returnService2() throws Exception {\n" +
                        "        // Arrange\n" +
                        "        ServiceWithState serviceWithState = new ServiceWithState(5);\n" +
                        "\n" +
                        "        // Act\n" +
                        "        ServiceWithState result = serviceWithState.returnService2(serviceWithState, serviceWithState);\n" +
                        "\n" +
                        "        // Assert\n" +
                        "        assertEquals(11, result.getSampleInt());\n" +
                        "\n" +
                        "        // Side Effects\n" +
                        "        assertEquals(11, serviceWithState.getSampleInt());\n" +
                        "    }\n" +
                        "}\n" +
                        "\n" +
                        "END GENERATED TEST ========="),
                StringUtils.prepareForCompare(testString));
    }

    @Test
    void returnService2() {
        // Arrange
        ServiceWithState serviceWithState = new ServiceWithState(5);

        // Act
        ServiceWithState result = serviceWithState.returnService2(serviceWithState, serviceWithState);

        // Assert
        assertEquals(11, result.getSampleInt());

        // Side Effects
        assertEquals(11, serviceWithState.getSampleInt());
    }
}
