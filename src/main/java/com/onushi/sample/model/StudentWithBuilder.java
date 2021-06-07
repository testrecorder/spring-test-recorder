package com.onushi.sample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class StudentWithBuilder {
    private String firstName;
    private String lastName;
    private int age;

    private StudentWithBuilder() {
    }
}
