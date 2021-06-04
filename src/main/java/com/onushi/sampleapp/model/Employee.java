package com.onushi.sampleapp.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private Department department;
    private double salaryParam1;
    private double salaryParam2;
    private double salaryParam3;
    // made public for test purposes
    public Color teamColor;
}
