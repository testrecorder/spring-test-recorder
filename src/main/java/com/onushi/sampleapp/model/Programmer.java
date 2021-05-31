package com.onushi.sampleapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

// For testing asserts on result fields
@Builder
@Setter
public class Programmer {
    public int id;
    public String firstName;
    public String lastName;
    public Department department;
    @Getter private double salary;
    @Getter private int isSenior;
    @Getter private int isolation;
    @Getter private boolean isOnline;
    @Getter private Boolean isOnline1;
    @Getter private boolean registered;
    @Getter private boolean isometric;
    @Getter private boolean is;
    @Getter private boolean isa;
    @Getter private boolean isA;
    @Getter private String otherField;
}
