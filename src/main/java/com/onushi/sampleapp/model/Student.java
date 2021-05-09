package com.onushi.sampleapp.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Student {
    private String firstName;
    private String lastName;
    private int age;
}
