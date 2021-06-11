package com.onushi.testrecorder.codegenerator.template;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringServiceTest {

    StringService stringService;
    @BeforeEach
    void setUp() {
        stringService = new StringService();
    }

    @Test
    void addPrefixOnAllLines() {
        String result = stringService.addPrefixOnAllLines("line1\nline2", "    ");
        assertEquals("    line1\n    line2", result);
    }


    @Test
    void getVariableName() {
        assertEquals("person", stringService.getVariableName("person"));
        assertEquals("d", stringService.getVariableName("D"));
        assertEquals("uuid", stringService.getVariableName("UUID"));
        assertEquals("date", stringService.getVariableName("Date"));
        assertEquals("personRepository", stringService.getVariableName("PersonRepository"));
        assertEquals("uuidArrayList", stringService.getVariableName("UUIDArrayList"));
        assertEquals("aList", stringService.getVariableName("aList"));
        assertEquals("aList", stringService.getVariableName("AList"));
        assertEquals("anObject", stringService.getVariableName("AnObject"));
        assertEquals("isPerson", stringService.getVariableName("isPerson"));
        assertThrows(IllegalArgumentException.class, () -> stringService.getVariableName(""));
    }

    @Test
    void upperCaseFirstLetter() {
        assertEquals("Date", stringService.upperCaseFirstLetter("date"));
        assertEquals("PersonRepository", stringService.upperCaseFirstLetter("personRepository"));
        assertEquals("D", stringService.upperCaseFirstLetter("d"));
        assertThrows(IllegalArgumentException.class, () -> stringService.upperCaseFirstLetter(""));
    }

    @Test
    void escape() {
        assertNull(stringService.escape(null));
        assertEquals("Date", stringService.escape("Date"));
        assertEquals("\\\\", stringService.escape("\\"));
        assertEquals("\\t", stringService.escape("\t"));
        assertEquals("\\b", stringService.escape("\b"));
        assertEquals("\\n", stringService.escape("\n"));
        assertEquals("\\r", stringService.escape("\r"));
        assertEquals("\\f", stringService.escape("\f"));
        assertEquals("\\\"", stringService.escape("\""));
    }
}
