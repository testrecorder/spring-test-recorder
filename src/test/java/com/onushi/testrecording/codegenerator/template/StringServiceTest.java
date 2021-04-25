package com.onushi.testrecording.codegenerator.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringServiceTest {

    @Test
    void addSuffixOnAllLines() {
        // Arrange
        StringService stringService = new StringService();

        // Act
        String result = stringService.addSuffixOnAllLines("line1\nline2\n", "    ");

        // Assert
        assertEquals("    line1\n    line2\n", result);
    }
}
