package com.onushi.testrecording.codegenerator.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringServiceTest {

    @Test
    void addPrefixOnAllLines() {
        // Arrange
        StringService stringService = new StringService();

        // Act
        String result = stringService.addPrefixOnAllLines("line1\nline2", "    ");

        // Assert
        assertEquals("    line1\n    line2", result);
    }
}
