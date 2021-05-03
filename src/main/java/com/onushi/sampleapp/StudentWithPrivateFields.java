package com.onushi.sampleapp;

public class StudentWithPrivateFields extends StudentWithPrivateField {
    private String lastName;

    public StudentWithPrivateFields(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
