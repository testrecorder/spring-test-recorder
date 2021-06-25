/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.sample.model;

public class PersonWithProtectedNoArgsConstructor {
    protected PersonWithProtectedNoArgsConstructor() {
    }

    public static PersonWithProtectedNoArgsConstructor createService() {
        //noinspection InstantiationOfUtilityClass
        return new PersonWithProtectedNoArgsConstructor();
    }
}
