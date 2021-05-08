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


    @Test
    void lowerCaseFirstLetter() {
        StringService stringService = new StringService();
        assertEquals("date", stringService.lowerCaseFirstLetter("Date"));
        assertEquals("personRepository", stringService.lowerCaseFirstLetter("PersonRepository"));
        assertEquals("d", stringService.lowerCaseFirstLetter("D"));
        assertThrows(IllegalArgumentException.class, () -> stringService.lowerCaseFirstLetter(""));
    }

    @Test
    void upperCaseFirstLetter() {
        StringService stringService = new StringService();
        assertEquals("Date", stringService.upperCaseFirstLetter("date"));
        assertEquals("PersonRepository", stringService.upperCaseFirstLetter("personRepository"));
        assertEquals("D", stringService.upperCaseFirstLetter("d"));
        assertThrows(IllegalArgumentException.class, () -> stringService.upperCaseFirstLetter(""));
    }
}
