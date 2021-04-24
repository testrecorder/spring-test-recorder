package com.onushi.testrecording.sampleclasses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class PersonWithBuilder {
    private String firstName;
    private String lastName;
    private int age;
}
