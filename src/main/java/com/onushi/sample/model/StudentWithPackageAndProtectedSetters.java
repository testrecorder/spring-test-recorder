package com.onushi.sample.model;

public class StudentWithPackageAndProtectedSetters {
    private String firstName = "fn";
    private String lastName = "ln";

    StudentWithPackageAndProtectedSetters setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    protected StudentWithPackageAndProtectedSetters setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    protected StudentWithPackageAndProtectedSetters() {
    }

    public static StudentWithPackageAndProtectedSetters createStudent() {
        return new StudentWithPackageAndProtectedSetters();
    }
}

