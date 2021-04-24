package com.onushi.sampleapp;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class Person {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private boolean isActor;
}
