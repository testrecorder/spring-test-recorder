package com.onushi.sampleapp.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class Employee1 {
    private String name;
    private int age;
    private boolean isStudent;

    public Employee1 setName(String name) {
        this.name = name;
        return this;
    }

    public Employee1 setAge(int age) {
        this.age = age;
        return this;
    }

    public Employee1 setStudent(boolean student) {
        isStudent = student;
        return this;
    }
}
