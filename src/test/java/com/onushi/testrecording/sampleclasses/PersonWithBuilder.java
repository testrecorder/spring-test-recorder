package com.onushi.testrecording.sampleclasses;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PersonWithBuilder {
    private String firstName;
    private String lastName;
    private int age;
}
