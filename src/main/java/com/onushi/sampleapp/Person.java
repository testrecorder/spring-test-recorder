package com.onushi.sampleapp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    private String firstName;
    private String lastName;
    private Date dateOfBirth;

    private Person(String firstName) {
        this.firstName = firstName;
    }
}
