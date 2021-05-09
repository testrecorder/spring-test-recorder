package com.onushi.sampleapp.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String name;
    private int age;
    private boolean isStudent;

    public Employee setName(String name) {
        this.name = name;
        return this;
    }

    public Employee setAge(int age) {
        this.age = age;
        return this;
    }

    public Employee setStudent(boolean student) {
        isStudent = student;
        return this;
    }
}
