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

    public Person() {
    }

    private Person(String firstName) {
        this.firstName = firstName;
    }

    public Person(String firstName, String lastName, Date dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
    }
}
