package com.onushi.sampleapp.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Employee {
    private int id;
    private String firstName;
    private String lastName;
    private Department department;
    private double salaryParam1;
    private double salaryParam2;
    private double salaryParam3;
}
