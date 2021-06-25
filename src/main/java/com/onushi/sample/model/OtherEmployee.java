/*
 *
 * Copyright (c) 2021 spring-test-recorder contributors
 * This program is made available under the terms of the MIT License.
 *
 */

package com.onushi.sample.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class OtherEmployee {
    private String name;
    private int age;
    private boolean isStudent;

    public OtherEmployee setName(String name) {
        this.name = name;
        return this;
    }

    public OtherEmployee setAge(int age) {
        this.age = age;
        return this;
    }

    public OtherEmployee setStudent(boolean student) {
        isStudent = student;
        return this;
    }
}
