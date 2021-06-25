/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.springtestrecorder.codegenerator.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringGeneratorTest {

    @Test
    void generate() {
        // Arrange + Act
        String result = new StringGenerator()
                .setTemplate("Hello {{name}}!")
                .addAttribute("name", "World")
                .generate();

        // Assert
        assertEquals("Hello World!", result);
    }
}
