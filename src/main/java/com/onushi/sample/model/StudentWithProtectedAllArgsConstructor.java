/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.sample.model;

public class StudentWithProtectedAllArgsConstructor {
    public final String firstName;
    private final String lastName;

    protected StudentWithProtectedAllArgsConstructor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public static StudentWithProtectedAllArgsConstructor createStudent(String firstName, String lastName) {
        return new StudentWithProtectedAllArgsConstructor(firstName, lastName);
    }
}
