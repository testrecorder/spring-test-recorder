package com.onushi.testrecording.codegenerator.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringGeneratorTest {

    @Test
    void generate() {
        // Arrange
        StringGenerator stringGenerator = new StringGenerator();
        stringGenerator.setTemplate("Hello {{name}}!");
        stringGenerator.addAttribute("name", "World");

        // Act
        String result = stringGenerator.generate();

        // Assert
        assertEquals(result, "Hello World!");
    }
}
