/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package org.sample.model;

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

