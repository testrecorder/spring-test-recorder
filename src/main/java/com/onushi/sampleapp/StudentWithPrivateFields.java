package com.onushi.sampleapp;

import lombok.experimental.FieldNameConstants;

public class StudentWithPrivateFields {
    private String firstName;
    private String lastName;

    public StudentWithPrivateFields(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
