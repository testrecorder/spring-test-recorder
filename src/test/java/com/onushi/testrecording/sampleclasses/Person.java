package com.onushi.testrecording.sampleclasses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private int age;
}
