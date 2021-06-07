package com.onushi.sample.model;

public class StudentWithPrivateFields extends StudentWithPrivateField {
    private final String lastName;
    public static String middleName = "mn";

    public StudentWithPrivateFields(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
