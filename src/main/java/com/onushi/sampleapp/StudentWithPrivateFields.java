package com.onushi.sampleapp;

public class StudentWithPrivateFields extends StudentWithPrivateField {
    private String lastName;
    // TODO IB static fields are not handled yet
    public static String middleName = "mn";

    public StudentWithPrivateFields(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
