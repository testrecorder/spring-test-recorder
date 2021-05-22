package com.onushi.testrecording.codegenerator.template;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class StringServiceTest {

    @Test
    void addPrefixOnAllLines() {
        StringService stringService = new StringService();
        String result = stringService.addPrefixOnAllLines("line1\nline2", "    ");
        assertEquals("    line1\n    line2", result);
    }


    @Test
    void getVariableName() {
        StringService stringService = new StringService();
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
        StringService stringService = new StringService();
        assertEquals("Date", stringService.upperCaseFirstLetter("date"));
        assertEquals("PersonRepository", stringService.upperCaseFirstLetter("personRepository"));
        assertEquals("D", stringService.upperCaseFirstLetter("d"));
        assertThrows(IllegalArgumentException.class, () -> stringService.upperCaseFirstLetter(""));
    }
}
